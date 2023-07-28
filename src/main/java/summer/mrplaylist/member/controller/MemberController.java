package summer.mrplaylist.member.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import summer.mrplaylist.member.dto.AddMemberRequestDto;
import summer.mrplaylist.member.dto.LoginMemberRequestDto;
import summer.mrplaylist.member.dto.UpdateMemberRequestDto;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.member.service.EmailService;
import summer.mrplaylist.member.service.MemberService;


@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;
    @PostMapping("/join")
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
    public ResponseEntity<Boolean> sendAuthEmail(@RequestParam String email) {
        try {
            emailService.sendAuthEmail(email);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(true);
    }

    @GetMapping("/auth")
    public ResponseEntity<Boolean> checkAuth(@RequestParam String email,
                                             @RequestParam(name = "authnum") String userAuthNumber) {
        if(emailService.checkAuthNumber(email, userAuthNumber)){
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginMemberRequestDto requestDto) {
        return ResponseEntity.ok(memberService.login(requestDto));
    }

}
