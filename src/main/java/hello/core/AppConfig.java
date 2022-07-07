package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 이 어플리케이션 전체를 설정하고 구성하는 역할
 * 공연으로 따지면 공연 기획자
 *
 * 애플리케이션의 실제 동작에 필요한 구현 객체를 생성함
 * 생성한 객체 인스턴스의 참조를 생성자를 통해서 주입함 (연결함)
 * 구성영역과 사용영역을 구분할 수 있게 됨
 *
 * IoC (Inversion of Control) 제어의 역전
 * 프로그램의 제어 흐름을 직접 제어하는 것이 아니라 외부에서 관리하는 것
 * AppConfig 가 구현 객체들을 생성하고, 어떻게 동작할 것인지 제어함
 * 나머지 OrderServiceImpl 등의 구현 객체들은 자신의 역할만 할 뿐이지 전체 프로그램이 어떻게 돌아가는지 모름
 *
 * 프레임워크 VS 라이브러리
 * - 프레임워크 : 내가 작성한 코드가 외부에 의해서 실행되고 제어됨 (ex. JUnit (@Test))
 * - 라이브러리 : 내가 작성한 코드가 직접 제어의 흐름을 담당 (ex. JSON 형식으로 데이터 변경을 위해 내가 호출)
 *
 * 의존관계
 * - 정적인 객체 인스턴스 의존 관계 : 소스 상 import 를 통해서 파악 가능
 * - 동적인 객체 인스턴스 의존 관계 : DI (Dependency Injection)
 *  애플리케이션 실행 시점 (런타임) 에 외부에서 실제 구현 객체를 생성하고 클라이언트에 전달해서 클라이언트와 서버의 실제 의존관계가 연결되는 것
 *  의존관계 주입을 사용하면 클라이언트 코드를 변경하지 않고, 클라이언트가 호출하는 대상의 타입 인스턴스 변경 가능 (= 사용영역 변경 없이)
 *  정적인 클래스 의존관계 변경 X 인 상태에서 동적인 객체 인스턴스 의존관계 쉽게 변경 가능
 *
 * DI 컨테이너 (= IOC 컨테이너)
 * AppConfig 처럼 객체를 생성하고 관리하면서 의존관계를 연결해주는 것
 * 
 */

@Configuration /* 애플리케이션의 설정 정보 */
public class AppConfig {

    /**
     * 역할에 따른 구현을 보여주지 못함
     * 중복이 있음
     */

    @Bean /* 스프링 컨테이너에 등록 */
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
        // MemoryMemberRepository 를 사용하는 (주입한) MemberServiceImpl 객체를 반환할거야
        // memberRepository 를 사용하는 MemberServiceImpl 객체를 반환할거야
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    /**
     * 역할에 따른 구현
     * 메소드명과 RETURN 타입을 보면 역할을 확인할 수 있음
     */
    @Bean
    public MemoryMemberRepository memberRepository() {
        return new MemoryMemberRepository(); // 나중에 DB 로 바뀌게 되면, 여기만 바꾸면 됨
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        // return new FixDiscountPolicy(); // 나중에 할인 정책이 바뀌게 되면, 여기만 바꾸면 됨
        return new RateDiscountPolicy();
    }

}
