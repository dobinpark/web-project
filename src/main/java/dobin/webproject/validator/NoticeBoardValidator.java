package dobin.webproject.validator;

import dobin.webproject.entity.board.NoticeBoard;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.thymeleaf.util.StringUtils;

@Component
public class NoticeBoardValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return NoticeBoard.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        NoticeBoard b = (NoticeBoard) obj;
        if(StringUtils.isEmpty(b.getContent())) {
            errors.rejectValue("content", "key", "내용을 입력하세요");
        }
    }
}