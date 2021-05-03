import com.ding.pojo.Hello;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {
    public static void main(String[] args) {
        // 获取Spring的上下文对象
        ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        // 我们的对象现在都在Spring中管理，我们要水涌，直接去里面取出来既可以
        Hello hello = (Hello) context.getBean("hello");
        System.out.println(hello.toString());
    }
}
