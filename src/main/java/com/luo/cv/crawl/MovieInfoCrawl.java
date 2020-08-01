package com.luo.cv.crawl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luo.cv.bean.Comment;
import com.luo.cv.bean.Movie;
import com.luo.cv.mapper.MovieMapper;
import com.luo.cv.util.CrawlUtil;
import com.luo.cv.util.LtpUtil;
import com.luo.cv.util.commonUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shkstart
 * @create 2020-07-22 20:51
 */
@Component
@PropertySource(value = "classpath:cache.properties")
public class MovieInfoCrawl {
    private static final Logger logger = LoggerFactory.getLogger(MovieInfoCrawl.class);

    @Value("${crawl.url}")
    private String url;

    @Value("${analysis.cache_name}")
    private String cache_name;

    @Value("${crawl.comment_num_ctl}")
    private Integer comment_num_ctl;

    // jackson，用于json和对象的转换
    @Autowired
    private ObjectMapper objectMapper;

    // 用于保存数据到数据库
    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    // 爬取数据以及数据分析入口，定时进行，先清除缓存
    @Scheduled(cron = "0 0 0 * * ?")
    public void start(){
        try {
            // 爬取电影信息
            List<Movie> movies = getMovies();
            if (movies != null){
                logger.debug("电影信息爬取成功！");
                // 清空movies表
                movieMapper.deleteMovies();
                // 插入新电影信息
                movieMapper.insertMovies(movies);
                logger.debug("电影信息已保存！");
                // 爬取评论信息
                logger.debug("开始爬取评论信息！");
                List<Comment> comments = crawlComments(movies);
                logger.debug("评论信息爬取成功！");

                // 如果评论数量大于0
                if (comments.size() > 0)
                {
                    // 清空comments表
                    movieMapper.deleteComments();
                    // 保存评论信息
                    movieMapper.insertComments(comments);
                    // 开始数据分析
                    logger.debug("开始数据分析！");
                    List<Map<String, Object>> mapList = analyseComment(movies, comments);
                    // 覆盖缓存
                    redisTemplate.opsForValue().set(cache_name, mapList);
                    logger.debug("数据分析完成！");

                }else {
                    logger.debug("评论爬取失败");
                }
            } else {
              logger.debug("电影信息爬取失败");
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // 获取所有电影信息
    private List<Movie> getMovies() throws JsonProcessingException {
        // 获取电影信息json
        String movieJson = CrawlUtil.getSource(url);

        List<Movie> movies = null;

        if (movieJson != null)
        {
            // 将json转换成对象
            HashMap<String, ArrayList<Movie>> subjects =
                    objectMapper.readValue(movieJson, new TypeReference<HashMap<String, ArrayList<Movie>>>() {});

            movies = subjects.get("subjects");

        }
        return movies;
    }

    // 根据id获取电影短评
    private List<Comment> getCommentsById(String movieId){

        List<Comment> cmts = null;

        String url = "";
        for (int i = 0; i <= comment_num_ctl; i++) {
            try {
                // 拼接短评url
                url = "https://movie.douban.com/subject/"+movieId+"/comments?start="+(20*i)+"&limit=20&sort=new_score&status=P";

                // 获取html页面
                String html = CrawlUtil.getSource(url);

                // 若获取到页面数据，则初始化容器
                if (html != null && cmts == null)
                {
                    cmts= new ArrayList<>();
                }

                // 提取短评数据
                Document document = Jsoup.parse(html);
                Elements commentDivs = document.select("#comments div[class='comment']");

                for (Element commentDiv : commentDivs) {

                    Elements commentInfo = commentDiv.select("h3 span[class='comment-info']");
                    // 得分
                    String recommendLevel = commentInfo.select("span[class^='allstar']").attr("title");
                    // 时间
                    String commentTime = commentInfo.select("span[class='comment-time']").attr("title");
                    // 有用
                    String votes = commentDiv.select("h3 span[class='comment-vote'] span[class='votes']").text();
                    // 评论
                    String comment = commentDiv.select("p span[class='short']").text();

                    // 封装对象并存放到容器中
                    cmts.add(new Comment(movieId, commentTime, recommendLevel, Integer.valueOf(votes), comment));

                }

                
            }catch (Exception e) {
                logger.info("id为["+movieId+"]的电影在i=" + i + "的位置，评论爬取出错");
            }
        }

        return cmts;
    }

    // 爬取评论信息
    private List<Comment> crawlComments(List<Movie> movies) {
        List<Comment> comments = new ArrayList<>();

        for (Movie movie : movies) {
            try {
                logger.debug(movie.getTitle() + " : 开始爬取评论");
                comments.addAll(getCommentsById(movie.getId()));

                Thread.sleep(80);
                logger.debug(movie.getTitle() + " : 评论爬取完毕");
            }catch (Exception e){
                logger.debug("在爬取"+movie.getTitle() + "时发生错误!");
            }
        }

        return comments;
    }

    // 数据分析入口
    public List<Map<String, Object>> analyseComment(List<Movie> movies, List<Comment> comments) {
        return unionMoviesAndAnalysis(movies, executeAnalyse(comments));
    }

    // 合并电影信息和评论分析结果
    private List<Map<String, Object>> unionMoviesAndAnalysis(List<Movie> movies, Map<String, Map<String, Map<String, Object>>> commentMap){
        List<Map<String, Object>> list = new ArrayList<>(movies.size());
        for (Movie movie : movies) {
            Map<String, Object> movieMap = commonUtil.exchangeObjectToMap(movie);
            Map<String, Map<String, Object>> wordsMap = commentMap.get(movie.getId());
            // 加入综合得分
            movieMap.put("score", calculateScore(wordsMap.get("rcmdMap"), movie.getRate()));
            // 添加分词结果
            movieMap.put("analyze", wordsMap);
            // 加入集合
            list.add(movieMap);
        }

        // 按综合得分进行排序
        List<Map<String, Object>> collect = list.stream().sorted((o1, o2) -> {
            Double score1 = (Double)o2.get("score");
            Double score2 = (Double)o2.get("score");
            return score2.compareTo(score1);
        }).collect(Collectors.toList());

        return collect;
    }

    // 计算得分
    private Double calculateScore(Map<String, Object> rcmdMap, String rate){
        Integer level1 = 0;
        Integer level2 = 0;
        Integer level3 = 0;
        Integer level4 = 0;
        Integer level5 = 0;

        if (rcmdMap.containsKey("力荐"))
            level1 = (Integer) rcmdMap.get("力荐");

        if (rcmdMap.containsKey("推荐"))
            level2 = (Integer) rcmdMap.get("推荐");

        if (rcmdMap.containsKey("还行"))
            level3 = (Integer) rcmdMap.get("还行");

        if (rcmdMap.containsKey("较差"))
            level4 = (Integer) rcmdMap.get("较差");

        if (rcmdMap.containsKey("很差"))
            level5 = (Integer) rcmdMap.get("很差");

        String format = "0";

        if ((level1 + level2 + level3 + level4 + level5) != 0)
        {
            // 计算得分
            format = new DecimalFormat("#.00")
                    .format((10*(level1 + level2) / (level1 + level2 + level3 + level4 + level5) +
                            Double.valueOf(rate)) / 2);
        }

        return Double.valueOf(format);
    }

    // 执行评论分析
    private Map<String, Map<String, Map<String, Object>>> executeAnalyse(List<Comment> comments){

        // 存储总的结果，共享资源
        Map<String, Map<String, Map<String, Object>>> mainMap = new HashMap<>();

        int count = comments.size();
        for (Comment comment : comments) {
            logger.debug("正在分析" + comment.getM_id() + "("+ (count--) +"/" + comments.size() +")");
            // 为评论添加数据分析容器
            if (!mainMap.containsKey(comment.getM_id())){
                mainMap.put(comment.getM_id(), getMovieMap());
            }

            // 分词，把词性分析结果添加到分词容器中
            List<String> words = LtpUtil.executeSegmentors(comment.getComment());
            List<String> tags = LtpUtil.executePostags(words);

            for (int i = 0; i < words.size(); i++) {
                String word = words.get(i);

                // 判断词性，添加到对应的容器中
                switch (tags.get(i))
                {
                    case "a":
                        // 形容词
                        mapPut(mainMap.get(comment.getM_id()).get("adjMap"), word, 1 + comment.getVotes());
                        break;

                    case "i":
                        // 成语
                        mapPut(mainMap.get(comment.getM_id()).get("idiom"), word, 1 + comment.getVotes());
                        break;

                    case "v":
                        // 动词
                        mapPut(mainMap.get(comment.getM_id()).get("verbMap"), word, 1 + comment.getVotes());
                        break;

                    case "n":
                        // 名词
                        mapPut(mainMap.get(comment.getM_id()).get("nounMap"), word, 1 + comment.getVotes());
                        break;
                }
            }
            // 统计评论等级数量
            mapPut(mainMap.get(comment.getM_id()).get("rcmdMap"), comment.getRecommend_level(), 1 + comment.getVotes());

        }

        // 释放模型
        LtpUtil.releaseSegmentor();
        LtpUtil.releasePostags();

        // 对分析结果进行排序筛选
        for (Map.Entry<String, Map<String, Map<String, Object>>> mainEntry : mainMap.entrySet()) {
            for (Map.Entry<String, Map<String, Object>> movieEntry : mainEntry.getValue().entrySet()) {
                switch (movieEntry.getKey())
                {
                    case "adjMap":
                        movieEntry.setValue(mapSort(movieEntry.getValue(), new Integer[]{0, Integer.MAX_VALUE, 2, 10}));
                        break;

                    case "idiom":
                        movieEntry.setValue(mapSort(movieEntry.getValue(), new Integer[]{0, Integer.MAX_VALUE, 4, 10}));
                        break;

                    case "verbMap":
                        movieEntry.setValue(mapSort(movieEntry.getValue(), new Integer[]{0, Integer.MAX_VALUE, 2, 4}));
                        break;

                    case "nounMap":
                        movieEntry.setValue(mapSort(movieEntry.getValue(), new Integer[]{0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE}));
                        break;
                }
            }
        }

        return mainMap;
    }

    // 获取电影分析结果容器
    private Map<String, Map<String, Object>> getMovieMap(){
        // 存储单个电影的所有分析结果
        Map<String, Map<String, Object>> movieMap = new HashMap<>();

        movieMap.put("rcmdMap", new HashMap<>());
        movieMap.put("adjMap", new HashMap<>());
        movieMap.put("idiom", new HashMap<>());
        movieMap.put("verbMap", new HashMap<>());
        movieMap.put("nounMap", new HashMap<>());

        return movieMap;
    }

    // 如果map中不存在键，添加键值，否则在原基础上累加值
    private void mapPut(Map<String, Object> map, String key, Integer num){
        if (!map.containsKey(key)){
            // 添加新键及其对应的初始值
            map.put(key, num);
        }else {
            // 否则，在原来值的基础上新增值
            map.put(key, (Integer)map.get(key) + num);
        }
    }

    // 过滤及排序评论分析结果
    private Map<String, Object> mapSort(Map<String, Object> map, Integer[] condition){

        LinkedHashMap<String, Object> collect = map.entrySet().stream()
                .filter(m -> (Integer)m.getValue() >= condition[0]
                        && (Integer)m.getValue() <= condition[1]
                        && m.getKey().length() >= condition[2]
                        && m.getKey().length() <= condition[3]
                ).sorted(Map.Entry.comparingByValue((o1, o2) -> (Integer)o2 - (Integer) o1))
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldVal, newVal) -> oldVal,
                                LinkedHashMap::new
                        ));

        Iterator<Map.Entry<String, Object>> iterator = collect.entrySet().iterator();

        List<String> keyList = new ArrayList<>(collect.size());
        List<Integer> valueList = new ArrayList<>(collect.size());

        while (iterator.hasNext()){
            Map.Entry<String, Object> next = iterator.next();

            keyList.add(next.getKey());
            valueList.add((Integer) next.getValue());
        }

        Map<String, Object>  listMap = new HashMap<>(2);
        listMap.put("keyList", keyList);
        listMap.put("valueList", valueList);

        return listMap;
    }
}
