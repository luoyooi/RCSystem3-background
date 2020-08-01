package com.luo.cv;

import com.luo.cv.bean.Comment;
import com.luo.cv.bean.Movie;
import com.luo.cv.crawl.MovieInfoCrawl;
import com.luo.cv.mapper.MovieMapper;
import com.luo.cv.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class CvApplicationTests {
    @Autowired
    MovieInfoCrawl movieInfoCrawl;

    @Autowired
    MovieMapper movieMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
//        movieInfoCrawl.start();
    }

    @Test
    void contextLoads1() {
//        redisTemplate.delete("aaaa");
    }
}
