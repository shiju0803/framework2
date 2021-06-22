package com.shiju.test;

import com.alibaba.fastjson.JSON;
import com.shiju.domain.Goods;
import com.shiju.mapper.GoodsMapper;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.Max;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shiju
 * @date 2021/06/21 09:48
 */
@SpringBootTest
public class TestBulk {

    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 1.批量操作
     */
    @Test
    public void testBulk() throws IOException {
        //创建bulkrequest对象，整合所有操作
        BulkRequest bulk = new BulkRequest();
        /*
            #批量操作
            # 1.删除5号记录
            # 2.添加6号记录
            # 3.修改2号记录 名称为"二号"
        */
        //添加对应操作
        //1.删除5号记录
        DeleteRequest deleteRequest = new DeleteRequest("person", "1");
        bulk.add(deleteRequest);

        //2.添加6号记录
        Map<String, Object> map = new HashMap<>();
        map.put("name", "黄筱甜");
        IndexRequest indexRequest = new IndexRequest("person").id("6").source(map);
        bulk.add(indexRequest);

        //3.修改2号记录 名称为"二号"
        Map<String, Object> mapUpdate = new HashMap<>();
        mapUpdate.put("name", "二号");
        UpdateRequest updateRequest = new UpdateRequest("person", "2").doc(mapUpdate);
        bulk.add(updateRequest);

        //执行批量操作
        BulkResponse response = client.bulk(bulk, RequestOptions.DEFAULT);
        System.out.println(response.status());
    }

    /**
     * 将数据库中的数据批量导入到es中
     */
    @Test
    public void testBulkFromDb() throws IOException {
        //1 从数据库中查询所有数据
        List<Goods> list = goodsMapper.findAll();
        //2 批量导入到es中
        //2.1 创建批量操作的请求对象
        BulkRequest bulkRequest = new BulkRequest();
        //2.2 添加批量操作任务
        list.stream().forEach(goods -> {
            //添加文档
            IndexRequest indexRequest = new IndexRequest("goods")
                    .id(String.valueOf(goods.getId()))
                    .source(JSON.toJSONString(goods), XContentType.JSON);

            bulkRequest.add(indexRequest);
        });
        //2.3 执行批量操作
        BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        //打印结果
        System.out.println(response.status());
    }

    /**
     * 查询所有
     * 1. matchAll
     * 2. 将查询结果封装为Goods对象，装载到List中
     * 3. 分页。默认显示10条
     */
    @Test
    public void testMatchAll() throws IOException {
        //2.创建SearchRequest搜索请求对象
        SearchRequest searchRequest = new SearchRequest("goods");
        //4.创建查询条件构建器
        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();
        //6. 创建查询条件
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        //5. 添加查询条件，并进行分页
        searchBuilder.query(queryBuilder).from(0).size(100);
        //3.添加查询条件构建器
        searchRequest.source(searchBuilder);
        //1. 开始执行查询
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        //7. 获取查询结果
        // 7.1 获取总记录数
        long total = response.getHits().getTotalHits().value;
        System.out.println("总记录数：" + total);
        // 7.2 获取数据
        SearchHit[] hits = response.getHits().getHits();
        //遍历打印
        Arrays.stream(hits)
                .map(hit -> JSON.parseObject(hit.getSourceAsString(), Goods.class))
                .forEach(goods -> System.out.println(goods));
    }

    /**
     * term查询
     * match查询会对查询条件进行分词。然后将分词后的查询条件和词条进行等值匹配默认取并集（OR）
     */
    @Test
    public void testTermQuery() throws IOException {
        //2 创建SearchRequest搜索请求对象
        SearchRequest searchRequest = new SearchRequest("goods");
        //4 创建查询条件构建器
        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();
        //6 创建查询条件
        QueryBuilder queryBuilder = QueryBuilders.termQuery("title", "华为");
        //5 添加查询条件
        searchBuilder.query(queryBuilder).from(0).size(100);
        //3 添加查询条件构建器
        searchRequest.source(searchBuilder);
        //1 开始执行查询
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        //7 获取查询结果
        //7.1 获取总记录数
        long total = response.getHits().getTotalHits().value;
        System.out.println("总记录数：" + total);
        //7.2 获取数据
        SearchHit[] hits = response.getHits().getHits();
        Arrays.stream(hits)
                .map(hit -> JSON.parseObject(hit.getSourceAsString(), Goods.class))
                .forEach(goods -> System.out.println(goods));
    }

    /**
     * match分词查询
     */
    @Test
    public void testMatchQuery() throws IOException {
        //2.创建SearchRequest搜索请求对象
        SearchRequest searchRequest = new SearchRequest("goods");
        //4.创建查询条件构建器
        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();
        //6. 创建查询条件
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("title", "华为手机").operator(Operator.AND);
        //5. 添加查询条件，并进行分页
        searchBuilder.query(queryBuilder).from(0).size(100);
        //3.添加查询条件构建器
        searchRequest.source(searchBuilder);
        //1. 开始执行查询
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        //7. 获取查询结果
        // 7.1 获取总记录数
        long total = response.getHits().getTotalHits().value;
        System.out.println("总记录数：" + total);
        // 7.2 获取数据
        SearchHit[] hits = response.getHits().getHits();
        //遍历打印
        Arrays.stream(hits)
                .map(hit -> JSON.parseObject(hit.getSourceAsString(), Goods.class))
                .forEach(goods -> System.out.println(goods));
    }

    /**
     * 测试聚合分组查询,需求：查找最贵的手机价格和按照品牌统计手机
     */
    @Test
    public void testAggs() throws IOException {
        //2.创建SearchRequest搜索请求对象
        SearchRequest searchRequest = new SearchRequest();
        //4.创建查询条件构建器
        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();
        //6. 创建查询条件
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("title", "手机").operator(Operator.AND);
        //5. 添加查询条件，并进行分页
        AggregationBuilder aggregationBuilder1 = AggregationBuilders.max("max_price").field("price");
        AggregationBuilder aggregationBuilder2 = AggregationBuilders.terms("goods_brands").field("brandName").size(100);
        searchBuilder.query(queryBuilder)
                .aggregation(aggregationBuilder1)
                .aggregation(aggregationBuilder2);
        //3.添加查询条件构建器
        searchRequest.source(searchBuilder);
        //1. 开始执行查询
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        //7. 获取查询结果
        // 7.1 获取总记录数
        long total = response.getHits().getTotalHits().value;
        System.out.println("总记录数：" + total);
        //7.3 获取最大价格
        Max max = response.getAggregations().get("max_price");
        System.out.println("最高价格：" + max.getValue());
        //7.4获取所有品牌信息并打印
        Terms terms = response.getAggregations().get("goods_brands");
        terms.getBuckets().stream().forEach(bucket -> System.out.println(bucket.getKey() + "=" + bucket.getDocCount()));
    }

    /**
     * wildcard查询
     * 会对查询条件进行分词。还可以使用通配符 ?（任意单个字符） 和  * （0个或多个字符）
     */
    @Test
    public void testFuzzyQuery() throws IOException {
        //2. 创建SearchRequest搜索对象
        SearchRequest searchRequest = new SearchRequest();

        //4. 创建查询条件构造器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //6. 模糊查询,华后多个字符
        // QueryBuilder queryBuilder = QueryBuilders.wildcardQuery("title","华*");

        //正则查询
        // QueryBuilder queryBuilder = QueryBuilders.regexpQuery("title","\\w+(.)*");

        //前缀查询
        QueryBuilder queryBuilder = QueryBuilders.prefixQuery("brandName", "三");
        //5. 添加查询条件并分页
        searchSourceBuilder.query(queryBuilder).from(0).size(100);
        //3. 添加查询条件构造器
        searchRequest.source(searchSourceBuilder);
        //1.开始执行查询
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        //7 获取查询结果
        //7.1 获取总记录数
        long total = response.getHits().getTotalHits().value;
        System.out.println("总记录数:" + total);
        //7.2 获取数据
        SearchHit[] hits = response.getHits().getHits();
        Arrays.stream(hits)
                .map(hit -> JSON.parseObject(hit.getSourceAsString(), Goods.class))
                .forEach(goods -> System.out.println(goods));
    }

    /**
     * 范围和排序查询
     */
    @Test
    public void testRangeQuery() throws IOException {
        //2. 创建搜索对象
        SearchRequest searchRequest = new SearchRequest();
        //4. 创建条件查询构造器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //6. 创建查询条件（前缀查询）
        QueryBuilder queryBuilder = QueryBuilders.rangeQuery("price").gte(2000).lte(3000);
        //5. 添加查询条件并分页
        searchSourceBuilder.query(queryBuilder)
                .sort("price", SortOrder.DESC) //降序排序
                .from(0).size(100);
        //3. 添加查询条件构造器
        searchRequest.source(searchSourceBuilder);
        //1.开始执行查询
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        //7 获取查询结果
        //7.1 获取总记录数
        long total = response.getHits().getTotalHits().value;
        System.out.println("总记录数:" + total);
        //7.2 获取数据
        SearchHit[] hits = response.getHits().getHits();
        Arrays.stream(hits)
                .map(hit -> JSON.parseObject(hit.getSourceAsString(), Goods.class))
                .forEach(goods -> System.out.println(goods));
    }

    /**
     * queryString查询-多字段查询
     * query_string：识别query中的连接符（or 、and）
     */
    @Test
    public void testStringQuery() throws IOException {
        //2. 创建搜索对象
        SearchRequest searchRequest = new SearchRequest();
        //4. 创建查询条件构造器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //6. 创建查询条件，queryString字段查询条件
        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery("华为手机")//搜索条件
                .field("title")//搜索字段
                .field("categoryName")
                .field("brandName")
                .defaultOperator(Operator.AND);
        //5. 添加查询条件
        searchSourceBuilder.query(queryBuilder).from(0).size(100);
        //3. 添加查询条件构造器
        searchRequest.source(searchSourceBuilder);
        //1.开始执行查询
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        //7.获取查询结果
        // 7.1 获取总记录数
        long total = response.getHits().getTotalHits().value;
        System.out.println("总记录数：" + total);
        //7.2 获取数据
        SearchHit[] hits = response.getHits().getHits();
        Arrays.stream(hits)
                .map(hit -> JSON.parseObject(hit.getSourceAsString(), Goods.class))
                .forEach(goods -> System.out.println(goods));
    }

    @Test
    public void testBoolQuery() throws IOException {
        //2. 创建搜索对象
        SearchRequest searchRequest = new SearchRequest();
        //4. 创建查询条件构造器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //创建termQuery查询条件，查询品牌名称为:华为
        QueryBuilder mustTermQueryBuilder = QueryBuilders.termQuery("brandName", "华为");
        //创建matchQuery查询条件，查询标题包含：手机
        QueryBuilder mustMatchQueryBuilder = QueryBuilders.matchQuery("title", "手机");
        //创建matchQuery查询条件，查询标题包含：手机
        QueryBuilder filterRangeQueryBuilder = QueryBuilders.rangeQuery("price").gte(1000).lte(3000);
        //6 创建查询条件，bool查询条件
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(mustMatchQueryBuilder)
                .must(filterRangeQueryBuilder)
                .filter(filterRangeQueryBuilder);
        //5. 添加查询条件
        searchSourceBuilder.query(queryBuilder).from(0).size(100);
        //3. 添加查询条件构造器
        searchRequest.source(searchSourceBuilder);
        //1.开始执行查询
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        //7.获取查询结果
        // 7.1 获取总记录数
        long total = response.getHits().getTotalHits().value;
        System.out.println("总记录数：" + total);
        //7.2 获取数据
        SearchHit[] hits = response.getHits().getHits();
        Arrays.stream(hits)
                .map(hit -> JSON.parseObject(hit.getSourceAsString(), Goods.class))
                .forEach(goods -> System.out.println(goods));
    }

    /**
     * 高亮查询，需求：查询title中包含“电视”，并将高亮内容设置给goods的title属性
     * 1 三要素：
     * 高亮字段
     * 前缀
     * 后缀
     * 2 将高亮内容代替原有的title
     */
    @Test
    public void testHighlight() throws IOException {
        //2 创建查询请求对象
        SearchRequest searchRequest = new SearchRequest();
        //4 创建查询条件构建器
        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();
        //6 创建查询条件
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("title", "电视");
        //创建高亮条件
        HighlightBuilder highlightBuilder = new HighlightBuilder()
                .field("title").preTags("<font color='red'>").postTags("</font>");
        //5 添加查询条件
        searchBuilder.query(queryBuilder).highlighter(highlightBuilder);
        //3 添加查询条件构建器
        searchRequest.source(searchBuilder);
        //1 开始执行查询
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        //7 处理查询结果，并将高亮内容设置给goods的title属性
        SearchHit[] hits = response.getHits().getHits(); //命中的数据
        Arrays.stream(hits).map(hit -> {
            //获取"_source"中的原始数据内容
            Goods goods = JSON.parseObject(hit.getSourceAsString(), Goods.class);
            //获取"highlight"高亮内容
            String title = hit.getHighlightFields().get("title").fragments()[0].toString();
            //将高亮的title设置到good中并返回
            goods.setTitle(title);
            return goods;
        }).forEach(goods -> System.out.println(goods));
    }


}
