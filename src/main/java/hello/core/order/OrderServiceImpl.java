package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;


/**
 * 이 클래스를 배우가 하는 역할을 정의한 문서라고 하면
 * 여기서 직접 배우가 누가 되어야 하는지 지정하는 것은 공연 기획자의 역할인데
 * 정작 배우가 직접 누가 출현을 하는지 결정하고 있으므로, 관심사가 분리되어야 함
 */
public class OrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository; // = new MemoryMemberRepository();
    private final DiscountPolicy discountPolicy; // 인터페이스에만 의존하도록 코드 변경을 했지만 NPE 발생

    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
    // 이 문제를 해결하려면 누군가가 클라이언트인 OrderServiceImpl 에 DiscountPolicy 의 구현 객체를 대신 생성하고 주입해줘야 함



    /**
    private final DiscountPolicy discountPolicy = new FixDiscountPolicy(); // 마치 배역에 어떤 배우가 해야하는지 정하는 코드
    // DIP 위반 (구현 클래스에도 의존)
    // OCP 위반 (FixDiscountPolicy 를 RateDiscountPolicy 로 변경하는 순간 OrderServiceImpl.java 의 코드도 변경해줘야 함)
    */

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }

}
