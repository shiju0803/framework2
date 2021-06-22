package com.shiju.test;

import com.alibaba.fastjson.JSON;
import com.shiju.domain.Product;
import com.shiju.mapper.ProductMapper;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

/**
 * @author shiju
 * @date 2021/06/22 19:16
 */
@SpringBootTest
public class ProductMapperTest {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private RestHighLevelClient client;

    /**
     * bulk批量导入数据到es中
     */
    @Test
    public void testImportData() throws IOException {
        //1. 查询所有商品信息
        List<Product> list = productMapper.findAll();
        //2. bulk批量导入
        //2.2 创建批量导入请求对象
        BulkRequest bulkRequest = new BulkRequest();
        //2.3 遍历list集合，批量导入数据
        list.stream().forEach(product -> {
            IndexRequest indexRequest = new IndexRequest("j_product");
            indexRequest.source(JSON.toJSONString(product), XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        //2.1 开始批量导入
        BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(response.status());
    }
}
