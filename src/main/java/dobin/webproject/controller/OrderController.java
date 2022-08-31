package dobin.webproject.controller;

import dobin.webproject.dto.OrderDto;
import dobin.webproject.dto.OrderHistDto;
import dobin.webproject.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto,
                                              BindingResult bindingResult, Principal principal) {

        // 1-2. 주문 정보를 받는 orderDto 객체에 데이터 바인딩 시 에러가 있는지 검사.
        if (bindingResult.hasErrors()) {
            // 1-3. 에러가 있을 경우 Map에 담아서 에러 코드와 함께 return
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        // 1-4. principal 객체로 현재 로그인한 회원 아이디를 가져온다.
        String email = principal.getName();
        Long orderId;

        try {
            // 1-5. 화면으로부터 넘어온 주문 정보와 회원의 이메일 정보로 주문 로직 호출 성공하면 주문 id와 OK HttpStatus 코드를 result에 담는다.
            orderId = orderService.order(orderDto, email);
        } catch(Exception e) {
            // 1-6. 1-5에서 실패했다면 BAD_REQUEST 상태코드를 result에 담는다.
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    // 1-1. 주문 내역 조회
    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model) {
        // 1-2. 한 번에 가지고 올 주문의 개수를 4개로 설정하고 Pageable 객체 생성 /
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);

        // 1-3. 현재 로그인한 회원의 이메일과 페이징 객체를 파라미터로 전달하여 화면에 전달할 주문 목록 데이터를 리턴 받는다.
        Page<OrderHistDto> ordersHistDtoList = orderService.getOrderList(principal.getName(), pageable);

        // 1-4. 2-3을 orders란 이름으로 Model에 담는다.
        model.addAttribute("orders", ordersHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);
        return "order/orderHist";
    }

    // 2-1. 주문 취소 API
    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, Principal principal) {

        // 2-2. 주문자와 로그인한 사람을 비교해서 로그인한 사람이 주문자가 아니면
        if (!orderService.validateOrder(orderId, principal.getName())) {
            return new ResponseEntity("주문 취소 권한이 없습니다.",
                    HttpStatus.FORBIDDEN);
        }
        /* 주문 취소 로직을 호출 */
        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }
}
