package wooteco.subway.member.ui;

import java.net.URI;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.AuthMember;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest request) {
        try {
            MemberResponse member = memberService.createMember(request);
            return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(
        @AuthenticationPrincipal AuthMember authMember) {
        if (!authMember.isLoggedIn()) {
            throw new AuthorizationException("로그인을 해야합니다.");
        }
        MemberResponse member = memberService.findMember((LoginMember) authMember);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/members/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine(
        @AuthenticationPrincipal AuthMember authMember, @RequestBody MemberRequest param) {
        if (!authMember.isLoggedIn()) {
            throw new AuthorizationException("로그인을 해야합니다.");
        }
        memberService.updateMember((LoginMember) authMember, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/me")
    public ResponseEntity<MemberResponse> deleteMemberOfMine(
        @AuthenticationPrincipal AuthMember authMember) {
        if (!authMember.isLoggedIn()) {
            throw new AuthorizationException("로그인을 해야합니다.");
        }
        memberService.deleteMember((LoginMember) authMember);
        return ResponseEntity.noContent().build();
    }
}
