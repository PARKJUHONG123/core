package hello.core.member;

public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository; // 추상화에 의존

    // 생성자를 통해 어떤 구현 객체가 들어올지 알 수 없음
    // 생성자를 통해서 어떤 구현 객체를 주입할지는 오직 외부 (AppConfig) 에서 결정됨
    // 이제부터 의존관계에 대한 고민은 외부에 맡기고 실행에만 집중하면 됨

    // memberServiceImpl 입장에서 보면 의존관계를 마치 외부에서 주입해주는 것과 같다고 해서
    // DI (Dependency Injection) (의존관계 주입 또는 의존성 주입)

    public MemberServiceImpl(MemberRepository memberRepository) { // 생성자를 통해 객체가 들어가는 [생성자 주입]
        this.memberRepository = memberRepository;
    }


    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
