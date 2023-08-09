package summer.mrplaylist.comment.model;

import org.junit.jupiter.api.Test;
import summer.mrplaylist.comment.dto.CommentForm;
import summer.mrplaylist.member.model.Member;
import summer.mrplaylist.playlist.model.Playlist;

import static org.assertj.core.api.Assertions.*;
public class CommentTest {

    @Test
    public void createComment() throws Exception {

        CommentForm commentForm = CommentForm.builder()
                .content("좋아요.")
                .playlistId(1L)
                .build();

        Member member = Member.builder()
                .id(1L)
                .nickname("테스트")
                .build();
        Playlist playlist = Playlist.builder()
                .id(1L)
                .name("플리이름")
                .commentCount(0)
                .build();


        Comment comment = Comment.createComment(commentForm, member, playlist);
        playlist.addComment(comment);


        // 컨텐츠 내용이 같은지. ✅
        assertThat(comment.getContent()).isEqualTo(commentForm.getContent());
        // 플레이 리스트가 같은지. ✅
        assertThat(comment.getPlaylist().getId()).isEqualTo(commentForm.getPlaylistId());
        // 댓글 작성한 사람이 같은지. ✅
        assertThat(member.getId()).isEqualTo(comment.getMember().getId());
        // 플레이리스트의 댓글 개수가 맞는지. ✅
        assertThat(playlist.getCommentCount()).isEqualTo(1);
        // 플레이리스트의 댓글 목록에 들어가있는지. ✅
        //System.out.println(playlist.getCommentList().get(0).getContent());
        //System.out.println( playlist.getCommentList().stream().map(Comment::getContent).toList() );
        assertThat( playlist.getCommentList().stream().map(Comment::getContent).toList() ).contains("좋아요.");

    }

}
