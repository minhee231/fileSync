package api.fileSync.fileSync.controller;

import api.fileSync.fileSync.service.FileSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileSyncController {

    @Autowired
    private FileSyncService fileSyncService;

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return fileSyncService.createFile(file);
    }
}
