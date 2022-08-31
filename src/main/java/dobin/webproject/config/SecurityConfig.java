package dobin.webproject.config;

import dobin.webproject.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// 1-1. WebSecurityConfigurerAdapter를 상속받는 클래스에 @EnableWebSecurity 어노테이션을 선언하면
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter { // 1-2. SpringSecurityFilterChain이 자동으로 포함됩니다. WebSecurityConfigurerAdapter를 상속받아서 메소드 오버라이딩을 통해 보안 설정을 커스터마이징 할 수 있습니다.

    @Autowired
    MemberService memberService;

    @Bean
    public PasswordEncoder passwordEncoder() { // 1-3. 비밀번호를 데이터베이스에 저장시 비밀번호를 암호화 해주는 PasswordEncoder(BCryptPasswordEncoder)를 빈으로 등록
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { // 1-4. http 요청에 대한 보안을 설정합니다. 페이지 권한 설정, 로그인 페이지 설정, 로그아웃 메소드 등에 대한 설정을 작성합니다. 이 메소드를 구현 안하면  @SpringBootApplication(exclude = SecurityAutoConfiguration.class)처럼 인증을 무시합니다.
        http.formLogin()
                .loginPage("/members/login") // 1-5. 컨트롤러에 맵핑되어 있는 로그인 페이지로 가는 @GetMapping에 등록된 url 입력
                .defaultSuccessUrl("/")      // 1-6. 로그인 성공시 이동할 URL 입력
                .usernameParameter("email")  // 1-7. MemberService에서  UserDetails loadUserByUsername(String email)에서 파라미터로 email이란 이름을 넣어줬으니 email이란 이름으로 등록
                .failureUrl("/members/login/error") // 1-8. 로그인 실패시 이동할 URL
                .and()
                .logout() // 1-9. 로그아웃 1
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) // 1-9. 로그아웃 2 -> 로그아웃 성공시 이동할 url을 AntPathRequestMatcher로 전달
                .logoutSuccessUrl("/") // 1-10. 로그아웃 성공시 이동할 URL 입력
        ;

        http.authorizeRequests() // 2-1. Security 처리를 하는데 HttpServletRequest를 이용한다는 뜻.
                .mvcMatchers("/", "/members/**", "/item/**", "/img/**", "/images/**","/company/**","/api/**","/board/**", "/support/**").permitAll() // 2-2. 해당 URL은 인증(로그인) 없이 접근 할 수 있도록 설정.
                .mvcMatchers("/admin/**").hasRole("ADMIN") // 2-3. /admin으로 시작하는 경로는 ADMIN 권한만 접근 가능
                .anyRequest().authenticated() // 2-4. 2-2, 2-3에서 설정한 경로를 제외한 나머지 경로는 모두 인증을 요구한다는 의미.
        ;

        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // 1-5. 인증되지 않은 사용자가 리소스에 접근하였을 때 실행되는 핸들러를 등록.
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 2-6. static 디렉터리의 하위 파일은 인증을 무시하도록 설정
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/images/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { // 1-11. Spring Security 인증은 AuthenticationManager를 통해 이루어지며 AuthenticationManagerBuilder가 AuthenticationManager를 생성합니다.
        auth.userDetailsService(memberService) // 1-12. userDetailService를 구현하고 있는 객체로 membeService를 지정.
                .passwordEncoder(passwordEncoder()); // 1-13. 비밀번호 암호화를 위해 bean으로 등록된 passwordEncoder 객체를 생성해 지정해줍니다.
    }
}
