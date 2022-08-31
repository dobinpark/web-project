package dobin.webproject.service;

import dobin.webproject.dto.OrderDto;
import dobin.webproject.dto.OrderHistDto;
import dobin.webproject.dto.OrderItemDto;
import dobin.webproject.entity.*;
import dobin.webproject.repository.ItemImgRepository;
import dobin.webproject.repository.ItemRepository;
import dobin.webproject.repository.MemberRepository;
import dobin.webproject.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    // 1-1. 주문
    public Long order(OrderDto orderDto, String email) {
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new); // 1-2. 주문서에 상품 아이디로 해당 상품을 조회한다.
        Member member = memberRepository.findByEmail(email); // 1-3. 현재 로그인한 회원의 이메일 정보를 이용해서 회원 정보를 가져온다.

        List<OrderItem> orderItemList = new ArrayList<>(); // 1-4. 주문 목록 리스트 객체 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount()); // 1-5. 1-2에서 조회한 주문서에 적혀 있는 item 정보를 가져와 저장한 item Entity와 주문서에 적혀 있는 상품 수를 가지고 주문아이템 엔티티를 만든다.
        orderItemList.add(orderItem); // 1-6. 주문 목록 리스트에 1-5에서 만든 주문아이템 엔티티를 추가

        Order order = Order.createOrder(member, orderItemList); // 1-7. 회원 정보와 1-5, 1-6에서 만든 주문 상품 리스트 정보를 이용해 주문 엔티티를 생성
        orderRepository.save(order); // 1-8. 저장(영속화) 후 주문 id return

        return order.getId();
    }

    // 2-1. 주문 목록 조회
    @Transactional(readOnly = true) // 2-2. 성능 향상을 위해 설정
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {
        // 2-3. 유저의 아이디와 페이징 조건을 이용하여 주문 목록을 조회
        List<Order> orders = orderRepository.findOrders(email, pageable);
        // 2-4. 유저의 주문 총 개수를 구합니다.
        Long totalCount = orderRepository.countOrder(email);
        // 2-5. 반환할 orderHistDto 리스트
        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        // 2-6. 주문 리스트를 돌면서
        for (Order order : orders) {
            // 2-7. 주문서(Order)를 바탕으로 구매이력 페이지에 전달할 주문목록을 조회해서 OrderHistDto로 받아온다.
            OrderHistDto orderHistDto = new OrderHistDto(order);

            // 2-8. 주문목록 리스트 객체를 만들어 주문서(Order)에서 주문목록을 가져와서 세팅
            List<OrderItem> orderItems = order.getOrderItems();

            // 2-9. 주문서(Order)에서 가져온 주문목록을 향상된 for문으로 돌린다.
            for (OrderItem orderItem : orderItems) {
                // 2-10. 주문서에 등록된 아이템의 아이디와 대표이미지인지 알려주는 "Y"를 가지고 itemImgRepository에서 대표 이미지를 가져온다
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(orderItem.getItem().getId(), "Y");

                // 2-11. 주문목록과 대표이미지 URL로 주문 목록 DTO 객체를 만든다.
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());

                // 2-12. 2-11에서 만든 주문 목록 DTO 객체를 주문 목록 페이지로 넘겨줄때 사용할 orderHistDto에 세팅.
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            // 2-13. 반환할 orderHistDto 리스트에 하나씩 세팅.
            orderHistDtos.add(orderHistDto);
        }
        // 2-14. 페이지 구현객체를 생성하여 위에서 생성한 객체들을 가지고 뷰에 넘겨준다.
        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    // 3-1. 현재 로그인한 사용자와 주문 데이터를 생성한 사용자가 같은지 검사를 함.
    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email) {
        // 3-2. 현재 로그인한 사용자를 가져옴.
        Member curMember = memberRepository.findByEmail(email);

        // 3-3. 주문 아이디로 주문 엔티티를 가져온다.
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        // 3-4. 주문 엔티티에서 해당 주문을 한 사용자를 가져온다.
        Member saveMember = order.getMember();

        // 3-5. 현재 로그인한 사용자와 주문을 한 사용자가 동일한지 비교한다.
        if (!StringUtils.equals(curMember.getEmail(), saveMember.getEmail())) {
            return false;
        }

        return true;
    }

    // 4-1. 주문 취소
    public void cancelOrder(Long orderId) {
        // 4-2. 전달받은 orderId로 주문 엔티티를 가져온다.
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        // 4-3. 주문 취소.
        order.cancelOrder();
    }

    // 5-1. 상품 주문 2 - 장바구니
    public Long orders(List<OrderDto> orderDtoList, String email) {
        // 5-2. 현재 로그인한 사용자를 가져온다.
        Member member = memberRepository.findByEmail(email);
        // 5-3. 장바구니에 있는 주문 상품을 담을 List형 객체 생성
        List<OrderItem> orderItemList = new ArrayList<>();

        // 5-3. 주문상품 목록 리스트를 만든다.
        for (OrderDto orderDto : orderDtoList) {
            // 5-4. 주문서에 적혀 있는 상품 아이디로 해당 상품 엔티티를 만든다.
            Item item = itemRepository.findById(orderDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);

            // 5-5. 5-4에서 조회한 주문서에 적혀 있는 item 정보를 가져와 저장한 item Entity와 주문서(orderDto)에 적혀 있는 상품 수를 가지고 주문아이템(OrderItem) 엔티티를 만든다.
            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());

            // 5-6. 주문 목록 리스트에 5-5에서 만든 주문아이템 엔티티를 추가
            orderItemList.add(orderItem);
        }

        // 5-7. 회원 정보와 1-5, 1-6에서 만든 주문 상품 리스트 정보를 이용해 주문 엔티티를 생성
        Order order = Order.createOrder(member, orderItemList);

        // 5-8. 저장(영속화) 후 주문 id return
        orderRepository.save(order);

        return order.getId();
    }
}
