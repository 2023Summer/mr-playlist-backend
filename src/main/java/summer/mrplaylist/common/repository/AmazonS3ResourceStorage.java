package summer.mrplaylist.common.repository;

import com.amazonaws.services.s3.AmazonS3Client;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import summer.mrplaylist.common.util.MultipartUtil;

import java.io.File;

@Component
@RequiredArgsConstructor
public class AmazonS3ResourceStorage {
	@Value("${spring.cloud.aws.s3.bucket}")
	private String bucket;
	private final AmazonS3Client amazonS3Client;

	public void store(String fullPath, MultipartFile multipartFile, String directory) {
		File file = new File(MultipartUtil.getLocalHomeDirectory(), fullPath);
		try {
			multipartFile.transferTo(file);
			fullPath = directory + fullPath;
			amazonS3Client.putObject(new PutObjectRequest(bucket, fullPath, file)
				.withCannedAcl(CannedAccessControlList.PublicRead));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (file.exists()) {
				file.delete();
			}
		}
	}
}
