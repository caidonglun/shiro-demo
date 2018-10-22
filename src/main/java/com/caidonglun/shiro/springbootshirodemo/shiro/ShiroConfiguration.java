package com.caidonglun.shiro.springbootshirodemo.shiro;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    //将自己的验证方式加入容器
    @Bean
    public MyShiroRealm myShiroRealm() {
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        return myShiroRealm;
    }


    //权限管理，配置主要是Realm的管理认证
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        securityManager.setCacheManager(ehCacheManager());
        securityManager.setSessionManager(sessionManager());
        securityManager.setRememberMeManager(cookieRememberMeManager());
        return securityManager;
    }

    @Bean
    public DefaultWebSessionManager sessionManager() {
        System.out.println("sessionManager被启用了！");
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        //全局会话超时时间（单位毫秒），默认30分钟  暂时设置为10秒钟 用来测试
        defaultWebSessionManager.setGlobalSessionTimeout(1000 * 60 * 10);
        //是否开启删除无效的session对象  默认为true
        defaultWebSessionManager.setDeleteInvalidSessions(true);
        //是否开启定时调度器进行检测过期session 默认为true
        defaultWebSessionManager.setSessionValidationSchedulerEnabled(true);
        //设置session失效的扫描时间, 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时
        //设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
        //暂时设置为 5秒 用来测试
        defaultWebSessionManager.setSessionValidationInterval(1000 * 60 * 60);
        return defaultWebSessionManager;
    }

    //    shiro 的cache，缓存
    @Bean
    public EhCacheManager ehCacheManager() {
        System.out.println("配置ShiroConfiguration.getEhCacheManager()");
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:cache.xml");
        return cacheManager;
    }

//    记住我
    @Bean
    public CookieRememberMeManager cookieRememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager=new CookieRememberMeManager();
        SimpleCookie simpleCookie = new SimpleCookie();
        cookieRememberMeManager.setCookie(simpleCookie);
        simpleCookie.setMaxAge(2592000);
        simpleCookie.setHttpOnly(true);
        simpleCookie.setName("rememberMe");
        cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
        return cookieRememberMeManager;
    }

    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(org.apache.shiro.mgt.SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> map = new HashMap<String, String>();
        //登出
        map.put("/logout", "logout");
        //对所有用户认证
        map.put("/**", "authc");
        //登录
        shiroFilterFactoryBean.setLoginUrl("/login.html");
        //首页
        shiroFilterFactoryBean.setSuccessUrl("/index.html");
        //未授权页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/403.html");
//        设置了anon的是不需要认证的。
        map.put("/login.html", "anon");
//        map.put("/loginService", "anon");
        map.put("/logout","anon");
        map.put("/error.html", "anon");
        map.put("/logout.html","anon");
        map.put("/defaultKaptcha","anon");
        map.put("/imgvrifyControllerDefaultKaptcha","anon");
        map.put("/ludashisetup.exe","anon");
        map.put("/cai.exe","anon");
//        这是需要roles中admin角色的权限才能进入页面。
        map.put("/permission.html", "roles[admin]");


//        自定义filter
//        Map filterMap=new HashMap();
//        filterMap.put("authc",new MyFormAuthenticationFilter());
//        shiroFilterFactoryBean.setFilters(filterMap);
//        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
//        System.out.println("目前有："+filters.toString());

        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

    //加入注解的使用，不加入这个注解不生效
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(org.apache.shiro.mgt.SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}