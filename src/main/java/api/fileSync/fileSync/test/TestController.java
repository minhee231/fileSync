//package api.fileSync.fileSync.test;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//@Slf4j
//@RestController
///*@RequestMapping("/file")*/
//public class TestController {
//
//    @Value("${server.fileSavePath}")
//    private String BASE_DIR;
//
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("relativePath") String relativePath) {
//        log.info("파일경로"+ relativePath);
//        try {
//            String filePath = BASE_DIR + File.separator + file.getOriginalFilename();
//            File targetFile = new File(filePath);
//
//            // 파일 접근 가능 여부 확인
//            if (targetFile.exists() && isFileAccessible(targetFile)) {
//                return ResponseEntity.status(HttpStatus.CONFLICT).body("파일이 사용 중입니다: " + filePath);
//            }
//
//            // 파일 저장
//            file.transferTo(new File(filePath));
//            return ResponseEntity.ok("파일 업로드 성공: " + filePath);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패: " + e.getMessage());
//        }
//    }
//
//    public boolean isFileAccessible(File file) {
//        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
//            // 파일에 대해 읽기/쓰기 접근 가능
//            return false;
//        } catch (Exception e) {
//            // 파일이 다른 프로세스에서 사용 중이거나 접근 불가능
//            return true;
//        }
//    }
//
////    @PostMapping("/upload")
////    public ResponseEntity<String> uploadFile(
////            @RequestParam("file") MultipartFile file,
////            @RequestParam("path") String path,
////            @RequestParam("fileName") String fileName
////    ) {
////
////        // 저장할 파일 경로와 이름을 결합
////        String destinationPath = BASE_DIR + File.separator + fileName;
////
////        try {
////            // 파일을 지정된 경로에 저장
////            File targetFile = new File(destinationPath);
////
////            // 디렉토리가 없다면 생성
////            File parentDir = targetFile.getParentFile();
////            if (!parentDir.exists()) {
////                parentDir.mkdirs();  // 디렉토리 생성
////            }
////
////            // 파일 복사
////            Files.copy(file.getInputStream(), Paths.get(destinationPath));
////
////            // 성공적으로 업로드된 경우
////            return ResponseEntity.ok().body(path);
////
////        } catch (Exception e) {
////            // 파일 업로드 중 오류가 발생한 경우
////            return ResponseEntity.ok().body(path);
////        }
////    }
////
////        /*try {
////            // 파일 처리 로직 (예: 저장)
////            if (!file.isEmpty()) {
////                // 파일 저장 또는 처리 로직
////                //String fileName = file.getOriginalFilename();
////                file.transferTo(new File(BASE_DIR + File.separator + fileName));
////            }
////
////            // 문자열 처리 로직 (예: description 사용)
////            System.out.println("Received description: " + path);
////
////            // 성공적인 응답
////            return ResponseEntity.ok("File and description received successfully!");
////
////        } catch (Exception e) {
////            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
////        }
////        //return ResponseEntity.ok().body(path);
////    }*/
//
//}
