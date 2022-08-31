package dobin.webproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // 1-1. @Value 어노테이션(org.springframework.beans.factory.annotation.Value) -> application.properties에 설정한 "uploadPath" 프로퍼티 값을 읽어옴.
    @Value("${uploadPath}")
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/images/**") // 1-2. /images/**로 요청이 들어오면
                .addResourceLocations(uploadPath);                // 1-3. uploadPath에 저장된 경로를 탐색한다.
    }
}
