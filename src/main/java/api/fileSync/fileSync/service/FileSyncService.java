package api.fileSync.fileSync.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    public ResponseEntity<Object> mkdirs(String path) {
        String normalizedPath = path.replace("/", File.separator).replace("\\", File.separator);

        File dir;

        // 확장자가 있는지 확인하여 파일인지 디렉토리인지 판별
        if (normalizedPath.contains(".") && normalizedPath.lastIndexOf(".") > normalizedPath.lastIndexOf(File.separator)) {
            // 확장자가 있는 경우 → 파일이므로 디렉토리 경로만 추출
            String directoryPath = normalizedPath.substring(0, normalizedPath.lastIndexOf(File.separator));
            dir = new File(BASE_DIR + File.separator + directoryPath);
        } else {
            // 확장자가 없는 경우 → 디렉토리 그대로 사용
            dir = new File(BASE_DIR + File.separator + normalizedPath);
        }

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

        return ResponseEntity.ok("디렉토리 업데이트 완료: " + dir.getAbsolutePath());
    }


    public ResponseEntity<Object> deleteFile(String path) {
        // 파일 경로 설정
        File file = new File(BASE_DIR + File.separator + path);
        log.info("file is {}", file.getAbsolutePath());

        // 파일이 존재하는지 확인
        if (file.exists()) {
            // 파일 삭제 시도
            boolean deleted = file.delete();

            if (deleted) {
                log.info("파일 삭제 성공: {}", file.getAbsolutePath());
                return ResponseEntity.ok().body("파일 삭제 성공");
            } else {
                log.error("파일 삭제 실패: {}", file.getAbsolutePath());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("파일 삭제 실패");
            }
        } else {
            log.warn("파일이 존재하지 않습니다: {}", file.getAbsolutePath());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("파일을 찾을 수 없습니다");
        }
    }

    public ResponseEntity<Boolean> fileExists(String path) {
        try {
            // 주어진 파일 이름이 baseDir에 존재하는지 확인
            boolean fileExists = Files.exists(Paths.get(BASE_DIR, path)); // BASE_DIR 활용
            return ResponseEntity.ok(fileExists);
        } catch (Exception e) {
            // 예외 발생 시 BAD_REQUEST 상태 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(false);
        }
    }

    public ResponseEntity<Object> deleteDirectory(String path) {
            try {
                // 디렉토리 삭제 요청
                String normalizedPath = path.replace("/", File.separator).replace("\\", File.separator);
                File dir = new File(BASE_DIR + File.separator + normalizedPath);

                if (dir.exists() && dir.isDirectory()) {
                    boolean deleted = deleteDirectory(dir);
                    if (deleted) {
                        log.info("디렉토리 삭제 완료: {}", dir.getAbsolutePath());
                        return ResponseEntity.ok("디렉토리 삭제 완료: " + dir.getAbsolutePath());
                    } else {
                        log.error("디렉토리 삭제 실패: {}", dir.getAbsolutePath());
                        return ResponseEntity.status(500).body("디렉토리 삭제 실패: " + dir.getAbsolutePath());
                    }
                } else {
                    log.error("디렉토리가 존재하지 않습니다: {}", dir.getAbsolutePath());
                    return ResponseEntity.status(404).body("디렉토리가 존재하지 않습니다: " + dir.getAbsolutePath());
                }
            } catch (Exception e) {
                log.error("디렉토리 삭제 중 오류 발생", e);
                return ResponseEntity.status(500).body("디렉토리 삭제 중 오류 발생");
            }
        }

        // 디렉토리 및 하위 파일을 재귀적으로 삭제하는 메서드
        private boolean deleteDirectory(File dir) {
            if (dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        deleteDirectory(file); // 재귀 호출
                    }
                }
            }
            return dir.delete();
        }


    // 하위 폴더와 파일까지 삭제하는 재귀 메서드
//    private boolean deleteDirectory(File dir) {
//        if (dir.isDirectory()) {
//            File[] files = dir.listFiles();
//            if (files != null) {
//                for (File file : files) {
//                    deleteDirectory(file);
//                }
//            }
//        }
//        return dir.delete();
//    }

}
