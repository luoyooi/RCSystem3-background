package com.luo.cv.bean;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author shkstart
 * @create 2020-07-22 21:15
 */
public class Movie implements Serializable {
    private String cover;
    private Integer cover_x;
    private Integer cover_y;
    private String id;
    private Boolean is_new;
    private Boolean playable;
    private String rate;
    private String title;
    private String url;

    public Movie() {
    }

    public Movie(String cover, Integer cover_x, Integer cover_y, String id, Boolean is_new, Boolean playable, String rate, String title, String url) {
        this.cover = cover;
        this.cover_x = cover_x;
        this.cover_y = cover_y;
        this.id = id;
        this.is_new = is_new;
        this.playable = playable;
        this.rate = rate;
        this.title = title;
        this.url = url;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Integer getCover_x() {
        return cover_x;
    }

    public void setCover_x(Integer cover_x) {
        this.cover_x = cover_x;
    }

    public Integer getCover_y() {
        return cover_y;
    }

    public void setCover_y(Integer cover_y) {
        this.cover_y = cover_y;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIs_new() {
        return is_new;
    }

    public void setIs_new(Boolean is_new) {
        this.is_new = is_new;
    }

    public Boolean getPlayable() {
        return playable;
    }

    public void setPlayable(Boolean playable) {
        this.playable = playable;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(cover, movie.cover) &&
                Objects.equals(cover_x, movie.cover_x) &&
                Objects.equals(cover_y, movie.cover_y) &&
                Objects.equals(id, movie.id) &&
                Objects.equals(is_new, movie.is_new) &&
                Objects.equals(playable, movie.playable) &&
                Objects.equals(rate, movie.rate) &&
                Objects.equals(title, movie.title) &&
                Objects.equals(url, movie.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cover, cover_x, cover_y, id, is_new, playable, rate, title, url);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "cover='" + cover + '\'' +
                ", cover_x=" + cover_x +
                ", cover_y=" + cover_y +
                ", id='" + id + '\'' +
                ", is_new=" + is_new +
                ", playable=" + playable +
                ", rate='" + rate + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
