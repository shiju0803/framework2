##  SpringBoot整合Junit

1. 搭建SpringBoot工程

2. 引入starter-test起步依赖


```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
  </dependency>
</dependencies>
```

3. 编写测试类

```java
/**
 * 测试类
 */

@RunWith(SpringRunner.class) //低版本使用该注解，高版本不需要
@SpringBootTest(classes = SpringbootJunitApplication.class )
public class UserServiceTest {

  @Test
  public void test(){
    System.out.println("test1...");
  }
}
```

4.测试

> 说明：如果springboot从2.2.x版本开始，将Junit4升级成Junit5了，导包是`import org.junit.jupiter.api.Test;`，使用方式更简单，具体如下：

```java
@SpringBootTest
public class AppTest {

    @Test
    void test1(){
        System.out.println("test1...");
    }
}
```

## 总结
    springboot整合junit
    【前提】：如果没有引导类，就需要创建引导类。
    【第一步】：添加junit起步依赖
    <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    </dependency>
    【第二步】：在测试类上使用@SpringBootTest表示该类是单元测试类,在测试方法上使用@Test注解
    注意：单元测试类要在引导所在包及其子包下，否则需要在@SpringBootTest注解中指定引导。
    @SpringBootTest
    public class RedisTest {}