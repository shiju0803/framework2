package com.shiju.test;

import com.alibaba.fastjson.JSON;
import com.shiju.domain.Person;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shiju
 * @date 2021/06/19 18:47
 */
@SpringBootTest
public class ElasticSearchTest {

    @Autowired
    private RestHighLevelClient client;

/*    @Autowired
    private  ElasticsearchRestTemplate template;*/

    /**
     * 自定义配置类加载配置文件测试
     */
    @Test
    public void contextLoads() {
        System.out.println(client);
    }

    //----------导入elasticsearch起步依赖后自动加载配置文件参数-------------------------------------------------

    /**
     * 添加索引
     */

    @Test
    public void createIndex() throws IOException {
        //使用client获取操作索引对象
        IndicesClient indices = client.indices();
        //具体操作获取返回值，RequestOptions主要用来设置一些请求头信息，一般使用默认值
        //设置索引名称
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("黄桂田");
        CreateIndexResponse createIndexResponse = indices.create(createIndexRequest, RequestOptions.DEFAULT);
        //根据返回值判断结果
        System.out.println(createIndexResponse.isAcknowledged());
    }

    /**
     * 添加索引，并添加映射
     */
    @Test
    public void createIndexAndMapping() throws IOException {
        //使用client获取操作索引对象
        IndicesClient indices = client.indices();
        //创建索引
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("shiju");
        //添加映射
        String mapping = "{\n" +
                "    \"properties\": {\n" +
                "      \"name\":{\n" +
                "        \"type\": \"keyword\"\n" +
                "      },\n" +
                "      \"age\":{\n" +
                "        \"type\": \"integer\"\n" +
                "      },\n" +
                "      \"address\":{\n" +
                "        \"type\": \"text\",\n" +
                "        \"analyzer\": \"ik_max_word\"\n" +
                "      }\n" +
                "    }\n" +
                "  }";
        createIndexRequest.mapping(mapping, XContentType.JSON);
        CreateIndexResponse response = indices.create(createIndexRequest, RequestOptions.DEFAULT);
        //打印创建结果
        System.out.println(response.isAcknowledged());
    }

    /**
     * 查询索引
     */
    @Test
    public void queryIndex() throws IOException {
        IndicesClient indices = client.indices();
        GetIndexRequest request = new GetIndexRequest("shiju");
        GetIndexResponse response = indices.get(request, RequestOptions.DEFAULT);
        Map<String, MappingMetadata> mappings = response.getMappings();
        for (String key : mappings.keySet()) {
            System.out.println(key + "===" + mappings.get(key).getSourceAsMap());
        }
    }

    /**
     * 删除索引
     */
    @Test
    public void deleteIndex() throws IOException {
        IndicesClient indices = client.indices();
        DeleteIndexRequest deleteRequest = new DeleteIndexRequest("shiju");
        AcknowledgedResponse deleted = indices.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(deleted.isAcknowledged());
    }

    /**
     * 判断索引是否存在
     */
    @Test
    public void existIndex() throws IOException {
        IndicesClient indices = client.indices();
        GetIndexRequest request = new GetIndexRequest("shiju");
        boolean exists = indices.exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    /**
     * 添加文档，使用map作为数据
     */
    @Test
    public void addDoc1() throws IOException {
        //构造map集合数据
        Map<String, Object> map = new HashMap<>();
        map.put("name", "黄桂田");
        map.put("age", "32");
        map.put("address", "武汉黄陂");
        //创建添加文档的请求对象
        IndexRequest request = new IndexRequest("person").id("5").source(map);
        //添加文档
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        //打印结果
        System.out.println(response.getId());
    }

    /**
     * 添加文档，使用对象作为数据
     */
    @Test
    public void addDoc2() throws IOException {
        //创建person对象，转换成json
        Person p = new Person();
        p.setId("6");
        p.setName("黄桂田");
        p.setAge(23);
        p.setAddress("湖北省武汉市");
        String json = JSON.toJSONString(p);
        //创建添加文档的请求对象
        IndexRequest indexRequest = new IndexRequest("person").id(p.getId()).source(json, XContentType.JSON);
        //添加文档
        IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
        //打印结果
        System.out.println(response.getId());
    }

    /**
     * 修改文档：添加文档时，如果id存在则修改，id不存在则添加
     */

    @Test
    public void UpdateDoc() throws IOException {
        Person person = new Person();
        person.setId("6");
        person.setName("李四");
        person.setAge(20);
        person.setAddress("北京三环车王");

        String data = JSON.toJSONString(person);

        IndexRequest request = new IndexRequest("person").id(person.getId()).source(data, XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println(response.getId());
    }

    /**
     * 根据id查询文档
     */
    @Test
    public void getDoc() throws IOException {
        //设置查询的索引、文档
        GetRequest indexRequest = new GetRequest("person", "6");

        GetResponse response = client.get(indexRequest, RequestOptions.DEFAULT);
        System.out.println(response.getSourceAsString());
    }

    /**
     * 根据id删除文档
     */
    @Test
    public void delDoc() throws IOException {
        //设置要删除的索引、文档
        DeleteRequest deleteRequest = new DeleteRequest("person", "6");

        DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(response.getId());
    }
}
