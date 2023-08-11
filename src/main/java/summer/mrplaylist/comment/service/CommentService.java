package summer.mrplaylist.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import summer.mrplaylist.comment.constant.CommentConst;
import summer.mrplaylist.comment.dto.CommentForm;
import summer.mrplaylist.comment.dto.CommentUpdateForm;
import summer.mrplaylist.comment.model.Comment;
import summer.mrplaylist.comment.repository.CommentRepository;
import summer.mrplaylist.member.constant.Role;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.playlist.model.Playlist;
import summer.mrplaylist.playlist.service.PlaylistService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final PlaylistService playlistService;

	/**
	 * 댓글 달기
	 */
	@Transactional
	public Comment create(CommentForm commentForm) {
		Playlist playlist = playlistService.findPlaylist(commentForm.getPlaylistId());
		Comment comment = Comment.createComment(commentForm, commentForm.getMember(), playlist);
		return commentRepository.save(comment);
	}

	/**
	 * 댓글 수정
	 */
	@Transactional
	public Comment update(CommentUpdateForm updateForm) {
		Comment comment = findComment(updateForm.getCommentId());

		if (isGrantComment(updateForm.getMember(), comment)) {
			comment.update(updateForm.getContent());
		} else {
			throw new IllegalStateException(CommentConst.NOT_AUTHOR);
		}
		return comment;
	}

	/**
	 * 댓글 삭제
	 */
	@Transactional
	public void delete(Long commentId, Member member) {
		Comment comment = findComment(commentId);

		if (isGrantComment(member, comment)) {
			comment.getPlaylist().removeComment(comment);
			commentRepository.deleteById(commentId);
		} else {
			throw new IllegalStateException(CommentConst.NOT_AUTHOR);
		}
	}

	private boolean isGrantComment(Member member, Comment comment) {
		return (member == comment.getMember() || member.getRole() == Role.ADMIN);
	}

	private Comment findComment(Long commentId) {
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException(CommentConst.NOT_FOUND));
		return comment;
	}

}
