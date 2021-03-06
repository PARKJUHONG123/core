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
 * 
 * XML 로도 스프링빈 설정을 할 수도 있고, 자바 코드로도 스프링빈 설정 가능
 * -> BeanDefinition 이라는 추상화가 있기 때문에 가능
 * 스프링은 BeanDefinition 만 알면 되기 때문에 (= BeanDefinition 에만 의존) 사용자가 스프링이 제공하는 BeanDefinition 인터페이스 정의에 맞게 설정만 해주면 XML, 자바 외에 다른 양식도 가능
 * BeanDefinition 을 빈 설정 메타정보라고 하고, @Bean, <bean> 당 각각 하나씩 메타 정보 생성
 * 스프링 컨테이너는 이 메타정보를 기반으로 스프링빈 생성
 */

/**
 * @Configuration
 * 애플리케이션의 설정 정보
 * 
 */
@Configuration 
public class AppConfig {

    /**
     * @Bean memberService -> new MemoryMemberRepository()
     * @Bean orderService -> new MemoryMemberRepository()
     * 
     * 코드만 보면 MemoryMemberRepository 객체가 싱글톤 패턴을 어기고 각각 2번 생성된다고 보여짐
     * call AppConfig.memberRepository 라는 로그가 3번은 찍혀야 할 것 같은데, 1번 밖에 호출되지 않음
     *
     * @Configuration
     * 스프링 컨테이너는 싱글톤 레지스트리 이기 때문에 스프링 싱글톤이 되도록 보장해줘야 하는데, 자바 코드까지 스프링이 어떻게 하기는 어려움
     * 바이트코드를 조작해서 AppConfig@CGLIB 이라는 자식 객체가 생성되는데, 이 자식 객체는 기존에 생성된 스프링 빈이 있으면 그것을 반환하고, 아니면 스프링 빈을 등록함
     * 
     * 만약에 @Configuration 이 없게 되면 스프링빈은 생성이 되지만, 싱글톤이 깨지게 됨
     * 각각의 서비스가 각각의 객체를 생성하게 됨 
     * 그래서 call AppConfig.memberRepository 라는 로그가 3번이 찍히게 됨
     * (@Autowired 를 통해서 자동으로 의존 관계를 주입해서 해결이 되긴 하지만 차후 학습 예정)
     * 
     */

    @Bean /* 스프링 컨테이너에 등록 */
    public MemberService memberService() {
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
        // MemoryMemberRepository 를 사용하는 (주입한) MemberServiceImpl 객체를 반환할거야
        // memberRepository 를 사용하는 MemberServiceImpl 객체를 반환할거야
    }

    @Bean
    public OrderService orderService() {
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    /**
     * 역할에 따른 구현
     * 메소드명과 RETURN 타입을 보면 역할을 확인할 수 있음
     */
    @Bean
    public MemoryMemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository(); // 나중에 DB 로 바뀌게 되면, 여기만 바꾸면 됨
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        // return new FixDiscountPolicy(); // 나중에 할인 정책이 바뀌게 되면, 여기만 바꾸면 됨
        return new RateDiscountPolicy();
    }

}
