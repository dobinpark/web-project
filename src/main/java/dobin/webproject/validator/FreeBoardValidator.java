package dobin.webproject.validator;

import dobin.webproject.entity.board.FreeBoard;
import dobin.webproject.entity.board.NoticeBoard;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.thymeleaf.util.StringUtils;

@Component
public class FreeBoardValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return FreeBoard.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        FreeBoard b = (FreeBoard) obj;
        if(StringUtils.isEmpty(b.getContent())) {
            errors.rejectValue("content", "key", "내용을 입력하세요");
        }
    }
}