package api.fileSync.fileSync.controller;

import api.fileSync.fileSync.service.FileSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

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
}
