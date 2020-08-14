# <img src="src/docs/asciidoc/images/spring-framework.png" width="80" height="80"> Spring Framework [![Build Status](https://build.spring.io/plugins/servlet/wittified/build-status/SPR-PUBM)](https://build.spring.io/browse/SPR)

# Spring IOC源码

## BeanDefinition

- org.springframework.beans.factory.config.BeanDefinition的定义
  - 在JVM中使用java.lang.Class对象来描述对象。
  - 在spring中使用`org.springframework.beans.factory.config.BeanDefinition`来描述Bean的配置信息
  - 常用属性
    - `@Scope`作用范围
    - `@Lzy`决定Bean实例是否延迟加载
    - `Primary`：设置为true的bean会是优先的实现类
    - 工厂方法提现：factory-bean（工厂bean名称）和factory-method（工厂方法名称）（@Configuration和@Bean）
  - spring中有大量的接口，如果某个类实现了接口，就说明这个接口有某种能力。
  - 源码架构图![BeanDefinition](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh55gars05j30k70b2gly.jpg)
    - `org.springframework.beans.factory.support.RootBeanDefinition`：用来合并具有继承的BeanDefination
    - `org.springframework.core.AttributeAccessor`：提供访问和修改元数据信息的能力
    - `org.springframework.beans.factory.support.GenericBeanDefinition`：在spring 2.5之后，是RootBeanDefinition和ChildBeanDefinotion的替换，用于bean的配置文件类

## BeanFactory

- `org.springframework.beans.factory.BeanFactory`的定义
  - 要求所有的IOC容器都要实现该接口
  - `org.springframework.beans.factory.BeanFactory`和`org.springframework.beans.factory.FactoryBean`的区别
    - BeanFactory是顶级的容器接口，定义了IOC容器最基本的功能，是管理Bean的容器,等价工厂方法的抽象工厂方法
    - FactoryBean基于getObject()可以使用一套更加复杂的逻辑生成bean，用来生成bean，等价于在工厂方法的具体工厂方法。
  - 架构图![BeanFactory](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh56do89h0j30kh0dwjrr.jpg)
    - 那么多接口是因为架构的设计遵循设计模式单一职责原则
    - `AutowireCapableBeanFactory`：组件扫描和组件装配
    - 

## ApplicationContext（上下文）

- spring的IOC容器分为2类：
  - `org.springframework.beans.factory.BeanFactory`为主的简单IOC容器，已`org.springframework.context.ApplicationContext`为高级的IOC容器
  - ![ApplicationContext](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh56psnr0jj317o0p8q4k.jpg)
  - BeanFactory是spring IOC容器的基本设施（面向spring自身），与用户打交道的ApplicationContext的高级IOC容器，除了创建bean之外很多额外的功能
- ApplicationContext常用容器
  - 传统的基于XML配置的经典容器
    - org.springframework.context.support.FileSystemXmlApplicationContext：从文件系统加载配置
    - org.springframework.context.support.ClassPathXmlApplicationContext：从classpath加载配置
    - org.springframework.web.context.support.XmlWebApplicationContext：用于web应用程序的容器
  - 目前比较流行的容器
    - AnnotationConfigServletWebServerApplicationContext（springboot）
    - AnnotationConfigReactiveWebServerApplicationContext（springboot响应式编程）
    - AnnotationConfigApplicationContext（spring，基于注解的容器）

## DefaultListableBeanFactory

## Resource

- org.springframework.core.io.Resource
- ![Resource](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh6bn5xgxnj30ks0b2glw.jpg)
- ResourceLoader：根据资源自动地址自动选择正确的Resource
  - 自动识别"classpath:"、'file:" 等资源地址前缀
  - 支持自动解析Ant风格带通配符的资源地址（路径匹配表达式，用来对URI进行匹配）

## ResourceLoad

- ![ResourceLoader](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh6c06qj7wj30m30dwt95.jpg)
- org.springframework.core.io.DefaultResourceLoader#getResource
  - 大部分Resource的实现逻辑：根据不同的路径返回不同的resouce实例
  - 非简单工厂模式（黑盒，用户完全不了解内部），而是策略模式

## BeanDefinitionReader

- org.springframework.beans.factory.support.BeanDefinitionReader：使用ResourceLoad读取配置来转换为BeanDefinition。
- ![BeanDefinitionReader](https://tva1.sinaimg.cn/large/007S8ZIlgy1gh6chyp3t1j30gr0883yn.jpg)
- org.springframework.beans.factory.support.AbstractBeanDefinitionReader#loadBeanDefinitions(java.lang.String, java.util.Set<org.springframework.core.io.Resource>)：核心逻辑
- 问：BeanDefinitionDocumentReader documentReader替换为（DefaultBeanDefinitionDocumentReader）：错误！！！！！DefaultListableBeanFactory通过实现了BeanDefinitionRegistry，实现`Map<String, BeanDefinition> beanDefinitionMap `作为BeanDefinition存储的载体。在分析XmlBeanDefinitionReader的时候，DefaultListableBeanFactory通过献祭自己转换为BeanDefinitionRegistry给XmlBeanDefinitionReader使用

## DefaultBeanDefinitionDocumentReader

- org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader#doRegisterBeanDefinitions
- 事情监听：xml中的import标签和alias标签完成后都发送事件
- org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader#processBeanDefinition，把创建好的BeanDefinition注册到容器BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());

## BeanDefinitionRegistry

## AbstractBeanDefinitionReader

```java
protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
		Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
		this.registry = registry;

		// Determine ResourceLoader to use.
		if (this.registry instanceof ResourceLoader) {
			this.resourceLoader = (ResourceLoader) this.registry;
		}
		else {
			this.resourceLoader = new PathMatchingResourcePatternResolver();
		}

		// Inherit Environment if possible
		if (this.registry instanceof EnvironmentCapable) {
			this.environment = ((EnvironmentCapable) this.registry).getEnvironment();
		}
		else {
			this.environment = new StandardEnvironment();
		}
	}
```

- 细节：也就是说可以把IOC容器（DefaultListableBeanFactory和ApplicationContext）当成ResourceLoad来使用

## PostProcessor

- postProcessor作为容器或者bean的后置处理器
- 本身也是一种需要注册到容器的bean
  - 其里面的方法会在特定的时机被容器调用
  - 实现不改变容器或者Bean核心逻辑的情况下对Bean进行扩展.
  - 对bean进行包装，影响其行为、修改bean的内容等
- 种类：大类分为容器级别的后置处理器以及Bean级别的后置处理器
  - BeanDefinitionRegistryPostProcessor（容器级别）
  - BeanFactoryPostProcessor（容器级别）
  - BeanPostProcessor（bean级别）
- org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor：
  - 允许在正常的BeanFactoryPostProcessor检测开始之前注册更多的自定义beandefinition
- org.springframework.beans.factory.config.BeanFactoryPostProcessor：
- org.springframework.beans.factory.config.BeanPostProcessor：
- 责任链模式：为什么能能够在容器内调用postprocessor方法？
  - 排序好的postprocessor按照责任链模式来执行 

## Aware(可感知)

-  从Bean里面获取到的容器实例并对其进行操作
-  需要获取容器能力的实现，实现特定的Aware接口，就能得到该能力。在创建该bean的时候，将能力赋值给bean。
-  本身也必须是bean

## AbstractApplicationContext#refresh（模板方法）

> prepareRefresh();刷新前的工作准备
>
> obtainFreshBeanFactory();获取子类刷新后的内部beanFactory实例，对于xml这里对beanDefinition进行解释了注册，注解比较简单
>
> prepareBeanFactory(beanFactory);为容器注册必要的系统级别的Bean
>
> postProcessBeanFactory(beanFactory);允许容器的子类去注册postProcessor
>
> invokeBeanFactoryPostProcessors(beanFactory);调用容器注册的容器级别的后置处理器（而注解的方式，是在容器后置处理器警醒解析和注册的）
>
> registerBeanPostProcessors(beanFactory);向容器注册bean级别的后置处理器
>
> initMessageSource();初始化国际化配置
>
> initApplicationEventMulticaster();初始化事件发布者组件
>
> onRefresh();在单例bean初始化之前预留给子类初始化其他特殊bean的口子
>
> registerListeners();向前面的事件发布者组件注册事件监听器
>
> finishBeanFactoryInitialization(beanFactory);设置系统级别的服务，实例化所有非懒加载的单例(依赖注入)
>
> finishRefresh();触发初始化完成的回调方法，并发布容器刷新完成的事件给监听者
>
> resetCommonCaches();重置spring内核中的共用缓存

## 依赖注入

![spring依赖注入](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghq3ngxn4tj31070i3jud.jpg)

- AbstractBeanFactory
  - doGetBean
    - 尝试从缓存获取bean（bean实例或者factoryBean.getObject()方法）
    - 循环依赖的判断
    - 递归去父容器获取bean实例
    - 从当前容器获取beanDefinition实例
    - 递归实例化显示依赖的bean（depengs-on，后者会先实例化）
    - 依据不同的scope采用不同的策略创建bean实例
    - 对bean进行类型检查
- DefaultSingletonBeanRegistry
- AbstractAutowireCapableBeanFactory
  - createBean
    - bean类型解析
    - 处理方法覆盖(lookup-method和replace-method等替换bean属性)
    - bean实例化前的后置处理
    - doCreateBean
  - doCreateBean
    - 创建bean实例（工厂方法，含参构造器注入、无参数构造器注入）
    - 记录下被@Autowired或者@value标记上的方法和成员变量（通过MergedBeanDefinitionPostProcessor的后置处理器，将其放到InjectionMetadata中，为后面依赖注入做准备，还是责任链模式）
    - 是否允许提前暴露（bean是单例&&允许循环依赖&&正在创建的）
    - 填充bean属性
    - initializabean（beanpostProcessor）
      - 如果实现了aware接口，则设置值
      - Aop：初始化前操作
      - Aop：初始化操作
      - Aop：初始化后操作
    - 注册相关销毁逻辑 
    - 返回创建好的实例
  - AbstractAutowireCapableBeanFactory#populateBean（属性填充）
    - postProcessAfterInitialization:在设置属性前去修改bean状态，也可以控制是否继续给bean设置属性值
    - 注入属性到propertyValue中（按名字装配 or 按类型装配）
    - postProcessPropertyValue:对解析完但是未设置的属性进行再处理
    - 是否进行依赖检查
    - 将propertyValue中的属性值设置到beanwrapper中
- AutowiredAnnotationBeanPostProcessor
- DefaultListableBeanFactory
- DependencyDescriptor

## spring循环依赖

- 针对prototype的循环依赖，spring无解，直接抛出异常
- spring不支持显示循环依赖，(depends-on)
- 静态的file不支持依赖注入,代码报错
- 要想框架设计的好（强大和灵活），就要把功能拆拆的够细，每个过程都留有可操作的空间，使用责任链模式完美
- 一图胜前文![spring解决单例循环依赖](https://tva1.sinaimg.cn/large/007S8ZIlgy1ghgqr7obudj310e0i245s.jpg)
- 循环依赖的情况
  - 构造器循环依赖（Singleton、prototype）
  - Setter注入循环依赖（Singleton、prototype）
  - 总结：构造器或者prototype都不支持循环依赖
- 三级缓存解决的问题：
  1. 解决了单例循环依赖问题
  2. 单例的唯一性问题
  3. prototype无三级缓存（故不支持）
  4. AbstractAutowireCapableBeanFactory#createBeanInstance（构造器依赖注入，不支持循环依赖的问题，因为这里还没有使用到缓存）
  5. 总结：只支持setter注入的单例循环依赖

## BeanWrapperImpl

- 功能:修改bean里面的属性

## Spring IOC源码分析

- 抓住主干：
  - 解析配置
  - 定位与注册对象
    - 容器初始化主要做的事情：配置文件读取，Resource解析，BeanDefinition注册到容器
  - 注入对象
- xml（注解）配置的资源定位、加载、解析、注册全链路分析

# Spring AOP源码

