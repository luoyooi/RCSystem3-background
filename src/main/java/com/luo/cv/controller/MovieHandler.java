package com.luo.cv.controller;

import com.luo.cv.bean.Comment;
import com.luo.cv.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author shkstart
 * @create 2020-07-25 13:49
 */
@RestController
public class MovieHandler {

    @Autowired
    MovieService movieService;

    @GetMapping("/analyze")
    public Object getAnalyseComment(){
        return movieService.getAnalyzeData();
    }

    @GetMapping("/comments/{m_id}")
    public List<Comment> getCommentsById(@PathVariable("m_id")String m_id){
        return movieService.getCommentsById(m_id);
    }

}
