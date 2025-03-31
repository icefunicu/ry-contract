package com.ruoyi.web.util;

import org.resrun.sdk.config.SDKClientConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class kaifangqianConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "kaifangqian")
    public SDKClientConfig clientConfig(){
        return new SDKClientConfig();
    }
}
