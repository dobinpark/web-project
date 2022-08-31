package dobin.webproject.entity;

import dobin.webproject.constant.Role;
import dobin.webproject.dto.MemberFormDto;
import dobin.webproject.entity.board.FreeBoard;
import dobin.webproject.entity.board.NoticeBoard;
import dobin.webproject.entity.board.QnaBoard;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Data
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true) // 1-1. 회원은 이메일을 통해 유일하게 구분되어야 하기 때문에, 동일한 값이 데이터베이스에 들어올 수 없도록 유니크 제약조건을 걸어준다.
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING) // 1-2. EnumType.STRING
    private Role role;

    // 1-3. 회원가입 창에서 넘어온 MemberFormDto 데이터를 Member Entity로 변환해 return(이때 비밀번호 암호화)
    public static Member createMember(MemberFormDto memberFormDto,
                                      PasswordEncoder passwordEncoder) {

        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());

        String password = passwordEncoder.encode(memberFormDto.getPassword()); //비밀번호 암호화
        member.setPassword(password);
        member.setRole(Role.ADMIN);

        return member;
    }

    /**
    @ManyToMany
    @JoinTable(
            name = "member_role",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<dobin.webproject.entity.Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FreeBoard> freeBoards = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoticeBoard> noticeBoards = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QnaBoard> qnaBoards = new ArrayList<>(); **/

}
