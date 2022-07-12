package hello.core.beanDefinition;

import hello.core.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * BeanDefinition 을 직접 생성해서 스프링 컨테이너에 등록할 수도 있지만, 직접 정의하거나 사용하는 일은 거의 없음
 * 스프링이 다양한 형태의 설정 정보를 BeanDefinition 으로 추상화해서 사용
 */

public class BeanDefinitionTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
    GenericXmlApplicationContext gc = new GenericXmlApplicationContext("appConfig.xml");

    // [빈 정의 방법 1] AppConfig 에 있는 memberService() 와 같은 펙토리 메소드를 통해서 팩토리 빈으로 한번 더 감싸서 등록하는 방법
    @Test
    @DisplayName("빈 설정 메타정보 확인")
    void findApplicationBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                System.out.println("beanDefinitionName = " + beanDefinitionName +
                                    "\nbeanDefinition = " + beanDefinition);
            }
        }
    }

    // [빈 정의 방법 2] appConfig.xml 처럼 직접 명시해줘서 스프링 빈을 등록하는 방법
    @Test
    @DisplayName("XML 기반 빈 설정 메타정보 확인")
    void findApplicationBeanXml() {
        String[] beanDefinitionNames = gc.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = gc.getBeanDefinition(beanDefinitionName);

            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                System.out.println("beanDefinitionName = " + beanDefinitionName +
                        "\nbeanDefinition = " + beanDefinition);
            }
        }
    }

}
