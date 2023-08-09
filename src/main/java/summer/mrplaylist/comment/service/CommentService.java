package summer.mrplaylist.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import summer.mrplaylist.comment.dto.CommentForm;
import summer.mrplaylist.comment.model.Comment;
import summer.mrplaylist.comment.repository.CommentRepository;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.playlist.model.Playlist;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final Member member;
    private final Playlist playlist;

    /**
     * 댓글 달기
     */
    @Transactional
    public Comment create(CommentForm commentForm, Member member, Playlist playlist){
        Comment comment = Comment.createComment(commentForm, member, playlist);
        return commentRepository.save(comment);
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public Long update(Long commentId, CommentForm commentForm){
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.get().update(commentForm.getContent());
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
