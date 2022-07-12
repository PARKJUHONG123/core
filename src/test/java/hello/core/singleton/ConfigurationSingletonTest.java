package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberRepository;
import hello.core.member.MemberServiceImpl;
import hello.core.order.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class ConfigurationSingletonTest {

    @Test
    void configurationTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);
        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);

        MemberRepository memberRepository1 = memberService.getMemberRepository();
        MemberRepository memberRepository2 = orderService.getMemberRepository();

        System.out.println("memberService -> memberRepository : " + memberRepository1);
        System.out.println("orderService -> memberRepository : " + memberRepository2);
        System.out.println("memberRepository : " + memberRepository);

        Assertions.assertThat(memberService.getMemberRepository()).isSameAs(memberRepository);
        Assertions.assertThat(orderService.getMemberRepository()).isSameAs(memberRepository);

        /**
         * 모두 같은 인스턴스를 반환하는 것을 확인할 수 있음
         */
    }

    @Test
    void configurationDeep() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        AppConfig bean = ac.getBean(AppConfig.class);
        System.out.println("bean = " + bean.getClass());

        /**
         * 순수한 클래스라면 class hello.core.AppConfig 라는 정보가 출력되어야 하는데
         * EnhancerBySpringCGLIB 이라는 문구가 class 정보 뒤에 붙여서 나옴
         *
         * 내가 만든 클래스가 아니라, 스프링이 CGLIB 이라는 바이트코드 조작 라이브러리를 사용해서
         * AppConfig 클래스를 상속받은 임의의 다른 클래스를 만들고, 그 다른 클래스를 스프링 빈으로 등록한 결과라고 할 수 있음
         *
         * AppConfig@CGLIB 를 스프링 컨테이너 내 appConfig 라는 이름의 스프링 빈으로 등록함
         * AppConfig@CGLIB 은 AppConfig 의 자식 타입이므로, AppConfig 타입으로 조회할 수 있음
         * 
         * AppConfig@CGLIB 예상 코드 중 일부
         * @Bean
         * public MemberRepository memberRepository() { // AppConfig 에서 오버라이딩 됨
         *      if (memoryMemberRepository 가 이미 스프링 컨테이너에 등록되어 있으면) {
         *          return 스프링 컨테이너에서 찾아서 반환;
         *      }
         *      else { // 스프링 컨테이너에 없으면
         *          내가 만든 기존 로직을 호출해서 MemoryMemberRepository 를 생성하고 스프링 컨테이너에 등록
         *          return 반환;
         *      }
         * }
         *
         */


    }

}
