package com.hipi.code.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author hipi
 */
@Configuration
@Data
public class TraceLinkConfig {

    @Value("${trace.link}")
    private String link;

}
