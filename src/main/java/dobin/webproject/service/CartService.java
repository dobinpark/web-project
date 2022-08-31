package dobin.webproject.service;

import dobin.webproject.dto.CartDetailDto;
import dobin.webproject.dto.CartItemDto;
import dobin.webproject.dto.CartOrderDto;
import dobin.webproject.dto.OrderDto;
import dobin.webproject.entity.Cart;
import dobin.webproject.entity.CartItem;
import dobin.webproject.entity.Item;
import dobin.webproject.entity.Member;
import dobin.webproject.repository.CartItemRepository;
import dobin.webproject.repository.CartRepository;
import dobin.webproject.repository.ItemRepository;
import dobin.webproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService { // 1-1. 장바구니에 상품을 담는 로직 수행하는 서비스

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private  final OrderService orderService;

    // 1-2. 장바구니에 상품을 담는 기능을 수행하는 서비스
    public Long addCart(CartItemDto cartItemDto, String email) {
        // 1-3. 전달 받은 cartItemDto에 저장된 itemId로 해당 item 엔티티 조회
        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        // 1-4. 전달 받은 email 주소로 로그인한 회원 엔티티를 조회
        Member member = memberRepository.findByEmail(email);

        // 1-5. 1-4로 찾은 회원의 id로 Cart 엔티티를 가져온다.
        Cart cart = cartRepository.findByMemberId(member.getId());

        // 1-6. 상품을 처음으로 장바구니에 담을 경우 해당 회원의 장바구니 엔티티를 생성
        if (cart == null) {
            // 1-7. 로그인한 회원 엔티티를 가지고 있는 장바구니 엔티티를 새로 생성함.
            cart = Cart.createCart(member);
            // 1-8. 영속화
            cartRepository.save(cart);
        }

        // 1-9. 회원 아이디와 상품 아이디를 가지고 저장된 CartItem 엔티티를 가져온다.
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if (savedCartItem != null) { // 1-10. 장바구니가 비어 있지 않다면
            // 1-11. 기존 장바구니에 있던 아이템 수량에다가  현재 장바구니에 새로 담을 아이템에 수량을 더해줍니다.
            savedCartItem.addCount(cartItemDto.getCount());

            // 1-12. 그리고 새로 추가한 아이템을 가지고 있는 장바구니에 id를 return
            return savedCartItem.getId();
        } else {
            // 1-13. 처음 장바구니에 담는 경우 1-6, 1-7, 1-8에서 생성한 장바구니를 가지고 CartItem을 만듭니다.
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            // 1-14. 장바구니에 들어갈 상품을 저장(영속화)
            cartItemRepository.save(cartItem);
            // 1-15. 저장된 CartItem ID를 리턴함.
            return cartItem.getId();
        }
    }

    // 2-1. 현재 로그인한 회원의 정보를 이용하여 장바구니에 들어있는 상품을 조회.
    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email) {
        // 2-2. 장바구니에 있는 상품을 담을 CartItemDetailDto 리스트 객체 생성
        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();
        // 2-3. 전달받은 사용자 이메일로 로그인한 사용자 조회.
        Member member = memberRepository.findByEmail(email);
        // 2-4. 현재 로그인한 회원의 장바구니 엔티티를 조회
        Cart cart = cartRepository.findByMemberId(member.getId());
        // 2-5. 장바구니에 상품을 한 번도 안 담았을 경우 장바구니 엔티티가 없으므로 빈 리스트를 반환
        if (cart == null) {
            // 2-6. 빈 장바구니 상품 리스트 리턴함.
            return cartDetailDtoList;
        }

        // 2-7. 장바구니에 담겨있는 상품 정보를 조회
        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());
        return cartDetailDtoList;
    }

    // 3-1. 현재 로그인한 회원과 해당 장바구니 상품을 저장한 회원이 같은지 검사하는 메소드
    @Transactional(readOnly = true) // 3-7. 성능 향상을 위해 select는 readOnly true
    public boolean validateCartItem(Long cartItemId, String email) {
        // 3-2. 전달 받은 email로 현재 로그인한 회원을 조회
        Member curMember = memberRepository.findByEmail(email);
        // 3-3. 전달 받은 cartItemId로 장바구니 아이템 조회
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        // 3-4 장바구니 상품을 저장한 회원을 조회
        Member savedMember = cartItem.getCart().getMember();

        // 3-5. 현재 로그인한 회원과 장바구니 상품을 저장한 회원이 다를 경우 false를, 같으면 true를 반환
        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
            return false;
        }
        // 3-6. 같다면 true return
        return true;
    }

    // 4-1. 장바구니에 담겨있는 상품의 수량을 업데이트 해주는 메소드
    public void updateCartItemCount(Long cartItemId, int count) {
        // 4-2. 전달받은 cartItemId로 장바구니 아이템을 가져온다.
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        // 4-3. 전달받은 변경된 수량을 전달해 update 해준다.
        cartItem.updateCount(count);
    }

    // 5-1. 장바구니에 담겨있는 상품을 삭제해주는 메소드
    public void deleteCartItem(Long cartItemId) {
        // 5-2. 전달받은 cartItemId로 장바구니 아이템을 가져온다.
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        // 5-3. 해당 아이템을 삭제
        cartItemRepository.delete(cartItem);
    }

    // 6-1. 장바구니에 담긴 상품을 주문해주는 메소드
    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email) {
        List<OrderDto> orderDtoList = new ArrayList<>();
        // 6-2. 장바구니 페이지에서 전달받은 주문 상품 번호를 이용하여 주문 로직으로 전달할 orderDto 객체를 만듬
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository
                    .findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);
        }

        // 6-6. 장바구니에 담은 상품을 주문하도록 주문 로직을 호출
        Long orderId = orderService.orders(orderDtoList, email);

        // 6-7. 주문한 상품들을 장바구니에서 제거
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository
                    .findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }
        return orderId;
    }
}
