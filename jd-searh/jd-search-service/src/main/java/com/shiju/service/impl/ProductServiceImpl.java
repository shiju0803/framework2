package com.shiju.service.impl;

import com.alibaba.fastjson.JSON;
import com.shiju.ProductService;
import com.shiju.domain.Product;
import com.shiju.mapper.ProductMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shiju
 * @date 2021/06/18 19:59
 */
@DubboService
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private RestHighLevelClient client;

    /**
     * 从ES中搜索商品信息
     */
    @Override
    public List<Product> search(String searchString) throws IOException {
        //2.创建查询请求对象
        SearchRequest searchRequest = new SearchRequest();
        //4. 创建查询条件构建器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //6. 创建查询条件指定查询类型
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("title", searchString);
        //设置高亮三要素：高亮字段、前缀、后缀
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field("title")
                .preTags("<font color = 'red'>")
                .postTags("</font>");
        //5. 添加查询条件   //from=(当前页码-1)*每页条数 size=每页条数
        sourceBuilder.query(queryBuilder).highlighter(highlightBuilder).from(0).size(1000);
        //3. 添加查询条件构建器
        searchRequest.source(sourceBuilder);
        //1. 执行search查询
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        //7. 处理结果
        SearchHit[] hits = response.getHits().getHits();
        List<Product> list = Arrays.stream(hits).map(hit -> {
            //获取查询到的原始数据，不包括高亮字段，转换成product对象
            Product product = JSON.parseObject(hit.getSourceAsString(), Product.class);
            //获取高亮字段
            String title = hit.getHighlightFields().get("title").fragments()[0].toString();
            //将高亮字段设置到product中
            product.setTitle(title);
            return product;
        }).collect(Collectors.toList());
        return list;
    }


    /**
     * 从mysql中查询全部商品信息
     */
    /*@Override
    public List<Product> search(String word) {
        return productMapper.search(word);
    }*/

    /**
     * 添加商品信息
     */
    @Override
    public void add(Product product) {
        productMapper.add(product);
    }
}
