package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {
    public static void main(String[] args) {
//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService(); // new MemberServiceImpl();

        /**
         * ApplicationContext 이 컨테이너로 생각하면 됨 (스프링 컨테이너)
         * 기존에는 개발자가 AppConfig 를 사용해서 직접 객체를 생성하고 DI 했지만, 스프링 컨테이너를 사용해서 등록하기 위함
         * 스프링이 @Bean 으로 명시된 객체들을 관리함
         * 
         * 스프링 컨테이너는 @Configuration 이 붙은 AppConfig 를 설정(구성) 정보로 사용함
         * 여기서 @Bean 이라고 적힌 메소드를 모두 호출해서 반환된 객체를 스프링 컨테이너에 등록함
         * 스프링 컨테이너에 등록된 객체를 스프링 빈 이라고 함
         *
         * 스프링 빈은 @Bean 이 붙은 메소드의 명칭을 스프링 빈의 이름으로 설정 [@Bean(name = "") 으로 수정 가능하긴 함]
         */

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class); // 어노테이션 기반으로 선언된 Bean 을 스프링 컨테이너에 넣어서 관리함
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class); // 찾고 싶은 스프링빈의 이름과 구조를 전달해서, 스프링 컨테이너에서 그에 해당되는 객체를 찾아서 반환함

        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findMember = memberService.findMember(1L);
        System.out.println("new member = " + member.getName());
        System.out.println("find member = " + findMember.getName());
    }
}
