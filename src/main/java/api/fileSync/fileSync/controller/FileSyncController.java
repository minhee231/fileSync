package api.fileSync.fileSync.controller;

import api.fileSync.fileSync.service.FileSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileSyncController {

    @Autowired
    private FileSyncService fileSyncService;

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("path")String path
            ) throws IOException {
        log.info(path + " 경로!!!!!");
        return fileSyncService.createFile(file, path);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteFile(@RequestBody Map<String, String> payload) throws IOException {
        String path = payload.get("path");

        log.info(path);
        return fileSyncService.deleteFile(path);
    }

//    @GetMapping("/exists")
//    public ResponseEntity<Boolean> fileExists(@RequestParam String fileName) {
//		try {
//			// 주어진 파일 이름이 baseDir에 존재하는지 확인
//			boolean fileExists = Files.exists(Paths.get(BASE_DIR, fileName)); // BASE_DIR 활용
//			return ResponseEntity.ok(fileExists);
//		} catch (Exception e) {
//			// 예외 발생 시 BAD_REQUEST 상태 반환
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//					.body(false);
//		}
//	}
}
