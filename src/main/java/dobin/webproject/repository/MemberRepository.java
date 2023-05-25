package dobin.webproject.repository;

import dobin.webproject.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email); // 이메일 검사
    Member findByName(String name);
}