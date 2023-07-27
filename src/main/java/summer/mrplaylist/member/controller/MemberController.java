package summer.mrplaylist.member.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import summer.mrplaylist.member.dto.AddMemberRequestDto;
import summer.mrplaylist.member.dto.UpdateMemberRequestDto;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.member.service.MemberService;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    @PostMapping
    public ResponseEntity<String> signUp(@RequestBody AddMemberRequestDto requestDto)
    {
        memberService.join(Member.createMember(requestDto));
        return ResponseEntity.ok("가입 완료");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id,
                                         @RequestBody UpdateMemberRequestDto requestDto) {
        memberService.update(id, requestDto);
        return ResponseEntity.ok("회원 수정");
    }

    @PostMapping("/auth")
    public ResponseEntity<String> emailAuth(String email) {
        return null;
    }

}
