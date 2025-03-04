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
            @RequestParam("file") MultipartFile file, @RequestParam("path")String path) throws IOException {
        return fileSyncService.createFile(file, path);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteFile(@RequestBody Map<String, String> payload) throws IOException {
        String path = payload.get("path");

        log.info(path);
        return fileSyncService.deleteFile(path);
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> fileExists(@RequestParam String path) throws IOException {
		return fileSyncService.fileExists(path);
	}

    @PostMapping("/update/dir")
    public ResponseEntity<Object> updateDir(@RequestBody Map<String, String> payload) throws IOException {
        String path = payload.get("path");

        return fileSyncService.mkdirs(path);
    }
}
