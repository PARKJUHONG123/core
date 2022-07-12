package hello.core.singleton;

public class SingletonService {
    private static final SingletonService instance = new SingletonService();
    // static 으로 선언 시 클래스 단위로 생성되기 때문에 하나만 생성 가능함
    // 자바가 컴파일 당시에 static 영역에 instance 를 미리 하나 생성

    // 이 객체 인스턴스가 필요하면 오직 getInstance() 메소드를 통해서만 조회할 수 있음
    // 이 메소드를 호출하면 항상 같은 인스턴스 반환
    public static SingletonService getInstance() {
        return instance;
    }

    // private 으로 생성자를 선언하면, 외부에서는 생성이 불가능해짐
    // 혹시라도 외부에서 new 키워드로 객체 인스턴스가 생성되는 것을 막을 수 있음
    private SingletonService() { }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }

}
