package summer.mrplaylist.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import summer.mrplaylist.comment.dto.CommentForm;
import summer.mrplaylist.comment.model.Comment;
import summer.mrplaylist.comment.repository.CommentRepository;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.playlist.model.Playlist;

import java.util.NoSuchElementException;
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
    public Comment update(Long commentId, CommentForm commentForm){
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글은 수정이 불가능합니다."));
        comment.update(commentForm.getContent());
        return comment;
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public void delete(Long commentId){
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글은 삭제 불가능합니다."));
        comment.getPlaylist().removeComment(comment);
        commentRepository.deleteById(commentId);
    }

}
