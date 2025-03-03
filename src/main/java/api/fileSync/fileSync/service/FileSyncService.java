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
        //File baseDir = new File(BASE_DIR);
    }

    public ResponseEntity<Object> createFile(MultipartFile file, String path) throws IOException {
        // 파일을 저장할 전체 경로
        String filePath = BASE_DIR + File.separator + path;
        filePath = filePath.replace("/", File.separator).replace("\\", File.separator);
        filePath = filePath.replace("\\\\", File.separator).replace("/", File.separator);
        log.info("filePath is {}", filePath);

        // 디렉토리 생성
        this.mkdirs(path);

        // 저장할 파일 객체
        File targetFile = new File(filePath);

        // 파일 접근 가능 여부 확인
        if (targetFile.exists() && isFileAccessible(targetFile)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("파일이 사용 중입니다: " + filePath);
        }
        // 파일 저장
        try {
            file.transferTo(targetFile);
            return ResponseEntity.ok("파일 업로드 성공: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 저장 중 오류 발생: " + e.getMessage());
        }
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

    public void mkdirs(String path) {
        String normalizedPath = path.replace("/", File.separator).replace("\\", File.separator);

        // 파일명을 제외한 디렉토리 경로 추출
        String directoryPath = normalizedPath.substring(0, normalizedPath.lastIndexOf(File.separator));

        File dir = new File(BASE_DIR + File.separator + directoryPath);

        // 디렉토리 생성
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                System.out.println("디렉토리 생성 완료: " + dir.getAbsolutePath());
            } else {
                System.out.println("디렉토리 생성 실패: " + dir.getAbsolutePath());
            }
        } else {
            System.out.println("디렉토리가 이미 존재합니다: " + dir.getAbsolutePath());
        }
    }

}
