## SpringBoot整合mybatis

### 1 搭建SpringBoot工程

### 2 引入mybatis起步依赖，添加mysql驱动

```xml
<dependencies>
    <!--单元测试起步依赖,这个是spring官方提供-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
    </dependency>

    <!--springboot整合mybatis的起步依赖，这个是mybatis做-->
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>2.2.0</version>
    </dependency>

    <!--mysql驱动-->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
</dependencies>
```

### 3 定义表和实体类

```java
public class User {
    private int id;
    private String username;
    private String password;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
```

### 4 编写dao和mapper文件/纯注解开发

编写dao

```java
@Mapper
@Repository  //该注解可以不加，加主要是为了解决idea的语法检查报错问题
public interface UserXmlMapper {
    List<User> findAll();
}
```

mapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.itheima.springbootmybatis.mapper.UserXmlMapper">
    <select id="findAll" resultType="user">
        select * from t_user
    </select>
</mapper>
```

纯注解开发

```java

@Mapper  //表示该接口是一个mapper接口，将来由mybatis创建代理对象保存到spring容器中
@Repository //仅仅用来解决单元测试类中UserMapper语法检查报错问题的，没有实际作用
public interface UserMapper {

    @Select("select * from t_user")
    List<User> findAll();
}
```

### 5 在application.yml编写DataSource和MyBatis相关配置

```yaml
# datasource
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql:///springboot?serverTimezone=UTC
    username: root
    password: root

# mybatis
mybatis:
  mapper-locations: classpath:mapper/UserMapper.xml # 如果映射文件和mapper接口不在同一个包下，就需要配置mapper映射文件路径
  type-aliases-package: com.itheima.domain

  # config-location:  # 指定mybatis的核心配置文件
```

### 6 测试

```java
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testFindAll(){
        List<User> list = userMapper.findAll();
        list.forEach(user -> System.out.println(user));
    }
}
```

### 7 补充：整合分页插件启动依赖

#### pom.xml配置

```xml
<!--springboot整合mybatis分页插件启动依赖-->
<dependency>
  <groupId>com.github.pagehelper</groupId>
  <artifactId>pagehelper-spring-boot-starter</artifactId>
  <version>1.2.3</version>
</dependency>
```

#### 单元测试

```java
    @Test
    public void testFindByPage(){
      //设置分页参数
      PageHelper.startPage(1,2);
      //查询当前页所有数据
      List<User> list = userMapper.findAll();
      //封装分页结果
      PageInfo<User> pageInfo = new PageInfo<>(list);
      //打印结果
      System.out.println(pageInfo);
    }
 ```