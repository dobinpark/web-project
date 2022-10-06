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

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    private String birthDate;

    private String phoneNm;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDto memberFormDto,
                                      PasswordEncoder passwordEncoder) {

        Member member = new Member();
        member.setEmail(memberFormDto.getEmail());
        String password = passwordEncoder.encode(memberFormDto.getPassword()); //비밀번호 암호화
        member.setPassword(password);
        member.setName(memberFormDto.getName());
        member.setBirthDate(memberFormDto.getBirthDate());
        member.setPhoneNm(memberFormDto.getPhoneNm());
        member.setAddress(memberFormDto.getAddress());
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
