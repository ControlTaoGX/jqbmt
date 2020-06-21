package lyt.jingqi.badminton.config;

import lyt.jingqi.badminton.common.Constants;
import lyt.jingqi.badminton.interceptor.AdminLoginInterceptor;
import lyt.jingqi.badminton.interceptor.JqBadmintonCartNumberInterceptor;
import lyt.jingqi.badminton.interceptor.JqBadmintonLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class JqBadmintonWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private AdminLoginInterceptor adminLoginInterceptor;   //注入拦截器
    @Autowired
    private JqBadmintonLoginInterceptor jqBadmintonLoginInterceptor;
    @Autowired
    private JqBadmintonCartNumberInterceptor jqBadmintonCartNumberInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        //后台登录拦截,拦截除login和静态组件之外的所有资源
        registry.addInterceptor(adminLoginInterceptor)
                .addPathPatterns("/admin/**")   //拦截所有
                .excludePathPatterns("/admin/login")   //除了login
                .excludePathPatterns("/admin/dist/**")   //除了dist，plugins
                .excludePathPatterns("/admin/plugins/**");


        // 购物车中的数量统一处理
        registry.addInterceptor(jqBadmintonCartNumberInterceptor)
                .excludePathPatterns("/admin/**")
                .excludePathPatterns("/register")
                .excludePathPatterns("/login")
                .excludePathPatterns("/logout");
        // 商城页面登陆拦截
        registry.addInterceptor(jqBadmintonLoginInterceptor)
                .excludePathPatterns("/admin/**")
                .excludePathPatterns("/register")
                .excludePathPatterns("/login")
                .excludePathPatterns("/logout")
                .addPathPatterns("/goods/detail/**")
                .addPathPatterns("/shop-cart")
                .addPathPatterns("/shop-cart/**")
                .addPathPatterns("/saveOrder")
                .addPathPatterns("/orders")
                .addPathPatterns("/orders/**")
                .addPathPatterns("/personal")
                .addPathPatterns("/personal/updateInfo")
                .addPathPatterns("/selectPayType")
                .addPathPatterns("/payPage");
    }
//
    public void addResourceHandlers(ResourceHandlerRegistry registry) { //文件上传路径回显
        registry.addResourceHandler("/uploadimg/**").addResourceLocations("file:" + Constants.FILE_UPLOAD_DIC);
    }
}
