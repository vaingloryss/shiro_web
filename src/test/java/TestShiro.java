import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vaingloryss
 * @date 2019/9/28 0028 下午 2:21
 */
public class TestShiro {
    //入门
    //shiro.ini
    @Test
    public void test01(){
        //获取SecurityManager工厂，此处使用ini配置文件初始化SecurityManager
        Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro/shiro.ini");//弃用，

        //得到SecurityManager实例（shiro核心），并绑定给SecurityUtils
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        //托管给SecurityUtils
        SecurityUtils.setSecurityManager(securityManager);

        //得到Subject，直接由用户使用，调用功能简单，底层调用SecurityManager的方法
        //Subject可以直接使用shiro的几乎所有操作（加密除外，加密不归SecurityManager管理）
        Subject currentUser = SecurityUtils.getSubject();
        //创建 用户名/密码 身份验证的Token（即用户身份/凭证）
        UsernamePasswordToken token = new UsernamePasswordToken("zhang","123");
        try {
            //登录,即身份验证
            currentUser.login(token);
        }catch (AuthenticationException e){
            //身份验证失败
            e.printStackTrace();
        }
        //断言用已经登录
        //subject.isAuthenticated()获取当前用户的登录状态，需要session的支持（同步session中的用户信息）
        Assert.assertEquals(true,currentUser.isAuthenticated());
        //退出
        currentUser.logout();
    }

    //入门
    //a-shiro.ini
    @Test
    public void test0a(){
        //获取SecurityManager工厂，此处使用ini配置文件初始化SecurityManager
        Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro/a-shiro.ini");//弃用，

        //得到SecurityManager实例（shiro核心），并绑定给SecurityUtils
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        //托管给SecurityUtils
        SecurityUtils.setSecurityManager(securityManager);

        //得到Subject，直接由用户使用，调用功能简单，底层调用SecurityManager的方法
        //Subject可以直接使用shiro的几乎所有操作（加密除外，加密不归SecurityManager管理）
        Subject currentUser = SecurityUtils.getSubject();
        //创建 用户名/密码 身份验证的Token（即用户身份/凭证）
        UsernamePasswordToken token = new UsernamePasswordToken("zhangsan","123");
        try {
            //登录,即身份验证
            currentUser.login(token);
        }catch (AuthenticationException e){
            //身份验证失败
            e.printStackTrace();
        }
        //获取用户身份凭证
        System.out.println("-------------用户名----------------");
        System.out.println(currentUser.getPrincipal());//zhangsan

        //判断当前用户是否拥有某个角色
        System.out.println("-------------单个角色----------------");
        System.out.println(currentUser.hasRole("admin"));//true

        System.out.println("-------------角色组----------------");
        List<String> roles = new ArrayList<>();
        roles.add("admin");
        roles.add("manager");
        boolean[] booleans = currentUser.hasRoles(roles);
        for (boolean aBoolean : booleans) {
            System.out.println(aBoolean);
        }
        //判断当前用户是否拥有某个权限
        System.out.println("-------------单个权限----------------");
        System.out.println(currentUser.isPermitted("user:delete"));//true

        //判断当前用户是否拥有某组权限，返回一个boolean类型的数组
        System.out.println("-------------权限组----------------");
        boolean[] permitted = currentUser.isPermitted("user:delete", "user:insert");
        for (boolean b : permitted) {
            System.out.println(b);
        }

        //断言用已经登录
        //subject.isAuthenticated()获取当前用户的登录状态，需要session的支持（同步session中的用户信息）
        Assert.assertEquals(true,currentUser.isAuthenticated());
        //登出，清除当前用户的所有登录信息，可以防止CXRF攻击
        currentUser.logout();
    }


    //customRealm
    //shiro-realm.ini、shiro-multi-realm.ini
    @Test
    public void test02() {
        //1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        //一个customRealm
        //Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-realm.ini");
        //多个customRealm
        Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro/shiro-multi-realm.ini");
        //2、得到SecurityManager实例 并绑定给SecurityUtils
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
        try {
            //4、登录，即身份验证
            subject.login(token);
        } catch (AuthenticationException e) {
            //5、身份验证失败
            e.printStackTrace();
        }
        Assert.assertEquals(true, subject.isAuthenticated()); //断言用户已经登录
        //6、退出
        subject.logout();
    }

    //数据库访问
    //shiro-jdbc-realm.ini
    //JdbcRealm中有默认的SQL语句实现
    @Test
    public void testJDBCRealm() {
        //1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro/shiro-jdbc-realm.ini");

        //2、得到SecurityManager实例 并绑定给SecurityUtils
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("Jack", "123123");
        try {
            //4、登录，即身份验证
            subject.login(token);
        } catch (AuthenticationException e) {
            //5、身份验证失败
            e.printStackTrace();
        }
        Assert.assertEquals(true, subject.isAuthenticated()); //断言用户已经登录
        //6、退出
        subject.logout();
    }

    //自定义SqlRealm的SQL语句
    @Test
    public void test03(){
        //第一步：配置数据源
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/stushiro?useSSL=true");
        dataSource.setUsername("root");
        dataSource.setPassword("sx96411");

        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(dataSource);
        jdbcRealm.setPermissionsLookupEnabled(true);

        //第二步：自定义sql查询
        String sql = "select password from tb_user where username=?";
        jdbcRealm.setAuthenticationQuery(sql);//登录
        String sql1 = "select role_user from tb_user_role where username=?";
        jdbcRealm.setUserRolesQuery(sql1);//角色验证

        //第三步：构建SecurityManager环境，进行登录以及角色验证
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(jdbcRealm);
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();

        //主体提交授权认证
        UsernamePasswordToken token = new UsernamePasswordToken("Jack","123123");
        subject.login(token);
        System.out.println("isAuthenticate:"+subject.isAuthenticated());
        subject.checkRole("admin");

    }


    @After
    public void tearDown() throws Exception {
        ThreadContext.unbindSubject();//退出时请解除绑定Subject到线程 否则对下次测试造成影响
    }
}
