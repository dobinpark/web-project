package dobin.webproject.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@RequiredArgsConstructor
public class MemberFormDto {

    private Long id;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력하여 주십시오.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력하여 주십시오.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    private String birthDate;

    private String phoneNm;

    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String address;
}