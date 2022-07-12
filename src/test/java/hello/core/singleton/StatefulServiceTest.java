package hello.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {

    @Test
    void statefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        // ThreadA : A 사용자 10000원 주문
        statefulService1.order("userA", 10000);

        // ThreadB : B 사용자 20000원 주문
        statefulService2.order("userB", 20000);

        // ThreadA : A 사용자 주문 금액 조회
        Assertions.assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    @Test
    void improvedStatefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        // ThreadA : A 사용자 10000원 주문
        int aPrice = statefulService1.improvedOrder("userA", 10000);

        // ThreadB : B 사용자 20000원 주문
        int bPrice = statefulService2.improvedOrder("userB", 20000);

        // ThreadA : A 사용자 주문 금액 조회
        Assertions.assertThat(aPrice).isEqualTo(10000);
    }


    static class TestConfig {
        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }

}