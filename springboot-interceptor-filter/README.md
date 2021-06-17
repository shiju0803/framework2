#springboot整合Filter过滤器


> 说明：在springboot项目中定义的过滤器，默认是无法执行了。需要开启Servlet组件扫描之后springboot才可以识别到filter过滤器，开始过滤请求和响应。

### 1 定义过滤器

```java
@WebFilter("/*")
public class MyFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {}
    public void destroy() {}
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        System.out.println("MyFilter过滤器执行了...");
        chain.doFilter(request, response);
    }
}
```

### 2 在启动类上开启Servlet组件扫描

```java
@SpringBootApplication
//@WebFilter这个注解是Servlet3.0的规范，并不是Spring boot提供的,需要进行扫包
@ServletComponentScan("com.itheima.filter") //关键点
public class MainApp {
    public static void main(String[] args) {
        SpringApplication.run(MainApp.class,args);
    }
}
```

> **@ServletComponentScan("com.itheima.filter") ：表示开启Servlet的组件扫描。**


#springboot整合拦截器

拦截器：拦截controller中的方法调用，对目标方法进行前置、后置增强操作。

### 1 定义拦截器

```java
public class MyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        //能打印就表示拦截器执行了
        System.out.println("UserInterceptor拦截器的preHandle方法执行了..."+request.getRequestURI());
        //放行
        return true;
    }
}
```

### 2 配置拦截器

> 建议定义到config包中，并且保证引导类在config包外。

```java
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //配置自定义拦截器
        registry.addInterceptor(new MyInterceptor())
            .addPathPatterns("/user/**") //配置拦截路径，可变参数
            .excludePathPatterns("/user/findAll"); //配置排除拦截的路径，可变参数
    }
}
```

