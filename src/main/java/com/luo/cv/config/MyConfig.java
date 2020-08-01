package com.luo.cv.config;

import com.luo.cv.filter.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import javax.servlet.Filter;
import java.util.Arrays;

/**
 * @author shkstart
 * @create 2020-07-27 15:46
 */
@Configuration
public class MyConfig {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();

        template.setDefaultSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    /**
     * 注册跨域允许访问的过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean<Filter> filterRegistrationBean(){
        FilterRegistrationBean<Filter> filter = new FilterRegistrationBean<>();

        filter.setFilter(new CorsFilter());
        filter.setUrlPatterns(Arrays.asList("/*"));

        return filter;
    }
}
