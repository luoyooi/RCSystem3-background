package com.luo.cv.mapper;

import com.luo.cv.bean.Comment;
import com.luo.cv.bean.Movie;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-07-22 22:07
 */
public interface MovieMapper {

    // 批量保存电影信息
    public Integer insertMovies(List<Movie> movies);

    // 删除所有电影信息
    public Integer deleteMovies();

    // 批量保存评论信息
    public Integer insertComments(List<Comment> comments);

    // 根据id查找评论信息
    public List<Comment> selectCommentsById(String m_id);

    // 删除所有评论
    public Integer deleteComments();

}
