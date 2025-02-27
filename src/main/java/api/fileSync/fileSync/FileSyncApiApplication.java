package api.fileSync.fileSync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FileSyncApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileSyncApiApplication.class, args);
        new fileSyncService();

    }
}
