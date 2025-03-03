package api.fileSync.fileSync.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

@Slf4j
@Service
public class FileSyncService {

    @Value("${server.fileSavePath}")
    private String BASE_DIR;

    public FileSyncService(@Value("${server.fileSavePath}") String savePath) {
        // 파일 저장 경로 생성 (존재하지 않으면 폴더 생성)

        this.BASE_DIR = savePath;
        File baseDir = new File(BASE_DIR);
        /*if (!baseDir.exists()) {
            baseDir.mkdirs();
        }*/
    }

    public ResponseEntity<Object> createFile(MultipartFile file) throws IOException {

        String filePath = BASE_DIR + File.separator + file.getOriginalFilename();
        log.info(filePath);
        File targetFile = new File(filePath);

        // 부모 디렉토리까지 생성
        File parentDir = targetFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs(); // 디렉터리 없으면 생성
        }

        // 파일 접근 가능 여부 확인
        if (targetFile.exists() && isFileAccessible(targetFile)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("파일이 사용 중입니다: " + filePath);
        }

        // 파일 저장
        file.transferTo(targetFile);
        return ResponseEntity.ok("파일 업로드 성공: " + filePath);
    }

    public boolean isFileAccessible(File file) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
            // 파일에 대해 읽기/쓰기 접근 가능
            return false;
        } catch (Exception e) {
            // 파일이 다른 프로세스에서 사용 중이거나 접근 불가능
            return true;
        }
    }
}
