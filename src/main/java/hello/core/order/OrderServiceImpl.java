package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository = new MemoryMemberRepository();

    /**
    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    // DIP 위반 (구현 클래스에도 의존)
    // OCP 위반 (FixDiscountPolicy 를 RateDiscountPolicy 로 변경하는 순간 OrderServiceImpl.java 의 코드도 변경해줘야 함)
    */
    private DiscountPolicy discountPolicy; // 인터페이스에만 의존하도록 코드 변경을 했지만 NPE 발생
    // 이 문제를 해결하려면 누군가가 클라이언트인 OrderServiceImpl 에 DiscountPolicy 의 구현 객체를 대신 생성하고 주입해줘야 함

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
