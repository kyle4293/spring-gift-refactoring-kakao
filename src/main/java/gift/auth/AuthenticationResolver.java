package gift.auth;

import gift.member.Member;
import gift.member.MemberRepository;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationResolver {
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    public AuthenticationResolver(JwtProvider jwtProvider, MemberRepository memberRepository) {
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
    }

    public Member extractMember(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new UnauthorizedException("인증 정보가 유효하지 않습니다.");
        }
        final String token = authorization.substring(7);
        final String email;
        try {
            email = jwtProvider.getEmail(token);
        } catch (Exception e) {
            throw new UnauthorizedException("인증 정보가 유효하지 않습니다.");
        }
        // DB 조회 실패 등 시스템 오류는 의도적으로 catch하지 않아 500으로 전파
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new UnauthorizedException("인증 정보가 유효하지 않습니다."));
    }
}
