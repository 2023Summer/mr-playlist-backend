package summer.mrplaylist.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import summer.mrplaylist.comment.dto.CommentForm;
import summer.mrplaylist.comment.model.Comment;
import summer.mrplaylist.comment.repository.CommentRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * 댓글 달기
     */
    @Transactional
    public Comment create(CommentForm commentForm){
        Comment comment = Comment.createComment(commentForm);
        return commentRepository.save(comment);
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public Long update(Long commentId, CommentForm commentForm){
        Comment comment = commentRepository.getReferenceById(commentId);
        comment.update(commentForm.getContent());
        return commentId;
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public void deleteComment(Long commentId){
        commentRepository.deleteById(commentId);
    }

}
