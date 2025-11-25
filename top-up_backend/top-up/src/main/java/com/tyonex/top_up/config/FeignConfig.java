package com.tyonex.top_up.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.tyonex.top_up.client")
public class FeignConfig {
}

