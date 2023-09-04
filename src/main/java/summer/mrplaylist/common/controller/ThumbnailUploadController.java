package summer.mrplaylist.common.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import summer.mrplaylist.common.service.FileUploadService;
import summer.mrplaylist.common.model.FileDetail;


@RestController
@RequestMapping(value = "/upload")
@RequiredArgsConstructor
public class ThumbnailUploadController {
	private final FileUploadService fileUploadService;

	@PostMapping
	public ResponseEntity<FileDetail> post(
		@RequestPart("file") MultipartFile multipartFile) {
		System.out.println("파일 업로드 서비스 실행");
		return ResponseEntity.ok(fileUploadService.save(multipartFile, "aws test/"));
	}
}
