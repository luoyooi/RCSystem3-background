package com.luo.cv.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 * @author shkstart
 * @create 2020-07-29 16:24
 */
@Configuration
public class CacheConfig {

    @Bean
    public KeyGenerator movieKeyGenerator(){
        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                return "movieList";
            }
        };
    }

    @Bean
    public KeyGenerator commentKeyGenerator(){
        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                return "commentList";
            }
        };
    }

    @Bean
    public KeyGenerator analyzeKeyGenerator(){
        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                return "analyze";
            }
        };
    }

}
