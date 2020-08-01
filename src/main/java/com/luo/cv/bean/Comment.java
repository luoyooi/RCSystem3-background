package com.luo.cv.bean;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author shkstart
 * @create 2020-07-24 23:49
 */
public class Comment implements Serializable {
    private Integer id;
    private String m_id;
    private String comment_time;
    private String recommend_level;
    private Integer votes;
    private String comment;

    public Comment() {
    }

    public Comment(Integer id, String m_id, String comment_time, String recommend_level, Integer votes, String comment) {
        this.id = id;
        this.m_id = m_id;
        this.comment_time = comment_time;
        this.recommend_level = recommend_level;
        this.votes = votes;
        this.comment = comment;
    }

    public Comment(String m_id, String comment_time, String recommend_level, Integer votes, String comment) {
        this.id = id;
        this.m_id = m_id;
        this.comment_time = comment_time;
        this.recommend_level = recommend_level;
        this.votes = votes;
        this.comment = comment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getM_id() {
        return m_id;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }

    public String getRecommend_level() {
        return recommend_level;
    }

    public void setRecommend_level(String recommend_level) {
        this.recommend_level = recommend_level;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment1 = (Comment) o;
        return Objects.equals(id, comment1.id) &&
                Objects.equals(m_id, comment1.m_id) &&
                Objects.equals(comment_time, comment1.comment_time) &&
                Objects.equals(recommend_level, comment1.recommend_level) &&
                Objects.equals(votes, comment1.votes) &&
                Objects.equals(comment, comment1.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, m_id, comment_time, recommend_level, votes, comment);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", m_id='" + m_id + '\'' +
                ", comment_time='" + comment_time + '\'' +
                ", recommend_level='" + recommend_level + '\'' +
                ", votes=" + votes +
                ", comment='" + comment + '\'' +
                '}';
    }
}
