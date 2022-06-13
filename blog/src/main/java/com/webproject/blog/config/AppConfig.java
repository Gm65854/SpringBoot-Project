package com.webproject.blog.config;

import java.text.SimpleDateFormat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    
    /**
     * Setting straight to a certain day
     *
     * @return
     */

    @Bean("date_bean")
    public SimpleDateFormat simpleDateFormatForDate () {
        return new SimpleDateFormat("yyy-MM-dd");
    }

    /**
     * Setting straight to a certain hour
     *
     * @return
     */

     @Bean("time_bean")
     public SimpleDateFormat simpleDateFormatForTime() {
        return new SimpleDateFormat ("yyyy-MM-dd HH");
     }

}
