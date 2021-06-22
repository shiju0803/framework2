1 Elasticsearch是什么？

    是一个搜索的服务器。

2 Elasticsearch核心概念

    索引：相当于mysql中的数据库
    映射：相当于数据库中的表结构
    文档：相当于表中的每一条数据

3 操作ES：

    操作索引：
    注意：没有修改索引
    添加索引：PUT 索引名称,索引名称,...
    查询索引：GET 索引名称,索引名称,...
    删除索引：DELETE 索引名称,索引名称,...
    关闭索引：POST 索引名称/_close
    开启索引：POST 索引名称/_open


	操作映射：
		注意：没有修改映射和删除
		添加映射：
            PUT 索引名称/_mapping
            {
                "properties":{
                    "属性名":{"type":"text","analyzer": "ik_max_word"},
                    "属性名":{"type":"integer"},
                    "属性名":{"type":"keyword"}
                }
            }
		创建索引时添加映射：
            PUT 索引名称
            {
                "mappings":{
                    "properties":{
                        "属性名":{"type":"text","analyzer": "ik_max_word"},
                        "属性名":{"type":"integer"},
                        "属性名":{"type":"keyword"}
                    }
                }
            }
		查询映射：GET 索引名称/_mapping	
		给映射添加字段: 和"添加映射"操作一样
		
		
	操作文档：
		添加文档(指定id)：	
		PUT 索引名称/_doc/id值
		{
			"属性名":"属性值",
			"属性名":"属性值",
			"属性名":"属性值"
		}
		添加文档(不指定id)：自动分配一个默认id	
        PUT 索引名称/_doc
		{
			"属性名":"属性值",
			"属性名":"属性值",
			"属性名":"属性值"
		}
		根据id查询文档：GET 索引名称/_doc/id值
		查询所有文档：	GET 索引名称/_search		
		根据id删除文档：	DELETE 索引名称/_doc/id值		
		根据id修改文档：id值必须是存在的，如果id值不存在就是添加

4 springboot整合ES

    【第一步】添加依赖：spring-boot-starter-data-elasticsearc
    <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
    </dependency>
    【第二步】在application.yml中配置连接参数
    spring:
    elasticsearch:
    rest:
    uris: http://139.9.37.235:9200
    【第三步】在需要的地方注入RestHighLevelClient对象
    @SpringBootTest
    public class ElasticSearchTest {
    
        @Autowired
        private RestHighLevelClient client;
        //.........................
    }