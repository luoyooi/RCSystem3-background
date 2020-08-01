package com.luo.cv.service;

import com.luo.cv.bean.Comment;
import com.luo.cv.bean.Movie;

import java.util.List;
import java.util.Map;

/**
 * @author shkstart
 * @create 2020-07-25 0:32
 */
public interface MovieService {
    // 获取电影分析结果
    public Object getAnalyzeData();

    // 根据id获取电影评论信息
    public List<Comment> getCommentsById(String m_id);
}
