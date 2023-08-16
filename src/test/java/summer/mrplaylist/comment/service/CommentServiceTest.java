package summer.mrplaylist.comment.service;

import static org.assertj.core.api.Assertions.*;
import static summer.mrplaylist.CreateMethod.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import summer.mrplaylist.comment.constant.CommentConst;
import summer.mrplaylist.comment.dto.CommentForm;
import summer.mrplaylist.comment.dto.CommentUpdateForm;
import summer.mrplaylist.comment.model.Comment;
import summer.mrplaylist.member.constant.Role;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.member.repository.MemberRepository;
import summer.mrplaylist.playlist.dto.PlaylistForm;
import summer.mrplaylist.playlist.model.Playlist;
import summer.mrplaylist.playlist.service.PlaylistService;

@Slf4j
@Transactional
@SpringBootTest
public class CommentServiceTest {

	@Autowired
	CommentService commentService;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	PlaylistService playlistService;

	@Autowired
	EntityManager em;

	@Test
	public void createComment() throws Exception {
		//given
		Member member = getMember();
		member = memberRepository.save(member);

		Playlist playlist = getPlaylist(member);

		CommentForm commentForm = new CommentForm(playlist.getId(), member, "노래가 좋네요");
		//when
		Comment comment = commentService.create(commentForm);
		//then
		assertThat(comment.getPlaylist().getCommentCount()).isEqualTo(1);
		assertThat(comment.getMember().getEmail()).isEqualTo(member.getEmail());
	}

	@Test
	@DisplayName("수정 테스트")
	public void editTest() throws Exception {
		//given
		Member member = getMember();
		member = memberRepository.save(member);

		Playlist playlist = getPlaylist(member);

		CommentForm commentForm = new CommentForm(playlist.getId(), member, "노래가 좋네요");

		Comment comment = commentService.create(commentForm);

		em.clear();
		Member findMember = memberRepository.findByEmail(member.getEmail()).get();
		CommentUpdateForm commentUpdateForm = new CommentUpdateForm(comment.getId(), findMember, "잘못 작성했습니다.");

		//when
		Comment updateComment = commentService.update(commentUpdateForm);

		//then
		assertThat(updateComment.getContent()).isEqualTo(commentUpdateForm.getContent());
	}

	@Test
	@DisplayName("작성자와 다른 사람이 와서 수정")
	public void wrongAuthor() throws Exception {
		//given
		Member member = getMember();
		member = memberRepository.save(member);

		Playlist playlist = getPlaylist(member);

		CommentForm commentForm = new CommentForm(playlist.getId(), member, "노래가 좋네요");

		Comment comment = commentService.create(commentForm);
		Member wrongMember = Member.builder()
			.email("test11@naver.com")
			.password("abc11")
			.nickname("jpop11")
			.profileImg("abcde11")
			.role(Role.USER)
			.build();

		wrongMember = memberRepository.save(wrongMember);
		CommentUpdateForm commentUpdateForm = new CommentUpdateForm(comment.getId(), wrongMember, "그런가요...?");
		//when
		assertThatThrownBy(() -> commentService.update(commentUpdateForm))
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining(CommentConst.NOT_AUTHOR);

	}

	private Playlist getPlaylist(Member member) {
		PlaylistForm playlistForm = getPlaylistForm(member);
		playlistForm.setMusicFormList(getMusicFormList());

		Playlist playlist = playlistService.create(playlistForm);
		return playlist;
	}
}
