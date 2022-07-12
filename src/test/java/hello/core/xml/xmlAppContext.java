package hello.core.xml;

import hello.core.member.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class xmlAppContext {

    @Test
    void xmlAppContext() {
        /**
         * GenericXmlApplicationContext 는 XmlBeanDefinitionReader 를 사용해서 appConfig.xml 를 읽고, BeanDefinition 을 생성
         */
        ApplicationContext ac = new GenericXmlApplicationContext("appConfig.xml");
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        assertThat(memberService).isInstanceOf(MemberService.class);
    }
}
