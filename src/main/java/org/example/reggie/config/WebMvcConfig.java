package org.example.reggie.config;

import lombok.extern.slf4j.Slf4j;
import org.example.reggie.common.JacksonObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 设置静态资源映射
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开启静态资源映射");

        registry.addResourceHandler("/backend/**")
                .addResourceLocations("classpath:/backend/");

        registry.addResourceHandler("/front/**")
                .addResourceLocations("classpath:/front/");
    }

    /**
     * 扩展mvc转换器
     * @param converters the list of configured converters to extend
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter mappingConverter=new MappingJackson2HttpMessageConverter();
        mappingConverter.setObjectMapper(new JacksonObjectMapper());
        converters.add(0,mappingConverter);
    }
}
