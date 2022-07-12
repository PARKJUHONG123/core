package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SingletonTest {
    @Test
    @DisplayName("스프링 없는 순수한 DI 컨테이너")
    void pureContainer() {
        AppConfig appConfig = new AppConfig();

        // 1. 조회 : 호출할 때마다 객체 생성
        MemberService memberService1 = appConfig.memberService();
        // 2. 조회 : 호출할 때마다 객체 생성
        MemberService memberService2 = appConfig.memberService();

        // 호출할 때마다 객체가 생성되므로 JVM 메모리가 버티질 못함
        // memberService1 != memberService2
        Assertions.assertThat(memberService1).isNotSameAs(memberService2);

        /**
         * 스프링 없는 순수한 DI 컨테이너인 AppConfig 는 요청을 할 때마다 객체 생성으로 메모리 낭비가 심함
         * 객체가 딱 1개만 생성되고, 공유하도록 설계가 필요함 (싱글톤 패턴)
         *
         */

    }
    
    @Test
    @DisplayName("싱글톤 패턴을 적용한 객체 사용")
    void singletonServiceTest() {
        // new SingletonService();
        // 컴파일 오류를 통해 생성 불가 java: SingletonService() has private access in hello.core.singleton.SingletonService

        /**
         * 싱글톤 패턴
         * 클래스의 인스턴스가 딱 1개만 생성되는 것을 보장하는 디자인 패턴
         * 객체 인스턴스를 2개 이상 생성하지 못하도록 막음
         *
         * 하지만 싱글톤 패턴의 단점도 많음
         * - 싱글톤 패턴을 구현하는 코드 자체가 많이 들어감
         * - 의존관계상 클라이언트가 구체 코드에 의존해야 함 (DIP 위반 가능성 높음) : appConfig 에서 구체 클래스.getInstance() 를 공통적으로 사용하는 등
         * - 클라이언트가 구체 클래스에 의존해서 OCP 원칙을 위반할 가능성 높음
         * - 테스트 어려움, 내부 속성 변경 및 초기화 어려움
         * - private 생성자로 선언되었기 때문에 자식 클래스를 만들기 어려움
         * - 유연성 떨어짐
         *
         */
        SingletonService singletonService1 = SingletonService.getInstance();
        SingletonService singletonService2 = SingletonService.getInstance();

        // isEqualTo = .equals()
        // isSameAs : ==
        Assertions.assertThat(singletonService1).isSameAs(singletonService2);
    }

    /**
     * 스프링 컨테이너
     * 싱글톤 패턴의 문제점을 해결하면서, 객체 인스턴스를 싱글톤 (1개만 생성) 으로 관리
     *
     * 싱글톤 패턴을 적용하지 않아도, 객체 인스턴스를 싱글톤으로 관리함
     * 스프링 컨테이너는 싱글톤 컨테이너의 역할을 함
     * 싱글톤 객체를 생성하고 관리하는 기능을 싱글톤 레지스트리 라고 함
     *
     * 스프링 컨테이너 덕분에 고객의 요청이 올 때마다 객체를 생성하는 것이 아니라, 이미 만들어진 객체를 공유해서 효율적으로 재사용 가능
     * 스프링의 기본 빈 등록 방식은 싱글톤이지만, 요청할 때마다 새로운 객체를 생성해서 반환하는 기능도 제공함 (빈 스코프)
     * ex. HTTP Request 라이프 사이클에 맞춰서 스프링빈의 라이프 사이클도 맞춰줘야 할 때 (고객이 들어올 때 만들어서 고객이 나갈 때 죽일 경우)
     * ex. HTTP Session 의 라이플 사이클에 맞추거나 등
     */
    @Test
    @DisplayName("스프링 컨테이너와 싱글톤")
    void springContainer() {
        // AppConfig appConfig = new AppConfig();
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        // 1. 조회 : 호출할 때마다 객체 생성
        // MemberService memberService1 = appConfig.memberService();

        // 1. 조회 : 호출할 때마다 [스프링빈] 객체 반환
        MemberService memberService1 = ac.getBean("memberService", MemberService.class);

        // 2. 조회 : 호출할 때마다 객체 생성
        // MemberService memberService2 = appConfig.memberService();

        // 2. 조회 : 호출할 때마다 [스프링빈] 객체 반환
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);

        // memberService1 != memberService2
        Assertions.assertThat(memberService1).isSameAs(memberService2);
    }
    
}
