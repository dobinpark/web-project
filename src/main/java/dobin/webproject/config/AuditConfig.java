package dobin.webproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // 1-1. JPA에서 Auditing 기능을 활성화
public class AuditConfig {

    @Bean // 1-2. 사용자의 이름을 등록자와 수정자로 지정(처리)해주는 AuditorAware을 빈으로 등록
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
