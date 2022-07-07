package hello.core;

import hello.core.discount.FixDiscountPolicy;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;

/**
 * 이 어플리케이션 전체를 설정하고 구성하는 역할
 * 공연으로 따지면 공연 기획자
 *
 * 애플리케이션의 실제 동작에 필요한 구현 객체를 생성함
 * 생성한 객체 인스턴스의 참조를 생성자를 통해서 주입함 (연결함)
 *
 */
public class AppConfig {

    public MemberService memberService() {
        return new MemberServiceImpl(new MemoryMemberRepository());
        // MemoryMemberRepository 를 사용하는 (주입한) MemberServiceImpl 객체를 반환할거야
    }

    public OrderService orderService() {
        return new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());
    }
}
