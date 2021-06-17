### spring配置文件相关语法和获取配置文件中的数据

# SpringBoot配置文件

## 配置文件分类

SpringBoot是基于约定的，所以很多配置都有默认值，但如果想使用自己的配置替换默认配置的话，就可以使用application.properties或者application.yml（application.yaml）进行配置。

1. 默认配置文件名称：application

2. 在同一级目录下优先级为：properties>yml > yaml

例如：配置内置Tomcat的端口

properties：

```properties
server.port=8080
```

yml:

```yml
server: 
 port: 8080
```

## yaml基本语法【重要】

- 大小写敏感
- 数据值前边必须有空格，作为分隔符
- 使用缩进表示层级关系
- 缩进时不允许使用Tab键，只允许使用空格（各个系统 Tab对应的 空格数目可能不同，导致层次混乱）。
- 缩进的空格数目不重要，只要相同层级的元素左侧对齐即可
- ''#" 表示注释，从这个字符一直到行尾，都会被解析器忽略。

```yaml
server: 
  port: 8080  
  name: abc
```

## yaml数据格式

**对象(map)**：键值对的集合。

```yaml
person:   #比较常用
   name: zhangsan
   age: 20

# 行内写法
person2: {name: zhangsan,age:20}
```

**数组**：一组按次序排列的值

```yaml
address:
  - beijing
  - shanghai
# 行内写法
address2: [beijing,shanghai]
```

**纯量**：单个的、不可再分的值

```yaml
msg1: 'hello \n world'  # 单引忽略转义字符
msg2: "hello \n world"  # 双引识别转义字符
```

**参数引用**

```yaml
person:
  name: ${name} # 引用上边定义的name值
```

## 获取数据

### @Value【了解】

```java
//获取普通配置
@Value("${name}")
private String name;
//获取对象属性
@Value("${person.name}")
private String name2;
//获取数组
@Value("${address[0]}")
private String address1;
//获取纯量
@Value("${msg1}")
private String msg1;
```

注意：@Value注解只能注入普通类型的数据，所以对象和数组中的数据要一个个取出来注入。

应用场景：将来是写在配置类中。

### Evironment【了解】

```java
@Autowired
private Environment env;

System.out.println(env.getProperty("person.name"));

System.out.println(env.getProperty("address[0]"));
```

### @ConfigurationProperties 【重要】

作用：将配置参数封装到指定的bean对象中。

**注意**：prefix一定要写，否则不是获取person下的属性值。此处需要使用@Component将bean添加到spring容器中，封装配置参数的bean将来会运用在配置类中。

```java
@Component
@ConfigurationProperties(prefix = "person")
public class Person {
    private String name;
    private int age;
    private String[] address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String[] getAddress() {
        return address;
    }

    public void setAddress(String[] address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address=" + Arrays.toString(address) +
                '}';
    }
}
```

**出现的问题：**

配置文件上方提示错误

**解决：将如下依赖坐标添加到pom.xml中，刷新依赖即可消除提示。**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```

## profile多配置切换

1. **profile是用来完成不同环境下，配置动态切换功能的**。

2. **profile配置方式**

   2.1 多profile文件方式：提供多个配置文件，每个代表一种环境。

        application-dev.properties/yml 开发环境
    
        application-test.properties/yml 测试环境
    
        application-pro.properties/yml 生产环境

   2.2 yml多文档方式：

        在yml中使用  --- 分隔不同配置

3. **profile激活方式**

   **配置文件： 再配置文件中配置：`spring.profiles.active=dev`**

   虚拟机参数：在VM options 指定：`-Dspring.profiles.active=dev`

   命令行参数：`java –jar xxx.jar --spring.profiles.active=dev`

## 项目内部配置文件加载顺序

加载顺序为上文的排列顺序，高优先级配置的属性会生效

- file:./config/：当前项目下的/config目录下
- file:./ ：当前项目的根目录
- classpath:/config/：classpath的/config目录
- classpath:/ ：classpath的根目录

## 项目外部配置加载顺序

外部配置文件的使用是为了对能不文件的配合

1.命令行

```cmd
java -jar springboot_01_config.jar --server.port=9000
```

2.指定配置文件位置

```cmd
 java -jar springboot_01_config.jar --spring.config.location=e://application.properties
```

3.外部不带profile的properties文件

```properties
classpath=/config/application.properties
classpath=/application.properties
```

https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config

