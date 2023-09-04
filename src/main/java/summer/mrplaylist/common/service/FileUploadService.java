package summer.mrplaylist.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import summer.mrplaylist.common.repository.AmazonS3ResourceStorage;
import summer.mrplaylist.common.model.FileDetail;

@Service
@RequiredArgsConstructor
public class FileUploadService {
	private final AmazonS3ResourceStorage amazonS3ResourceStorage;

	public FileDetail save(MultipartFile multipartFile, String directory) {

		System.out.println("일단 냅다 string으로 출력" + multipartFile.getContentType());
		FileDetail fileDetail = FileDetail.multipartOf(multipartFile, directory);

		String objectKey = fileDetail.getName();

		System.out.println("objectKey = " + objectKey);
		amazonS3ResourceStorage.store(objectKey, multipartFile, directory);
		return fileDetail;
	}
}
