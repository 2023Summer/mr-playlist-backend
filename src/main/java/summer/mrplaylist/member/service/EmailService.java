package summer.mrplaylist.member.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import summer.mrplaylist.common.service.RedisService;
import summer.mrplaylist.member.repository.MemberRepository;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class EmailService {
    private final RedisService redisService;




    public String createAuthNumber(String email) {
        String authNumber = UUID.randomUUID().toString().substring(0,5); // 6자리 랜덤 인증번호 생성
        redisService.setDataWithExpire(email, authNumber, 300L); // 유효시간 5분
        return authNumber;
    }

    public boolean checkAuthNumber(String email, String userAuthNumber) {
        String authNumber = redisService.getData(email);
        if(userAuthNumber.equals(authNumber)) {
            return true;
        }
        else {
            return false;
        }
    }

}
