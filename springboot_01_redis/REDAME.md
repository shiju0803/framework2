## SpringBoot整合redis

### 1 搭建SpringBoot工程

### 2 引入redis起步依赖

```xml
<dependencies>
    <!--junit起步依赖-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
    </dependency>

    <!--springboot整合redis起步依赖-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
</dependencies>
```

### 3 配置redis相关属性

```yaml
spring:
  redis:
    host: 127.0.0.1 # redis的主机ip
    port: 6379
```

### 4 编写测试方法，测试

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootRedisApplicationTests {

  @Autowired
  private RedisTemplate redisTemplate;

  @Test
  public void testSet() {
    //存入数据,底层调用的还是opsForValue().set(key,value)方法
    redisTemplate.boundValueOps("name").set("snake");
  }

  @Test
  public void testGet() {
    //获取数据
    Object name = redisTemplate.boundValueOps("name").get();
    System.out.println(name);
  }
}
```

### 5 存在的问题

**==现象：==**在redis中查看报错的name，结果如下

"\xac\xed\x00\x05t\x00\x04name"

==**原因：**==我们在使用RedisTemplate时，它的泛型默认是RedisTemplate<Object,Object>
，当把数据写到redis中时，有一个Object序列化成String的过程，底层默认使用的序列化器是JdkSerializationRedisSerializer，这个序列化器在序列化的时候会在字符前面加上如上标识。

**==解决：==**替换默认的序列化器

```java
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        //创建序列化器
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //设置新的序列号器代替原有的JdkSerializationRedisSerializer序列化器
        template.setDefaultSerializer(stringRedisSerializer);
        return template;
    }
}
```
