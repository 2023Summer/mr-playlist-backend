package summer.mrplaylist.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import summer.mrplaylist.member.model.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {
    boolean existsByEmail(String email);
}
