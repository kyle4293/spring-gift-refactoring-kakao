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
        try {
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                throw new UnauthorizedException("인증 정보가 유효하지 않습니다.");
            }
            final String token = authorization.substring(7);
            final String email = jwtProvider.getEmail(token);
            return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("인증 정보가 유효하지 않습니다."));
        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new UnauthorizedException("인증 정보가 유효하지 않습니다.");
        }
    }
}
