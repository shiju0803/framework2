#springboot整合dubbo

**Apache Dubbo™** 是一款高性能Java RPC框架。 
Spring Boot应用场景的开发。同时也整合了 Spring Boot 特性

- **自动装配** (比如： 注解驱动, 自动装配等).

- **Production-Ready** (比如： 安全, 健康检查, 外部化配置等)

> Apache Dubbo |ˈdʌbəʊ| 是一款高性能、轻量级的开源Java RPC框架，它提供了三大核心能力：面向接口的远程方法调用，智能容错和负载均衡，以及服务自动注册和发现。
>
> 参考文档：https://github.com/apache/dubbo-spring-boot-project/blob/master/README_CN.md
>
> **本次讲解采用最新的 2.7.8 版本的dubbo，相对 2.7.0 版本的dubbo注解略微有些变化。**

### 1 环境搭建

使用的依赖

```xml
<!--dubbo起步依赖-->
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
    <version>2.7.8</version>
</dependency>
<!--zookeeper的curator客户端依赖-->
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-recipes</artifactId>
    <version>4.0.0</version>
</dependency>
```

> 具体环境见给的资料

### 2 provider配置

#### application.yml配置

```yaml
server:
  port: 81 # 应用的tomcat端口号

# dubbo相关配置
dubbo:
  application:
    name: dubbo-provider #应用的名称
  registry:
    address: zookeeper://192.168.66.128:2181 #注册中心地址
  protocol:
    port: 20880 # 服务在注册中心内部占用的端口号
    name: dubbo # 采用dubbo协议，只能是这个名字
  scan:
    base-packages: com.itheima.service #要发布服务类所在的包
```

#### 发布服务

```java
@DubboService //发布服务，低版dubbo本使用alibaba包下的@Service注解
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {
        return "hello "+name;
    }
}
```

### 3 consumer配置

#### application.yml配置

```yaml
server:
  port: 80 # 应用的tomcat端口号

dubbo:
  application:
    name: dubbo-provider #应用的名称
  registry:
    address: zookeeper://192.168.66.128:2181 #注册中心地址
  scan:
    base-packages: com.itheima.controller
```

#### 订阅服务

```java
@RestController
public class HelloController {

    @DubboReference //表示从注册中心中订阅服务，低版dubbo本使用alibaba包下的@Reference注解
    private HelloService helloService;

    @GetMapping(value = "/sayHello",produces = "text/html;charset=utf-8")
    public String sayHello(@RequestParam(defaultValue = "snake") String name){
        return helloService.sayHello(name);
    }
}
```



