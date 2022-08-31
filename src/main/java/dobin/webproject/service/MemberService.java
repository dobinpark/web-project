package dobin.webproject.service;

import dobin.webproject.entity.Member;
import dobin.webproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional // 1-1. 트랜잭션을 걸어준다.
@RequiredArgsConstructor
public class MemberService implements UserDetailsService { // 1-8. MemberService가 UserDetailsService를 구현한다.

    private final MemberRepository memberRepository;

    // 1-4. 회원 가입 서비스
    public Member saveMember(Member member) {
        // 1-6. 있는 회원인지 검증
        validateDuplicateMember(member);

        // 1-7. 1-6을 통과했다면 회원 가입
        return memberRepository.save(member);
    }

    // 1-5. 이미 가입된 회원이 있는지 체크하고 이미 가입된 회원이라면 IllegalStateException 예외를 터트린다.
    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());

        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    // 1-9. loadUserByUsername 오버라이딩(재정의), 로그인
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { // 1-10. 우리는 email을 아이디로 사용하니 username -> email로 수정
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
