import com.ding.dao.UserDaoMySqlImpl;
import com.ding.dao.UserDaoOracleImpl;
import com.ding.dao.UserDaoSqlserverImpl;
import com.ding.service.UserServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {
    public static void main(String[] args) {
        // 用户实际调用的是业务层，dao层他们不需要接触
        // UserServiceImpl userService = new UserServiceImpl();
        //
        // userService.setUserDao(new UserDaoSqlserverImpl());
        //
        // userService.getUser();

        // 获取ApplicaitonContext
        ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        UserServiceImpl userServiceImpl = (UserServiceImpl) context.getBean("userServiceImpl");
        userServiceImpl.getUser();
    }
}
