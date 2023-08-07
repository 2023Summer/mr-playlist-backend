package summer.mrplaylist.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import summer.mrplaylist.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
