package dobin.webproject.repository;

import dobin.webproject.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 1-1. 회원 가입시 중복된 회원이 있는지 검사하기 위해 이메일로 회원을 검사할 수 있도록 쿼리 메소드 생성.
    Member findByEmail(String email);
    Member findByName(String name);
}
