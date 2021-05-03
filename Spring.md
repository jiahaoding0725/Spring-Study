### 1. Spring

#### 1.1 简介

* spring理念：使现有的技术更加容易使用，本身是一个大杂烩。整合了现有的技术框架





- SSH：Struct2 + Spring + Hibernate
- SSM: SpringMVC + Spring + Mybatis



官网：https://spring.io/projects/spring-framework#overview

官方下载：https://repo.spring.io/release/org/springframework/spring/

GIthub：https://github.com/spring-projects/spring-framework



```xml
<!-- https://mvnrepository.com/artifact/org.springframework/spring-webmvc -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>5.3.6</version>
</dependency>

<!-- https://mvnrepository.com/artifact/org.springframework/spring-jdbc -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>5.3.6</version>
</dependency>

```





#### 1.2 优点

* Spring是一个开源的免费的框架（容器）
* spring是一个轻量级的，非入侵式的
* 控制反转（IOC），面向切面编程 (AOP)。
* 支持事务处理，对框架整合的支持。



==总结：spring是一个轻量级的控制反转(IOC)和面向切面编程(AOP)的框架。==





#### 1.3 组成

![image-20210428151503863](/Users/johnny/Library/Application Support/typora-user-images/image-20210428151503863.png)





#### 1.4 拓展

现代化的Java开发就是Spring的开发

* Spring Boot
  * 一个快速开发的脚手架
  * 基于SpringBoot可以快速的开发单个微服务
  * 约定大于配置
* Spring Cloud
  * SpringCloud 是基于SpringBoot实现的



弊端：发展了太久之后，违背了原来的理念！配置十分繁琐，人称：”配置地狱“



### 2. IOC理论推导(spring_01_ioc1)

1. 我们先用我们原来的方式写一段代码 .

   1、先写一个UserDao接口

   

   ```java
   public interface UserDao {
      public void getUser();
   }
   ```

   2、再去写Dao的实现类

   

   ```java
   public class UserDaoImpl implements UserDao {
      @Override
      public void getUser() {
          System.out.println("获取用户数据");
     }
   }
   ```

   3、然后去写UserService的接口

   

   ```java
   public interface UserService {
      public void getUser();
   }
   ```

   4、最后写Service的实现类

   

   ```java
   public class UserServiceImpl implements UserService {
      private UserDao userDao = new UserDaoImpl();
   
      @Override
      public void getUser() {
          userDao.getUser();
     }
   }
   ```

   5、测试一下`(这里每次更换不同接口都需要在service层更改new的接口)`

   

   ```java
   @Test
   public void test(){
      UserService service = new UserServiceImpl();
      service.getUser();
   }
   ```

   这是我们原来的方式 , 开始大家也都是这么去写的对吧 . 那我们现在修改一下 .

   把Userdao的实现类增加一个 .

   

   ```java
   public class UserDaoMySqlImpl implements UserDao {
      @Override
      public void getUser() {
          System.out.println("MySql获取用户数据");
     }
   }
   ```

   紧接着我们要去使用MySql的话 , 我们就需要去service实现类里面修改对应的实现

   

   ```java
   public class UserServiceImpl implements UserService {
      private UserDao userDao = new UserDaoMySqlImpl();
   
      @Override
      public void getUser() {
          userDao.getUser();
     }
   }
   ```

   在假设, 我们再增加一个Userdao的实现类 .

   

   ```java
   public class UserDaoOracleImpl implements UserDao {
      @Override
      public void getUser() {
          System.out.println("Oracle获取用户数据");
     }
   }
   ```

   那么我们要使用Oracle , 又需要去service实现类里面修改对应的实现 . 假设我们的这种需求非常大 , 这种方式就根本不适用了, 甚至反人类对吧 , 每次变动 , 都需要修改大量代码 . 这种设计的耦合性太高了, 牵一发而动全身 .

   **那我们如何去解决呢 ?**

我们可以在需要用到他的地方 , 不去实现它 , 而是留出一个接口 , 利用set , 我们去代码里修改下。

```java
public class UserServiceImpl implements UserService {
   private UserDao userDao;
// 利用set实现
   public void setUserDao(UserDao userDao) {
       this.userDao = userDao;
  }

   @Override
   public void getUser() {
       userDao.getUser();
  }
}
```

现在去我们的测试类里 , 进行测试 ;`（有了set方法就可以在调用的时候由用户选择调用的接口）`

```java
@Test
public void test(){
   UserServiceImpl service = new UserServiceImpl();
   service.setUserDao( new UserDaoMySqlImpl() );
   service.getUser();
   //那我们现在又想用Oracle去实现呢
   service.setUserDao( new UserDaoOracleImpl() );
   service.getUser();
}
```

大家发现了区别没有 ? 可能很多人说没啥区别 . 但是同学们 , 他们已经发生了根本性的变化 , 很多地方都不一样了 . 仔细去思考一下 , **以前所有东西都是由程序去进行控制创建** , **而现在是由我们自行控制创建对象 , 把主动权交给了调用者** . 程序不用去管怎么创建,怎么实现了 . 它只负责提供一个接口 .

这种思想 , 从本质上解决了问题 , 我们程序员不再去管理对象的创建了 , 更多的去关注业务的实现 . 耦合性大大降低 . 这也就是IOC的原型 !



![image-20210428155520036](/Users/johnny/Library/Application Support/typora-user-images/image-20210428155520036.png)





#### 2.1 IOC本质

**控制反转IoC(Inversion of Control)，是一种设计思想，DI(依赖注入)是实现IoC的一种方法**，也有人认为DI只是IoC的另一种说法。没有IoC的程序中 , 我们使用面向对象编程 , 对象的创建与对象间的依赖关系完全硬编码在程序中，对象的创建由程序自己控制，控制反转后将对象的创建转移给第三方，个人认为所谓控制反转就是：获得依赖对象的方式反转了。

![image-20210428155634641](/Users/johnny/Library/Application Support/typora-user-images/image-20210428155634641.png)



**IoC是Spring框架的核心内容**，使用多种方式完美的实现了IoC，可以使用XML配置，也可以使用注解，新版本的Spring也可以零配置实现IoC。

Spring容器在初始化时先读取配置文件，根据配置文件或元数据创建与组织对象存入容器中，程序使用时再从Ioc容器中取出需要的对象。

![image-20210428155835354](/Users/johnny/Library/Application Support/typora-user-images/image-20210428155835354.png)





采用XML方式配置Bean的时候，Bean的定义信息是和实现分离的，而采用注解的方式可以把两者合为一体，Bean的定义信息直接以注解的形式定义在实现类中，从而达到了零配置的目的。

**控制反转是一种通过描述（XML或注解）并通过第三方去生产或获取特定对象的方式。在Spring中实现控制反转的是IoC容器，其实现方法是依赖注入（Dependency Injection,DI）。**





### 3. HelloSpring (Spring_02_hellospring)

#### 3.1  导入jar包

#### 3.2 编写代码

1、编写一个Hello实体类

```java
public class Hello {
   private String name;

   public String getName() {
       return name;
  }
   public void setName(String name) {
       this.name = name;
  }

   public void show(){
       System.out.println("Hello,"+ name );
  }
}
```

2、编写我们的spring文件 , 这里我们命名为beans.xml

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">

   <!--bean就是java对象 , 由Spring创建和管理-->
   <bean id="hello" class="com.ding.pojo.Hello">
       <property name="name" value="Spring"/>
   </bean>

</beans>
```

3、我们可以去进行测试了 .

```java
@Test
public void test(){
   //解析beans.xml文件 , 生成管理相应的Bean对象
   ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
   //getBean : 参数即为spring配置文件中bean的id .
   Hello hello = (Hello) context.getBean("hello");
   hello.show();
}
```



#### 3.3 思考

- Hello 对象是谁创建的 ? 【hello 对象是由Spring创建的
- Hello 对象的属性是怎么设置的 ? hello 对象的属性是由Spring容器设置的

这个过程就叫控制反转 :

- 控制 : 谁来控制对象的创建 , 传统应用程序的对象是由程序本身控制创建的 , 使用Spring后 , 对象是由Spring来创建的
- 反转 : 程序本身不创建对象 , 而变成被动的接收对象 .

依赖注入 : 就是利用set方法来进行注入的.

IOC是一种编程思想，由主动的编程变成被动的接收

可以通过newClassPathXmlApplicationContext去浏览一下底层源码 .

**我们彻底不用在程序中改动了，要实现不同的操作，只需要在xml配置文件中进行修改，所谓的IoC，一句话：对象由Spring来创建，管理，装配**





#### 3.4 修改案例一

我们在案例一中， 新增一个Spring配置文件ApplicationContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id = "mysqlImpl" class="com.ding.dao.UserDaoMySqlImpl"></bean>
    <bean id = "oracleImpl" class="com.ding.dao.UserDaoOracleImpl"></bean>
    <bean id = "userServiceImpl" class="com.ding.service.UserServiceImpl">
        <!--
        ref：引用Spring容器中创建好的对象
        value: 具体的值，基本数据类型
        -->
        <property name="userDao" ref="mysqlImpl"></property>
    </bean>

</beans>
```



测试

```java
public class MyTest {
    public static void main(String[] args) {
        // 获取ApplicaitonContext
        ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        UserServiceImpl userServiceImpl = (UserServiceImpl) context.getBean("userServiceImpl");
        userServiceImpl.getUser();
    }
}
```





### 4. IOC创建对象的方式（spring_03_ioc2）

#### 4.1 使用无参构造方法来创建对象 

1、User.java

```java
public class User {

   private String name;

   public User() {
       System.out.println("user无参构造方法");
  }

   public void setName(String name) {
       this.name = name;
  }

   public void show(){
       System.out.println("name = "+ name );
  }

}
```

2、ApplicationContext.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">

   <bean id="user" class="com.ding.pojo.User">
       <property name="name" value="丁家豪"/>
   </bean>

</beans>
```

3、测试类

```java
public class MyTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        User user = (User) context.getBean("user");
        user.show();
    }
}
```

结果可以发现，在调用show方法之前，User对象已经通过无参构造初始化了！



#### 4.2 通过有参构造方法来创建 （spring_03_ioc2）

1、User . java

```java
public class User {

   private String name;

   public User(String name) {
       this.name = name;
  }

   public void setName(String name) {
       this.name = name;
  }

   public void show(){
       System.out.println("name ="+ name );
  }
}
```

2、ApplicationContext.xml 有三种方式编写

```xml
<!-- 第一种根据index参数下标设置 -->
<bean id="user" class="com.ding.pojo.User">
   <!-- index指构造方法 , 下标从0开始 -->
   <constructor-arg index="0" value="丁家豪123"/>
</bean>
<!-- 第二种根据参数名字设置 -->
<bean id="user" class="com.ding.pojo.User">
   <!-- name指参数名 -->
   <constructor-arg name="name" value="丁家豪123"/>
</bean>
<!-- 第三种根据参数类型设置(不推荐使用) -->
<bean id="user" class="com.ding.pojo.User">
   <constructor-arg type="java.lang.String" value="丁家豪123"/>
</bean>
```

3、测试

```java
@Test
public void testT(){
   ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
   UserT user = (User) context.getBean("user");
   user.show();
}
```

结论：在配置文件加载的时候。其中管理的对象都已经初始化了！







### 5. Spring配置

#### 5.1 别名

alias 设置别名 , 为bean设置别名 , 可以设置多个别名

```Xml
<!--设置别名：在获取Bean的时候可以使用别名获取-->
<alias name="userT" alias="userNew"/>
```

#### 5.2 Bean的配置

```xml
<!--bean就是java对象,由Spring创建和管理-->

<!--
   id 是bean的标识符,要唯一,如果没有配置id,name就是默认标识符
   如果配置id,又配置了name,那么name是别名
   name可以设置多个别名,可以用逗号,分号,空格隔开
   如果不配置id和name,可以根据applicationContext.getBean(.class)获取对象;

class是bean的全限定名=包名+类名
-->
<bean id="userT" name="user2 u2,u3;u4" class="com.kuang.ding.userT">
   <property name="name" value="Spring"/>
</bean>
```

#### 5.3 import

团队的合作通过import来实现 ，可以将多个配置文件，导入合并为一个配置文件

```xml
<import resource="ApplicationContextTmp.xml"/>
```





### 6. DI 依赖注入 (spring_04_di)

#### 6.1 构造器注入

已经说过

#### 6.2 setter方式注入

* 依赖注入：Set注入！
  * 依赖：bean对象的创建依赖于容器
  * 注入：bean对象中的所有属性，由容器来注入



环境搭建



```java
public class Student {
        private String name;
        private Address address;
        private String[] books;
        private List<String> hobbies;
        private Map<String, String> card;
        private Set<String> games;
        private Properties info;
        private String wife;
}


```

```java
public class Address {
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
```





注意点：这里的值是一个引用，ref

```xml
 <bean id="addr" class="com.ding.pojo.Address">
     <property name="address" value="重庆"/>
 </bean>
 
 <bean id="student" class="com.ding.pojo.Student">
     <property name="name" value="小明"/>
     <property name="address" ref="addr"/>
 </bean>
```

3、**数组注入**

```xml
 <bean id="student" class="com.ding.pojo.Student">
     <property name="name" value="小明"/>
     <property name="address" ref="addr"/>
     <property name="books">
         <array>
             <value>西游记</value>
             <value>红楼梦</value>
             <value>水浒传</value>
         </array>
     </property>
 </bean>
```

4、**List注入**

```xml
 <property name="hobbys">
     <list>
         <value>听歌</value>
         <value>看电影</value>
         <value>爬山</value>
     </list>
 </property>
```

5、**Map注入**

```xml
 <property name="card">
     <map>
         <entry key="中国邮政" value="456456456465456"/>
         <entry key="建设" value="1456682255511"/>
     </map>
 </property>
```

6、**set注入**

```xml
 <property name="games">
     <set>
         <value>LOL</value>
         <value>BOB</value>
         <value>COC</value>
     </set>
 </property>
```

7、**Null注入**

```xml
 <property name="wife"><null/></property>
```

8、**Properties注入**

```xml
 <property name="info">
     <props>
         <prop key="学号">20190604</prop>
         <prop key="性别">男</prop>
         <prop key="姓名">小明</prop>
     </props>
 </property>
```



#### 6.3 其他方式注入

* **p命名空间注入**

User.java ：【注意：这里没有有参构造器！】

```java
 public class User {
     private String name;
     private int age;
 
     public void setName(String name) {
         this.name = name;
    }
 
     public void setAge(int age) {
         this.age = age;
    }
 
     @Override
     public String toString() {
         return "User{" +
                 "name='" + name + '\'' +
                 ", age=" + age +
                 '}';
    }
 }
```

1、P命名空间注入 : 需要在头文件中加入约束文件

```xml
 导入约束 : xmlns:p="http://www.springframework.org/schema/p"
 
 <!--P(属性: properties)命名空间 , 属性依然要设置set方法-->
<bean id="user" class="com.ding.pojo.User" p:name="丁家豪" p:age="18"/>

```





* **c命名空间注入**

c 命名空间注入 : 需要在头文件中加入约束文件

```java
public class User {
    private String name;
    private int age;

    public User() {
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

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

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
```



```xml
 导入约束 : xmlns:c="http://www.springframework.org/schema/c"
 <!--C(构造: Constructor)命名空间 , 属性依然要设置set方法-->
<bean id="user2" class="com.ding.pojo.User" c:age="18" c:name="丁家豪"/>
```





#### 6.4  bean的作用域

![image-20210430130917352](/Users/johnny/CS/Spring_Study/1. Spring.assets/image-20210430130917352.png)

测试

```java
@Test
public void test3(){
    ApplicationContext context = new ClassPathXmlApplicationContext("userbeans.xml");
    User user1 = context.getBean("user2", User.class);
    User user2 = context.getBean("user2", User.class);
    System.out.println(user1==user2);
}
```





1. 单例模式（singleton）（Spring默认机制）

   ```xml
   <bean id="user2" class="com.ding.pojo.User" c:age="18" c:name="丁家豪" scope="singleton"/>
   ```

2. 原型模式 (prototype)：每次从容器中get的时候都会产生一个新对象

   ```xml
   <bean id="user2" class="com.ding.pojo.User" c:age="18" c:name="丁家豪" scope="prototype"/>
   ```

3. 其余的request、session、application、这些个只能在web开发中使用到。







### 7. 自动装配 (spring_05_Autowired)

* 自动装配是Spring满足bean依赖一种方式。
* Spring会在上下文中自动寻找，并自动给bean装配属性！





在Spring中有三种装配的方式

1. 在xml中显示的配置
2. 在java中显示配置
3. 隐式的自动装配bean





#### 7.1 测试

1. 环境搭建
   * 一个有两个宠物

```java
public class Cat {
    public void bark(){
        System.out.println("miao~");
    }
}
```

```java
public class Dog {
    public void bark(){
        System.out.println("woof~");
    }
}
```

```java
public class People {
    private Cat cat;
    private Dog dog;
    private String name;

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "People{" +
                "cat=" + cat +
                ", dog=" + dog +
                ", name='" + name + '\'' +
                '}';
    }
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="cat" class="com.ding.pojo.Cat"/>
    <bean id="dog" class="com.ding.pojo.Dog"/>
    <bean id="people" class="com.ding.pojo.People">
        <property name="name" value="丁家豪"/>
        <property name="dog" ref="dog"/>
        <property name="cat" ref="cat"/>
    </bean>
</beans>
```

```java
public class MyTest {
    @Test
    public void test1(){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        People people = context.getBean("people", People.class);
        people.getCat().bark();
        people.getDog().bark();
    }
}
```







#### 7.2 ByName自动装配

```xml
 <bean id="cat" class="com.ding.pojo.Cat"/>
 <bean id="dog" class="com.ding.pojo.Dog"/>

 <!--
     byName: 会自动在容器上下文中查找，和自己对象set方法后面的值对应的beanid！
-->
 <bean id="people" class="com.ding.pojo.People" autowire="byName">
     <property name="name" value="丁家豪"/>
 </bean>
```



#### 7.3 ByType自动装配

```xml
 <bean class="com.ding.pojo.Cat"/>
 <bean class="com.ding.pojo.Dog"/>

 <!--
     byName: 会自动在容器上下文中查找，和自己对象set方法后面的值对应的beanid！
     byType: 会自动在容器上下文中查找，和自己对象属性类型相同的bean！
-->
 <bean id="people" class="com.ding.pojo.People" autowire="byType">
     <property name="name" value="丁家豪"/>
 </bean>
```





小结：

* byName的时候，需要保证所有bean的id唯一，并且这个bean需要和自动注入的属性的set方法的值一致！
* byType的时候，需要保证所有bean的class唯一，并且这个bean需要和自动注入的属性的类型一致！



#### 7.4 使用注解实现自动装配

jdk1.5支持的注解，Spring2.5就支持注解。

要使用注解须知：

1. 导入约束 context约束

2. 配置注解的支持 <context:annotation-config/>

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
           https://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/context
           https://www.springframework.org/schema/context/spring-context.xsd">
   
       <context:annotation-config/>
   
   </beans>
   ```

   

**@Autowired**（默认按照类型配置）

直接在属性或者setter上使用，使用Autowired甚至setter方法都不需要，前提是你这个自动装配的属性在IOC容器中存在。

```java
public class People {

    @Autowired
    private Cat cat;
    @Autowired
    private Dog dog;
    private String name;

    public Cat getCat() {
        return cat;
    }

    public Dog getDog() {
        return dog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "People{" +
                "cat=" + cat +
                ", dog=" + dog +
                ", name='" + name + '\'' +
                '}';
    }
}
```

**@Autowired(required = false)**

```java
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    boolean required() default true;
}
```

```java
// 如果显示定义了Autowored的required属性为false，说明这个对象可以为null，否则不允许为空
@Autowired(required = false)
private Cat cat;
```



**@Qualifier**

- @Autowired是根据类型自动装配的，加上@Qualifier则可以根据byName的方式自动装配
- @Qualifier不能单独使用。

```java
@Autowired
@Qualifier(value="dog2")
private Dog dog;
```

```xml
<bean id="dog2" class="com.ding.pojo.Dog"/>
```

**@Nullable**

```xml
@Nullable 字段标记了这个注解，说明这个字段可以为空
```

```java
public People(@Nullable String name) {
    this.name = name;
}
```



**@Resource**

- @Resource如有指定的name属性，先按该属性进行byName方式查找装配；
- 其次再进行默认的byName方式进行装配；
- 如果以上都不成功，则按byType的方式自动装配。
- 都不成功，则报异常。

实体类：

```java
public class People {
   //如果允许对象为null，设置required = false,默认为true
   @Resource(name = "cat2")
   private Cat cat;
   @Resource
   private Dog dog;
   private String name;
}
```

applicationContext.xml

```xml
<bean id="dog" class="com.ding.pojo.Dog"/>
<bean id="cat1" class="com.ding.pojo.Cat"/>
<bean id="cat2" class="com.ding.pojo.Cat"/>

<bean id="people" class="com.ding.pojo.People"/>
```

测试：结果OK

配置文件2：applicationContext.xml ， 删掉cat2

```xml
<bean id="dog" class="com.kuang.pojo.Dog"/>
<bean id="cat1" class="com.kuang.pojo.Cat"/>
```

实体类上只保留注解

```java
@Resource
private Cat cat;
@Resource
private Dog dog;
```

结果：OK

结论：先进行byName查找，失败；再进行byType查找，成功。





**小结**

@Autowired与@Resource异同：

* @Autowired与@Resource都可以用来装配bean。都可以写在字段上，或写在setter方法上。
* @Autowired默认**按类型装配**（属于spring规范），默认情况下必须要求依赖对象必须存在，如果要允许null 值，可以设置它的required属性为false，如：@Autowired(required=false) ，如果我们想使用名称装配可以结合@Qualifier注解进行使用
* @Resource（属于J2EE复返），默认**按照名称进行装配**，名称可以通过name属性进行指定。如果没有指定name属性，当注解写在字段上时，默认取字段名进行按照名称查找，如果注解写在setter方法上默认取属性名进行装配。当找不到与名称匹配的bean时才按照类型进行装配。但是需要注意的是，如果name属性一旦指定，就只会按照名称进行装配。

它们的作用相同都是用注解方式注入对象，但执行顺序不同。@Autowired先byType，@Resource先byName。







### 8. 使用注解开发(spring_06_annotation)

在Spring4之后，要使用注解开发，必须要保证aop的包导入

![image-20210430222056992](/Users/johnny/CS/Spring_Study/1. Spring.assets/image-20210430222056992.png)

使用注解需要导入context约束，增加注解的支持

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xmlns:context="http://www.springframework.org/schema/context"
      xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

</beans>
```



#### 8.1 bean的实现

我们之前都是使用 bean 的标签进行bean注入，但是实际开发中，我们一般都会使用注解！

1、配置扫描哪些包下的注解

```xml
<!--指定注解扫描包-->
<context:component-scan base-package="com.ding.pojo"/>
```

2、在指定包下编写类，增加注解

```java
@Component("user")
// 相当于配置文件中 <bean id="user" class="com.ding.pojo.User"/>
public class User {
   public String name = "丁家豪";
}
```

3、测试

```java
@Test
public void test(){
   ApplicationContext applicationContext =
       new ClassPathXmlApplicationContext("applicationContext.xml");
   User user = (User) applicationContext.getBean("user");
   System.out.println(user.name);
}
```

#### 8.2 属性如何注入

使用注解注入属性

1、可以不用提供set方法，直接在直接名上添加@value("值")

```java
@Component("user")
// 相当于配置文件中 <bean id="user" class="com.ding.pojo.ding"/>
public class User {
   @Value("丁家豪")
   // 相当于配置文件中 <property name="name" value="丁家豪"/>
   public String name;
}
```

2、如果提供了set方法，在set方法上添加@value("值");

```java
@Component("user")
public class User {

   public String name;

   @Value("丁家豪")
   public void setName(String name) {
       this.name = name;
  }
}
```

#### 8.3 衍生的注解

@Component有几个衍生注解，我们在web开发中，会按照mvc三层架构分层

* dao 【@Repository】

* service 【@Service】

* controller 【@Controller】

这四个注解功能一样，都是将某个类注册到Spring中，装配Bean

#### 8.4 自动装配注解



#### 8.5 作用域

@scope

- singleton：默认的，Spring会采用单例模式创建这个对象。关闭工厂 ，所有的对象都会销毁。
- prototype：多例模式。关闭工厂 ，所有的对象不会销毁。内部的垃圾回收机制会回收

```java
@Controller("user")
@Scope("prototype")
public class User {
   @Value("秦疆")
   public String name;
}
```



#### 8.6 小结

**XML与注解比较**

- XML可以适用任何场景 ，结构清晰，维护方便
- 注解不是自己提供的类使用不了，开发简单方便

**xml与注解整合开发** ：推荐最佳实践

- xml管理Bean
- 注解完成属性注入
- 使用过程中， 可以不用扫描，扫描是为了类上的注解

```xml
<context:component-scan base-package="com.ding"/>
<context:annotation-config/>
```

作用：

- 进行注解驱动注册，从而使注解生效
- 用于激活那些已经在spring容器里注册过的bean上面的注解，也就是显示的向Spring注册
- 如果不扫描包，就需要手动配置bean
- 如果不加注解驱动，则注入的值为null！













### 9. 使用Java的方式配置Spring(spring_07_appconfig)

完全不使用xml配置

JavaConfig 原来是 Spring 的一个子项目，它通过 Java 类的方式提供 Bean 的定义信息，在 Spring4 的版本， JavaConfig 已正式成为 Spring4 的核心功能 。



1、编写一个实体类，Dog



```java
@Component  //将这个类标注为Spring的一个组件，放到容器中！
public class Dog {
   public String name = "dog";
}
```

2、新建一个config配置包，编写一个MyConfig配置类

```java
@Configuration  //代表这是一个配置类
public class MyConfig {

   @Bean //通过方法注册一个bean，这里的返回值就Bean的类型，方法名就是bean的id！
   public Dog dog(){
       return new Dog();
  }

}
```

3、测试

```java
@Test
public void test2(){
   ApplicationContext applicationContext =
           new AnnotationConfigApplicationContext(MyConfig.class);
   Dog dog = (Dog) applicationContext.getBean("dog");
   System.out.println(dog.name);
}
```





### 10. 代理模式(spring_08_proxy)

为什么要学习代理模式？因为这就是SpringAOP的底层！【SpringAOP和SpringMVC】

代理模式分类：

- 静态代理是
- 动态代理

学习aop之前 , 我们要先了解一下代理模式！

![image-20210501165829469](/Users/johnny/CS/Spring_Study/1. Spring.assets/image-20210501165829469.png)



#### 10.1 静态代理

角色分析：

* 抽象角色：一般会使用接口或者抽象类来解决
* 真实角色：被代理的角色
* 代理角色：代理真实角色，代理真实角色后，我们一般会做一些附属操作
* 客户：访问代理对象的人

Rent . java 即抽象角色

```java
//抽象角色：租房
public interface Rent {
   public void rent();
}java
```

Host . java 即真实角色

```java
//真实角色: 房东，房东要出租房子
public class Host implements Rent{
   public void rent() {
       System.out.println("房屋出租");
  }
}
```

Proxy . java 即代理角色

```java
//代理角色：中介
public class Proxy implements Rent {

   private Host host;
   public Proxy() { }
   public Proxy(Host host) {
       this.host = host;
  }

   //租房
   public void rent(){
       seeHouse();
       host.rent();
       fare();
  }
   //看房
   public void seeHouse(){
       System.out.println("带房客看房");
  }
   //收中介费
   public void fare(){
       System.out.println("收中介费");
  }
}
```

Client . java 即客户

```java
//客户类，一般客户都会去找代理！
public class Client {
   public static void main(String[] args) {
       //房东要租房
       Host host = new Host();
       //中介帮助房东
       Proxy proxy = new Proxy(host);

       //你去找中介！
       proxy.rent();
  }
}
```

分析：在这个过程中，你直接接触的就是中介，就如同现实生活中的样子，你看不到房东，但是你依旧租到了房东的房子通过代理，这就是所谓的代理模式，程序源自于生活，所以学编程的人，一般能够更加抽象的看待生活中发生的事情。





**静态代理的好处:**

- 可以使得我们的真实角色更加纯粹 . 不再去关注一些公共的事情 .
- 公共的业务由代理来完成 . 实现了业务的分工 ,
- 公共业务发生扩展时变得更加集中和方便 .

缺点 :

* 一个真实角色就会产生一个代理角色， 开发效率降低 

我们想要静态代理的好处，又不想要静态代理的缺点，所以 , 就有了动态代理 !



#### 10.2 加深理解

同学们练习完毕后，我们再来举一个例子，巩固大家的学习！

练习步骤：

1、创建一个抽象角色，比如咋们平时做的用户业务，抽象起来就是增删改查！



```java
//抽象角色：增删改查业务
public interface UserService {
   void add();
   void delete();
   void update();
   void query();
}
```

2、我们需要一个真实对象来完成这些增删改查操作

```java
//真实对象，完成增删改查操作的人
public class UserServiceImpl implements UserService {

   public void add() {
       System.out.println("增加了一个用户");
  }

   public void delete() {
       System.out.println("删除了一个用户");
  }

   public void update() {
       System.out.println("更新了一个用户");
  }

   public void query() {
       System.out.println("查询了一个用户");
  }
}
```

3、需求来了，现在我们需要增加一个日志功能，怎么实现！

- 思路1 ：在实现类上增加代码 【麻烦！】
- 思路2：使用代理来做，能够不改变原来的业务情况下，实现此功能就是最好的了！

4、设置一个代理类来处理日志！代理角色

```java
//代理角色，在这里面增加日志的实现
public class UserServiceProxy implements UserService {
   private UserServiceImpl userService;

   public void setUserService(UserServiceImpl userService) {
       this.userService = userService;
  }

   public void add() {
       log("add");
       userService.add();
  }

   public void delete() {
       log("delete");
       userService.delete();
  }

   public void update() {
       log("update");
       userService.update();
  }

   public void query() {
       log("query");
       userService.query();
  }

   public void log(String msg){
       System.out.println("执行了"+msg+"方法");
  }

}
```

5、测试访问类：

```java
public class Client {
   public static void main(String[] args) {
       //真实业务
       UserServiceImpl userService = new UserServiceImpl();
       //代理类
       UserServiceProxy proxy = new UserServiceProxy();
       //使用代理类实现日志功能！
       proxy.setUserService(userService);

       proxy.add();
  }
}
```

OK，到了现在代理模式大家应该都没有什么问题了，重点大家需要理解其中的思想；

我们在不改变原来的代码的情况下，实现了对原有功能的增强，这是AOP中最核心的思想

聊聊AOP：纵向开发，横向开发

![img](/Users/johnny/CS/Spring_Study/1. Spring.assets/640)





#### 10.3 动态代理

- 动态代理的角色和静态代理的一样 .

- 动态代理的代理类是动态生成的 . 静态代理的代理类是我们提前写好的

- 动态代理分为两类 : 一类是基于接口动态代理 , 一类是基于类的动态代理

  * 基于接口的动态代理----JDK动态代理

  - 基于类的动态代理--cglib
  - 现在用的比较多的是 javasist 来生成动态代理 . 百度一下javasist
  - 我们这里使用JDK的原生代码来实现，其余的道理都是一样的！





**JDK的动态代理需要了解两个类**

核心 : InvocationHandler 和 Proxy ， 打开JDK帮助文档看看

【InvocationHandler：调用处理程序】

[![img](/Users/johnny/CS/Spring_Study/1. Spring.assets/640-20210501230345153)](https://mmbiz.qpic.cn/mmbiz_png/uJDAUKrGC7LoeicP1O2nfyA6H0XPa9jMLxvyvZMwn9gIEibuxjFwE3enJ4TgKO5PXxM5BPr6Bh7GQwExLvst4AsQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

```java
Object invoke(Object proxy, 方法 method, Object[] args)；
//参数
//proxy - 调用该方法的代理实例
//method -所述方法对应于调用代理实例上的接口方法的实例。方法对象的声明类将是该方法声明的接口，它可以是代理类继承该方法的代理接口的超级接口。
//args -包含的方法调用传递代理实例的参数值的对象的阵列，或null如果接口方法没有参数。原始类型的参数包含在适当的原始包装器类的实例中，例如java.lang.Integer或java.lang.Boolean 。
```

【Proxy : 代理】

[![img](/Users/johnny/CS/Spring_Study/1. Spring.assets/640-20210501230647383)](https://mmbiz.qpic.cn/mmbiz_png/uJDAUKrGC7LoeicP1O2nfyA6H0XPa9jMLficZiaPU0h9wdeDicTMgBHemVvIdYTsE712DhkDfg0pdRg169oG5FHTmw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

[![img](/Users/johnny/CS/Spring_Study/1. Spring.assets/640-20210501230647452)](https://mmbiz.qpic.cn/mmbiz_png/uJDAUKrGC7LoeicP1O2nfyA6H0XPa9jMLCIv9ibKb4c9KjmZNNbsDbZojUy0aB1lS3ibqa1SJaBzkK7KneicEX43Zw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

[![img](/Users/johnny/CS/Spring_Study/1. Spring.assets/640-20210501230647486)](https://mmbiz.qpic.cn/mmbiz_png/uJDAUKrGC7LoeicP1O2nfyA6H0XPa9jML394CqGFmCP1nUlaU9mdLk19o1qIzjicTgDiaPz7ibR371jAo3uNNQ8Qgw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



```java
//生成代理类
public Object getProxy(){
   return Proxy.newProxyInstance(this.getClass().getClassLoader(),
                                 rent.getClass().getInterfaces(),this);
}
```

**代码实现**

抽象角色和真实角色和之前的一样！

Rent . java 即抽象角色

```java
//抽象角色：租房
public interface Rent {
   public void rent();
}
```

Host . java 即真实角色j

```java
//真实角色: 房东，房东要出租房子
public class Host implements Rent{
   public void rent() {
       System.out.println("房屋出租");
  }
}
```

ProxyInvocationHandler. java 即代理角色

```java
public class ProxyInvocationHandler implements InvocationHandler {
   private Rent rent;

   public void setRent(Rent rent) {
       this.rent = rent;
  }

   //生成代理类，重点是第二个参数，获取要代理的抽象角色！之前都是一个角色，现在可以代理一类角色
   public Object getProxy(){
       return Proxy.newProxyInstance(this.getClass().getClassLoader(),
               rent.getClass().getInterfaces(),this);
  }

   // proxy : 代理类 method : 代理类的调用处理程序的方法对象.
   // 处理代理实例上的方法调用并返回结果
   @Override
   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
       seeHouse();
       //核心：本质利用反射实现！
       Object result = method.invoke(rent, args);
       fee();
       return result;
  }

   //看房
   public void seeHouse(){
       System.out.println("带房客看房");
  }
   //收中介费
   public void fee(){
       System.out.println("收中介费");
  }

}
```

Client . java

```java
//租客
public class Client {
   public static void main(String[] args) {
       //真实角色
       Host host = new Host();
       //代理实例的调用处理程序
       ProxyInvocationHandler handler = new ProxyInvocationHandler();
       handler.setRent(host); //将真实角色放置进去！
       Rent proxy = (Rent)handler.getProxy(); //动态生成对应的代理类！
       proxy.rent();
  }

}
```

核心：**一个动态代理 , 一般代理某一类业务 , 一个动态代理可以代理多个类，代理的是接口！、**



### 11. AOP(spring_09_aop)

#### 11.1 什么是AOP

AOP（Aspect Oriented Programming）意为：面向切面编程，通过预编译方式和运行期动态代理实现程序功能的统一维护的一种技术。AOP是OOP的延续，是软件开发中的一个热点，也是Spring框架中的一个重要内容，是函数式编程的一种衍生范型。利用AOP可以对业务逻辑的各个部分进行隔离，从而使得业务逻辑各部分之间的耦合度降低，提高程序的可重用性，同时提高了开发的效率。

[![img](/Users/johnny/CS/Spring_Study/1. Spring.assets/640.png)](https://mmbiz.qpic.cn/mmbiz_png/uJDAUKrGC7JAeTYOaaH6rZ6WmLLgwQLHf5pmH30gj6mZm81PC7iauicFu55sicJtspU7K3vTCVdZCDTSHq7D5XHlw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)





#### 11.2 AOP在Spring中的作用

提供声明式事务；允许用户自定义切面

以下名词需要了解下：

- 横切关注点：跨越应用程序多个模块的方法或功能。即是，与我们业务逻辑无关的，但是我们需要关注的部分，就是横切关注点。如日志 , 安全 , 缓存 , 事务等等 ....
- 切面（ASPECT）：横切关注点 被模块化 的特殊对象。即，它是一个类。
- 通知（Advice）：切面必须要完成的工作。即，它是类中的一个方法。
- 目标（Target）：被通知对象。
- 代理（Proxy）：向目标对象应用通知之后创建的对象。
- 切入点（PointCut）：切面通知 执行的 “地点”的定义。
- 连接点（JointPoint）：与切入点匹配的执行点。

[![img](/Users/johnny/CS/Spring_Study/1. Spring.assets/640-20210502202639287)](https://mmbiz.qpic.cn/mmbiz_png/uJDAUKrGC7JAeTYOaaH6rZ6WmLLgwQLHVOZ1JpRb7ViaprZCRXsUbH0bZpibiaTjqib68LQHOWZicSvuU8Y1dquUVGw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

SpringAOP中，通过Advice定义横切逻辑，Spring中支持5种类型的Advice:

[![img](/Users/johnny/CS/Spring_Study/1. Spring.assets/640-20210502202640007.png)](https://mmbiz.qpic.cn/mmbiz_png/uJDAUKrGC7JAeTYOaaH6rZ6WmLLgwQLHbAWH8haUQeJ0LVBxxX0icC5TZlBkEBGibibey7jFrCbibPzQcRhkNFcGAA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

即 Aop 在 不改变原有代码的情况下 , 去增加新的功能 .





#### 11.3 使用Spring实现aop

【重点】使用AOP织入，需要导入一个依赖包！

```xml
<!-- https://mvnrepository.com/artifact/org.aspectj/aspectjweaver -->
<dependency>
   <groupId>org.aspectj</groupId>
   <artifactId>aspectjweaver</artifactId>
   <version>1.9.4</version>
</dependency>
```



方式一：使用Spring的API接口

**通过 Spring API 实现**

首先编写我们的业务接口和实现类

```java
public interface UserService {

   public void add();

   public void delete();

   public void update();

   public void search();

}
public class UserServiceImpl implements UserService{

   @Override
   public void add() {
       System.out.println("增加用户");
  }

   @Override
   public void delete() {
       System.out.println("删除用户");
  }

   @Override
   public void update() {
       System.out.println("更新用户");
  }

   @Override
   public void search() {
       System.out.println("查询用户");
  }
}
```

然后去写我们的增强类 , 我们编写两个 , 一个前置增强 一个后置增强



```java
public class Log implements MethodBeforeAdvice {

   //method : 要执行的目标对象的方法
   //objects : 被调用的方法的参数
   //Object : 目标对象
   @Override
   public void before(Method method, Object[] objects, Object o) throws Throwable {
       System.out.println( o.getClass().getName() + "的" + method.getName() + "方法被执行了");
  }
}
public class AfterLog implements AfterReturningAdvice {
   //returnValue 返回值
   //method被调用的方法
   //args 被调用的方法的对象的参数
   //target 被调用的目标对象
   @Override
   public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
       System.out.println("执行了" + target.getClass().getName()
       +"的"+method.getName()+"方法,"
       +"返回值："+returnValue);
  }
}
```

最后去spring的文件中注册 , 并实现aop切入实现 , 注意导入约束 .



```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xmlns:aop="http://www.springframework.org/schema/aop"
      xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/aop
       http://www.springframework.org/schema/aop/spring-aop.xsd">

   <!--注册bean-->
   <bean id="userService" class="com.ding.service.UserServiceImpl"/>
   <bean id="log" class="com.ding.log.Log"/>
   <bean id="afterLog" class="com.ding.log.AfterLog"/>

   <!--aop的配置-->
   <aop:config>
       <!--切入点 expression:表达式匹配要执行的方法-->
       <aop:pointcut id="pointcut" expression="execution(* com.ding.service.UserServiceImpl.*(..))"/>
       <!--执行环绕; advice-ref执行方法 . pointcut-ref切入点-->
       <aop:advisor advice-ref="log" pointcut-ref="pointcut"/>
       <aop:advisor advice-ref="afterLog" pointcut-ref="pointcut"/>
   </aop:config>

</beans>
```

测试

```java
public class MyTest {
   @Test
   public void test(){
       ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
       UserService userService = (UserService) context.getBean("userService");
       userService.search();
  }
}
```

Aop的重要性 : 很重要 . 一定要理解其中的思路 , 主要是思想的理解这一块 .

Spring的Aop就是将公共的业务 (日志 , 安全等) 和领域业务结合起来 , 当执行领域业务时 , 将会把公共业务加进来 . 实现公共业务的重复利用 . 领域业务更纯粹 , 程序猿专注领域业务 , 其本质还是动态代理 .









方式二：自定义来实现AOP

**自定义类来实现Aop**

目标业务类不变依旧是userServiceImpl

第一步 : 写我们自己的一个切入类

```java
public class DiyPointcut {

   public void before(){
       System.out.println("---------方法执行前---------");
  }
   public void after(){
       System.out.println("---------方法执行后---------");
  }
   
}
```

去spring中配置

```xml
<!--第二种方式自定义实现-->
<!--注册bean-->
<bean id="diy" class="com.ding.config.DiyPointcut"/>

<!--aop的配置-->
<aop:config>
   <!--第二种方式：使用AOP的标签实现-->
   <aop:aspect ref="diy">
       <aop:pointcut id="point" expression="execution(* com.ding.service.UserServiceImpl.*(..))"/>
       <aop:before pointcut-ref="point" method="before"/>
       <aop:after pointcut-ref="point" method="after"/>
   </aop:aspect>
</aop:config>
```

测试：

```java
public class MyTest {
   @Test
   public void test(){
       ApplicationContext context = new ClassPathXmlApplicationContext("applicationContet.xml");
       UserService userService = (UserService) context.getBean("userService");
       userService.add();
  }
}
```





方式三：**使用注解实现**

第一步：编写一个注解实现的增强类

```java
package com.ding.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class AnnotationPointcut {
   @Before("execution(* com.kuang.service.UserServiceImpl.*(..))")
   public void before(){
       System.out.println("---------方法执行前---------");
  }

   @After("execution(* com.kuang.service.UserServiceImpl.*(..))")
   public void after(){
       System.out.println("---------方法执行后---------");
  }

   @Around("execution(* com.kuang.service.UserServiceImpl.*(..))")
   public void around(ProceedingJoinPoint jp) throws Throwable {
       System.out.println("环绕前");
       System.out.println("签名:"+jp.getSignature());
       //执行目标方法proceed
       Object proceed = jp.proceed();
       System.out.println("环绕后");
       System.out.println(proceed);
  }
}
```

第二步：在Spring配置文件中，注册bean，并增加支持注解的配置

```xml
<!--第三种方式:注解实现-->
<bean id="annotationPointcut" class="com.kuang.config.AnnotationPointcut"/>
<aop:aspectj-autoproxy/>
```

aop:aspectj-autoproxy：说明

```
通过aop命名空间的<aop:aspectj-autoproxy />声明自动为spring容器中那些配置@aspectJ切面的bean创建代理，织入切面。当然，spring 在内部依旧采用AnnotationAwareAspectJAutoProxyCreator进行自动代理的创建工作，但具体实现的细节已经被<aop:aspectj-autoproxy />隐藏起来了

<aop:aspectj-autoproxy />有一个proxy-target-class属性，默认为false，表示使用jdk动态代理织入增强，当配为<aop:aspectj-autoproxy  poxy-target-class="true"/>时，表示使用CGLib动态代理技术织入增强。不过即使proxy-target-class设置为false，如果目标类没有声明接口，则spring将自动使用CGLib动态代理。
```

到了这里，AOP的思想和使用相信大家就没问题了！





### 12. 整合Mybatis (spring_10_mybatis)

#### 12.1 步骤

1、导入相关jar包

junit

```xml
<dependency>
   <groupId>junit</groupId>
   <artifactId>junit</artifactId>
   <version>4.12</version>
</dependency>
```

mybatis

```xml
<dependency>
   <groupId>org.mybatis</groupId>
   <artifactId>mybatis</artifactId>
   <version>3.5.2</version>
</dependency>
```

mysql-connector-javax

```xml
<dependency>
   <groupId>mysql</groupId>
   <artifactId>mysql-connector-java</artifactId>
   <version>5.1.47</version>
</dependency>
```

spring相关

```xml
<dependency>
   <groupId>org.springframework</groupId>
   <artifactId>spring-webmvc</artifactId>
   <version>5.1.10.RELEASE</version>
</dependency>
<dependency>
   <groupId>org.springframework</groupId>
   <artifactId>spring-jdbc</artifactId>
   <version>5.1.10.RELEASE</version>
</dependency>
```

aspectJ AOP 织入器

```xml
<!-- https://mvnrepository.com/artifact/org.aspectj/aspectjweaver -->
<dependency>
   <groupId>org.aspectj</groupId>
   <artifactId>aspectjweaver</artifactId>
   <version>1.9.4</version>
</dependency>
```

mybatis-spring整合包 【重点】

```xml
<dependency>
   <groupId>org.mybatis</groupId>
   <artifactId>mybatis-spring</artifactId>
   <version>2.0.2</version>
</dependency>
```

配置Maven静态资源过滤问题！

```xml
<build>
   <resources>
       <resource>
           <directory>src/main/java</directory>
           <includes>
               <include>**/*.properties</include>
               <include>**/*.xml</include>
           </includes>
           <filtering>true</filtering>
       </resource>
   </resources>
</build>
```

2、编写配置文件

3、代码实现



#### 12.2 回忆MyBatis

**编写pojo实体类**

```java
package com.ding.pojo;

import lombok.Data;

@Data
public class User {
    private int id;
    private String name;
    private String pwd;
}
```

**UserDao接口编写**

```java
package com.ding.mapper;

import com.ding.pojo.User;

import java.util.List;

public interface UserMapper {
    public List<User> selectUser();
}
```

**接口对应的Mapper映射文件**

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.ding.mapper.UserMapper">
    <select id="selectUser" resultType="User">
        select * from mybatis.user
    </select>
</mapper>
```

**实现mybatis的配置文件**

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">

<configuration>
    
    <typeAliases>
        <package name="com.ding.pojo"/>
    </typeAliases>

    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis?useSSL=true&amp;
                                            useUnicode=true&amp;characterEncoding=utf8&amp;
                                            serverTimezone=Asia/Shanghai"/>
                <property name="username" value="root"/>
                <property name="password" value="Djh1023921169?"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="com/ding/mapper/UserMapper.xml"/>
    </mappers>
</configuration>
```

**测试类**

```java
public class MyTest {
    @Test
    public void test1() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream in = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList = mapper.selectUser();
        for (User user : userList) {
            System.out.println(user);
        }
    }
}
```



#### 12.3 Mybatis-Spring学习

引入Spring之前需要了解mybatis-spring包中的一些重要类；

http://www.mybatis.org/spring/zh/index.html

[![img](/Users/johnny/CS/Spring_Study/1. Spring.assets/640-20210503101141898.png)](https://mmbiz.qpic.cn/mmbiz_png/uJDAUKrGC7JXnZ2vicYHdibF3mvk0w89CqKXn1bvBicZVeBJHGlFZOd8a6X5uzjVp6RnMR4ibS7hPdrHSxNGevkib0w/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

**什么是 MyBatis-Spring？**

MyBatis-Spring 会帮助你将 MyBatis 代码无缝地整合到 Spring 中。

**知识基础**

在开始使用 MyBatis-Spring 之前，你需要先熟悉 Spring 和 MyBatis 这两个框架和有关它们的术语。这很重要

MyBatis-Spring 需要以下版本：

| MyBatis-Spring | MyBatis | Spring 框架 | Spring Batch | Java    |
| :------------- | :------ | :---------- | :----------- | :------ |
| 2.0            | 3.5+    | 5.0+        | 4.0+         | Java 8+ |
| 1.3            | 3.4+    | 3.2.2+      | 2.1+         | Java 6+ |

如果使用 Maven 作为构建工具，仅需要在 pom.xml 中加入以下代码即可：

```xml
<dependency>
   <groupId>org.mybatis</groupId>
   <artifactId>mybatis-spring</artifactId>
   <version>2.0.2</version>
</dependency
```

要和 Spring 一起使用 MyBatis，需要在 Spring 应用上下文中定义至少两样东西：一个 SqlSessionFactory 和至少一个数据映射器类。

在 MyBatis-Spring 中，可使用SqlSessionFactoryBean来创建 SqlSessionFactory。要配置这个工厂 bean，只需要把下面代码放在 Spring 的 XML 配置文件中：

```xml
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
 <property name="dataSource" ref="dataSource" />
</bean>
```

注意：SqlSessionFactory需要一个 DataSource（数据源）。这可以是任意的 DataSource，只需要和配置其它 Spring 数据库连接一样配置它就可以了。

在基础的 MyBatis 用法中，是通过 SqlSessionFactoryBuilder 来创建 SqlSessionFactory 的。而在 MyBatis-Spring 中，则使用 SqlSessionFactoryBean 来创建。

在 MyBatis 中，你可以使用 SqlSessionFactory 来创建 SqlSession。一旦你获得一个 session 之后，你可以使用它来执行映射了的语句，提交或回滚连接，最后，当不再需要它的时候，你可以关闭 session。

SqlSessionFactory有一个唯一的必要属性：用于 JDBC 的 DataSource。这可以是任意的 DataSource 对象，它的配置方法和其它 Spring 数据库连接是一样的。

一个常用的属性是 configLocation，它用来指定 MyBatis 的 XML 配置文件路径。它在需要修改 MyBatis 的基础配置非常有用。通常，基础配置指的是 < settings> 或 < typeAliases>元素。

需要注意的是，这个配置文件并不需要是一个完整的 MyBatis 配置。确切地说，任何环境配置（），数据源（）和 MyBatis 的事务管理器（）都会被忽略。SqlSessionFactoryBean 会创建它自有的 MyBatis 环境配置（Environment），并按要求设置自定义环境的值。

SqlSessionTemplate 是 MyBatis-Spring 的核心。作为 SqlSession 的一个实现，这意味着可以使用它无缝代替你代码中已经在使用的 SqlSession。

模板可以参与到 Spring 的事务管理中，并且由于其是线程安全的，可以供多个映射器类使用，你应该总是用 SqlSessionTemplate 来替换 MyBatis 默认的 DefaultSqlSession 实现。在同一应用程序中的不同类之间混杂使用可能会引起数据一致性的问题。

可以使用 SqlSessionFactory 作为构造方法的参数来创建 SqlSessionTemplate 对象。

```xml
<bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
 <constructor-arg index="0" ref="sqlSessionFactory" />
</bean>
```

现在，这个 bean 就可以直接注入到你的 DAO bean 中了。你需要在你的 bean 中添加一个 SqlSession 属性，就像下面这样：

```java
public class UserMapperImpl implements UserMapper {

   private SqlSession sqlSession;

   public void setSqlSession(SqlSession sqlSession) {
     this.sqlSession = sqlSession;
  }

   public User getUser(String userId) {
     return sqlSession.getMapper(UserMapper.class).getUser();
  }
}
```

按下面这样，注入 SqlSessionTemplate：

```xml

<bean id="userMapper" class="com.ding.mapper.UserMapperImpl">
  <property name="sqlSession" ref="sqlSession"/>
</bean>
```

> **整合实现一**

1、引入Spring配置文件beans.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">
```

2、配置数据源替换mybaits的数据源

```xml
<!--配置数据源：数据源有非常多，可以使用第三方的，也可使使用Spring的-->
<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
   <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
   <property name="url" value="jdbc:mysql://localhost:3306/mybatis?useSSL=true&amp;useUnicode=true&amp;characterEncoding=utf8"/>
   <property name="username" value="root"/>
   <property name="password" value="Djh1023921169?"/>
</bean>
```

3、配置SqlSessionFactory，关联MyBatis

```xml
<!--配置SqlSessionFactory-->
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
   <property name="dataSource" ref="dataSource"/>
   <!--关联Mybatis-->
   <property name="configLocation" value="classpath:mybatis-config.xml"/>
   <property name="mapperLocations" value="classpath:com/ding/mapper/*.xml"/>
</bean>
```

4、注册sqlSessionTemplate，关联sqlSessionFactory；

```xml
<!--注册sqlSessionTemplate , 关联sqlSessionFactory-->
<bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
   <!--利用构造器注入-->
   <constructor-arg index="0" ref="sqlSessionFactory"/>
</bean>
```

5、增加Dao接口的实现类；私有化sqlSessionTemplate

```java
public class UserMapperImpl implements UserMapper {

   //sqlSession不用我们自己创建了，Spring来管理
   private SqlSessionTemplate sqlSession;

   public void setSqlSession(SqlSessionTemplate sqlSession) {
       this.sqlSession = sqlSession;
  }

   public List<User> selectUser() {
       UserMapper mapper = sqlSession.getMapper(UserMapper.class);
       return mapper.selectUser();
  }
   
}
```

6、注册bean实现

```xml
<bean id="userMapper" class="com.kuang.ding.UsermMapperImpl">
   <property name="sqlSession" ref="sqlSession"/>
</bean>
```

7、测试

```java
   @Test
   public void test2(){
       ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
       UserMapper mapper = (UserMapper) context.getBean("userMapper");
       List<User> user = mapper.selectUser();
       System.out.println(user);
  }
```

结果成功输出！现在我们的Mybatis配置文件的状态！发现都可以被Spring整合！

```java
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
       PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
       "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
   <typeAliases>
       <package name="com.ding.pojo"/>
   </typeAliases>
</configuration>
```





**整合实现二**

mybatis-spring1.2.3版以上的才有这个 .

官方文档截图 :

dao继承Support类 , 直接利用 getSqlSession() 获得 , 然后直接注入SqlSessionFactory . 比起方式1 , 不需要管理SqlSessionTemplate , 而且对事务的支持更加友好 . 可跟踪源码查看

[![img](/Users/johnny/CS/Spring_Study/1. Spring.assets/640-20210503101753631.png)](https://mmbiz.qpic.cn/mmbiz_png/uJDAUKrGC7JXnZ2vicYHdibF3mvk0w89Cq6qzPguHd15ribJeF3ickXAy98VG8YFjxwftEdsvvCLWkWLPeiaiaVibbPxQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

测试：

1、将我们上面写的UserMapperImpl修改一下

```java
public class UserMapperImpl extends SqlSessionDaoSupport implements UserMapper {
   public List<User> selectUser() {
       UserMapper mapper = getSqlSession().getMapper(UserMapper.class);
       return mapper.selectUser();
  }
}
```

2、修改applicationContext的配置

```xml
<bean id="userMapper2" class="com.kuang.ding.UserMapperImpl">
   <property name="sqlSessionFactory" ref="sqlSessionFactory" />
</bean>
```

3、测试

```xml
@Test
public void test2(){
   ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
   UserMapper mapper = (UserMapper) context.getBean("userMapper2");
   List<User> user = mapper.selectUser();
   System.out.println(user);
}
```

**总结 : 整合到spring以后可以完全不要mybatis的配置文件，除了这些方式可以实现整合之外，我们还可以使用注解来实现，这个等我们后面学习SpringBoot的时候还会测试整合！**



### 13. 声明式事务(spring_11_transaction)

#### 13.1 回顾事务

- 事务在项目开发过程非常重要，涉及到数据的一致性的问题，不容马虎！
- 事务管理是企业级应用程序开发中必备技术，用来确保数据的完整性和一致性。

事务就是把一系列的动作当成一个独立的工作单元，这些动作要么全部完成，要么全部不起作用。

**事务四个属性ACID**

1. 原子性（atomicity）
   * 事务是原子性操作，由一系列动作组成，事务的原子性确保动作要么全部完成，要么完全不起作用
2. 一致性（consistency）
   * 一旦所有事务动作完成，事务就要被提交。数据和资源处于一种满足业务规则的一致性状态中
3. 隔离性（isolation）
   * 可能多个事务会同时处理相同的数据，因此每个事务都应该与其他事务隔离开来，防止数据损坏
4. 持久性（durability）
   * 事务一旦完成，无论系统发生什么错误，结果都不会受到影响。通常情况下，事务的结果被写到持久化存储器中

#### 13.2 Spring中的事务管理

将上面的代码拷贝到一个新项目中

在之前的案例中，我们给userDao接口新增两个方法，删除和增加用户；

```java
//添加一个用户
int addUser(User user);

//根据id删除用户
int deleteUser(int id);
```

mapper文件，我们故意把 deletes 写错，测试！

```xml
<insert id="addUser" parameterType="com.ding.pojo.User">
insert into user (id,name,pwd) values (#{id},#{name},#{pwd})
</insert>

<delete id="deleteUser" parameterType="int">
deletes from user where id = #{id}
</delete>
```

编写接口的实现类，在实现类中，我们去操作一波

```java
public class UserDaoImpl extends SqlSessionDaoSupport implements UserMapper {

   //增加一些操作
   public List<User> selectUser() {
       User user = new User(4,"小明","123456");
       UserMapper mapper = getSqlSession().getMapper(UserMapper.class);
       mapper.addUser(user);
       mapper.deleteUser(4);
       return mapper.selectUser();
  }

   //新增
   public int addUser(User user) {
       UserMapper mapper = getSqlSession().getMapper(UserMapper.class);
       return mapper.addUser(user);
  }
   //删除
   public int deleteUser(int id) {
       UserMapper mapper = getSqlSession().getMapper(UserMapper.class);
       return mapper.deleteUser(id);
  }

}
```

测试

```java
@Test
public void test2(){
   ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
   UserMapper mapper = (UserMapper) context.getBean("userMapper");
   List<User> user = mapper.selectUser();
   System.out.println(user);
}
```

报错：sql异常，delete写错了

结果 ：插入成功！

没有进行事务的管理；我们想让他们都成功才成功，有一个失败，就都失败，我们就应该需要**事务！**

以前我们都需要自己手动管理事务，十分麻烦！

但是Spring给我们提供了事务管理，我们只需要配置即可；

**编程式事务管理**

- 将事务管理代码嵌到业务方法中来控制事务的提交和回滚
- 缺点：必须在每个事务操作业务逻辑中包含额外的事务管理代码

**声明式事务管理**

- 一般情况下比编程式事务好用。
- 将事务管理代码从业务方法中分离出来，以声明的方式来实现事务管理。
- 将事务管理作为横切关注点，通过aop方法模块化。Spring中通过Spring AOP框架支持声明式事务管理。



**使用Spring管理事务，注意头文件的约束导入 : tx**



```xml
xmlns:tx="http://www.springframework.org/schema/tx"

http://www.springframework.org/schema/tx
http://www.springframework.org/schema/tx/spring-tx.xsd">
```

**事务管理器**

- 无论使用Spring的哪种事务管理策略（编程式或者声明式）事务管理器都是必须的。
- 就是 Spring的核心事务管理抽象，管理封装了一组独立于技术的方法。

**JDBC事务**



```xml
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
       <property name="dataSource" ref="dataSource" />
</bean>
```

**配置好事务管理器后我们需要去配置事务的通知**



```xml
<!--配置事务通知-->
<tx:advice id="txAdvice" transaction-manager="transactionManager">
   <tx:attributes>
       <!--配置哪些方法使用什么样的事务,配置事务的传播特性-->
       <tx:method name="add" propagation="REQUIRED"/>
       <tx:method name="delete" propagation="REQUIRED"/>
       <tx:method name="update" propagation="REQUIRED"/>
       <tx:method name="search*" propagation="REQUIRED"/>
       <tx:method name="get" read-only="true"/>
       <tx:method name="*" propagation="REQUIRED"/>
   </tx:attributes>
</tx:advice>
```

**spring事务传播特性：**

事务传播行为就是多个事务方法相互调用时，事务如何在这些方法间传播。spring支持7种事务传播行为：

- propagation_requierd：如果当前没有事务，就新建一个事务，如果已存在一个事务中，加入到这个事务中，这是最常见的选择。
- propagation_supports：支持当前事务，如果没有当前事务，就以非事务方法执行。
- propagation_mandatory：使用当前事务，如果没有当前事务，就抛出异常。
- propagation_required_new：新建事务，如果当前存在事务，把当前事务挂起。
- propagation_not_supported：以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
- propagation_never：以非事务方式执行操作，如果当前事务存在则抛出异常。
- propagation_nested：如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则执行与propagation_required类似的操作

Spring 默认的事务传播行为是 PROPAGATION_REQUIRED，它适合于绝大多数的情况。

假设 ServiveX#methodX() 都工作在事务环境下（即都被 Spring 事务增强了），假设程序中存在如下的调用链：Service1#method1()->Service2#method2()->Service3#method3()，那么这 3 个服务类的 3 个方法通过 Spring 的事务传播机制都工作在同一个事务中。

就好比，我们刚才的几个方法存在调用，所以会被放在一组事务当中！

**配置AOP**

导入aop的头文件！

```xml
<!--配置aop织入事务-->
<aop:config>
   <aop:pointcut id="txPointcut" expression="execution(* com.ding.mapper.*.*(..))"/>
   <aop:advisor advice-ref="txAdvice" pointcut-ref="txPointcut"/>
</aop:config>
```

**进行测试**

删掉刚才插入的数据，再次测试！

```java
@Test
public void test2(){
   ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
   UserMapper mapper = (UserMapper) context.getBean("userMapper");
   List<User> user = mapper.selectUser();
   System.out.println(user);
}
```

> 思考问题？

为什么需要配置事务？

- 如果不配置，就需要我们手动提交控制事务；
- 事务在项目开发过程非常重要，涉及到数据的一致性的问题，不容马虎！