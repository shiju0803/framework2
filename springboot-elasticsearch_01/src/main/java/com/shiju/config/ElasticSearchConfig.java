package com.shiju.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;

/**
 * @author shiju
 * @date 2021/06/19 18:41
 * ElasticSearch配置类
 */
// @Configuration
//@ConfigurationProperties(prefix = "elasticsearch") //加载配置文件中的连接参数
public class ElasticSearchConfig {
    private String host;
    private Integer port;

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Bean
    public RestHighLevelClient client() {
        return new RestHighLevelClient(RestClient.builder(
                new HttpHost(host, port, "http")
        ));
    }
}
