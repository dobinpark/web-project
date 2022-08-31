package dobin.webproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    @GetMapping("/introduction")
    public String Introduction() {
        return "company/introduction";
    }

    @GetMapping("/maps")
    public String Maps() {
        return "company/maps";
    }
}
