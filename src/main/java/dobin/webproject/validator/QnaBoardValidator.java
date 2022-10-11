package dobin.webproject.validator;

import dobin.webproject.entity.board.QnaBoard;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.thymeleaf.util.StringUtils;

@Component
public class QnaBoardValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return QnaBoard.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        QnaBoard b = (QnaBoard) obj;
        if(StringUtils.isEmpty(b.getContent())) {
            errors.rejectValue("content", "key", "내용을 입력하세요");
        }
    }
}