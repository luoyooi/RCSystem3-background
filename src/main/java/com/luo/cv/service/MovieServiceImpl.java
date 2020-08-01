package com.luo.cv.service;

import com.luo.cv.bean.Comment;
import com.luo.cv.bean.Movie;
import com.luo.cv.mapper.MovieMapper;
import com.luo.cv.util.LtpUtil;
import com.luo.cv.util.commonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author shkstart
 * @create 2020-07-25 0:32
 */
@Service
@PropertySource(value = "classpath:cache.properties")
public class MovieServiceImpl implements MovieService{
    @Autowired
    private MovieMapper movieMapper;

    @Value("${analysis.cache_name}")
    private String cache_name;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Object getAnalyzeData() {
        return redisTemplate.opsForValue().get(cache_name);
    }

    @Override
    public List<Comment> getCommentsById(String m_id) {
        return movieMapper.selectCommentsById(m_id);
    }

}
