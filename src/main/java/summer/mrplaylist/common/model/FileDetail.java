package summer.mrplaylist.common.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import summer.mrplaylist.common.util.MultipartUtil;


import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class FileDetail {
	private String id;
	private String name;
	private String format;
	private String path;
	private String imgUrl;
	private long bytes;

	@Builder.Default
	private LocalDateTime createdAt = LocalDateTime.now();

	public static FileDetail multipartOf(MultipartFile multipartFile, String directory) {
		final String fileId = MultipartUtil.createFileId();
		final String format = MultipartUtil.getFormat(multipartFile.getContentType());
		final String imgUrl = "https://mrplaylist-forfiles.s3.ap-northeast-2.amazonaws.com/" + directory + multipartFile.getOriginalFilename();
		return FileDetail.builder()
			.id(fileId)
			.name(multipartFile.getOriginalFilename())
			.format(format)
			.path(MultipartUtil.createPath(fileId, format))
			.bytes(multipartFile.getSize())
			.imgUrl(imgUrl)
			.build();
	}
}
