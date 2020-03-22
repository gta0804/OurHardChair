package fudan.se.lab2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorConfig extends WebMvcConfigurationSupport {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //此处配置拦截路径
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns("/**/login/**","/**/register/**");
    }
}
