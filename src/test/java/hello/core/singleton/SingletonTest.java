package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
         * 싱글톤 패턴
         * 클래스의 인스턴스가 딱 1개만 생성되는 것을 보장하는 디자인 패턴
         * 객체 인스턴스를 2개 이상 생성하지 못하도록 막음
         * 
         * 하지만 싱글톤 패턴의 단점도 많음
         * - 싱글톤 패턴을 구현하는 코드 자체가 많이 들어감
         * - 의존관계상 클라이언트가 구체 코드에 의존해야 함 (DIP 위반 가능성 높음) : appConfig 에서 .getInstance 를 공통적으로 사용하는 등
         * - 클라이언트가 구체 클래스에 의존해서 OCP 원칙을 위반할 가능성 높음
         * - 테스트 어려움, 내부 속성 변경 및 초기화 어려움
         * - private 생성자로 선언되었기 때문에 자식 클래스를 만들기 어려움
         * - 유연성 떨어짐
         * 
         */

    }
    
    @Test
    @DisplayName("싱글톤 패턴을 적용한 객체 사용")
    void singletonServiceTest() {
        // new SingletonService();
        // 컴파일 오류를 통해 생성 불가 java: SingletonService() has private access in hello.core.singleton.SingletonService

        SingletonService singletonService1 = SingletonService.getInstance();
        SingletonService singletonService2 = SingletonService.getInstance();

        // isEqualTo = .equals()
        // isSameAs : ==
        Assertions.assertThat(singletonService1).isSameAs(singletonService2);
    }
}
