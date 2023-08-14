package smu.likelion.jikchon.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import smu.likelion.jikchon.domain.Cart;
import smu.likelion.jikchon.domain.Product;
import smu.likelion.jikchon.domain.Purchase;
import smu.likelion.jikchon.domain.member.Member;
import smu.likelion.jikchon.dto.cart.CartRequestDto;
import smu.likelion.jikchon.dto.purchase.PurchaseRequestDto;
import smu.likelion.jikchon.exception.CustomNotFoundException;
import smu.likelion.jikchon.exception.ErrorCode;
import smu.likelion.jikchon.repository.CartRepository;
import smu.likelion.jikchon.repository.MemberRepository;
import smu.likelion.jikchon.repository.ProductRepository;
import smu.likelion.jikchon.repository.PurchaseRepository;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final LoginService loginService;


    public List<Purchase> purchaseList(Member member) {
        return purchaseRepository.findByMemberId(member.getId());
    }

    public void deleteProduct(Long purchaseId) {
        Member member = memberRepository.findById(loginService.getLoginMemberId()).orElseThrow(() -> {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER);
        });
        Purchase purchase = purchaseRepository.findById(purchaseId).orElseThrow(() -> {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_PRODUCT);
        });
        Product product = purchase.getProduct(); //구매 목록에서의 제품
        Long currentQuantity = product.getQuantity(); //총 수량
        Cart cart = cartRepository.findById(purchaseId).orElseThrow(() -> {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_PRODUCT);
        });
        if (member.equals(purchase.getMember())) {
            purchaseRepository.delete(purchase);
            product.setQuantity(currentQuantity + 1);
            productRepository.save(product);
            if (Objects.equals(cart.getId(), purchaseId)) { //장바구니에 있는 경우
                //장바구니의 개수를 1 증가
//                cart.setQuantity(cart.getQuantity() + 1);
            } else {
                cartRepository.save(cart);
            }
        }

    }

}
