package com.bhhan.tram.common;

import io.eventuate.util.spring.swagger.EventuateSwaggerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hbh5274@gmail.com on 2020-11-04
 * Github : http://github.com/bhhan5274
 */

@Configuration
public class CommonSwaggerConfiguration {
    @Bean
    public EventuateSwaggerConfig eventuateSwaggerConfig(){
        return () -> "com.bhhan.tram";
    }
}
