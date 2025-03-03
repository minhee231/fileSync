//package api.fileSync.fileSync;
//
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.Map;
//
//@Slf4j
//@RestController
//@RequestMapping("/api/files")
//public class fileSyncService {
//
//	@Value("${server.fileSavePath}")
//	private String BASE_DIR;
//
//	public fileSyncService(@Value("${server.fileSavePath}") String savePath) {
//		// 파일 저장 경로 생성 (존재하지 않으면 폴더 생성)
//
//		this.BASE_DIR = savePath;
//		File baseDir = new File(BASE_DIR);
//		if (!baseDir.exists()) {
//			baseDir.mkdirs();
//		}
//	}
//
//	public fileSyncService() {
//
//	}
//
//	/**
//	 * 파일이 사용 중인지 여부 확인
//	 */
//	public boolean isFileAccessible(File file) {
//		try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
//			// 파일에 대해 읽기/쓰기 접근 가능
//			return false;
//		} catch (Exception e) {
//			// 파일이 다른 프로세스에서 사용 중이거나 접근 불가능
//			return true;
//		}
//	}
//
//	/**
//	 * 파일 업로드
//	 */
//	@PostMapping("/upload")
//	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
//		logRequestParameters(request);
//		try {
//			String filePath = BASE_DIR + File.separator + file.getOriginalFilename();
//			log.info(filePath);
//			File targetFile = new File(filePath);
//
//			// 부모 디렉토리까지 생성
//			File parentDir = targetFile.getParentFile();
//			if (!parentDir.exists()) {
//				parentDir.mkdirs(); // 디렉터리 없으면 생성
//			}
//
//			// 파일 접근 가능 여부 확인
//			if (targetFile.exists() && isFileAccessible(targetFile)) {
//				return ResponseEntity.status(HttpStatus.CONFLICT).body("파일이 사용 중입니다: " + filePath);
//			}
//
//			// 파일 저장
//			file.transferTo(targetFile);
//			return ResponseEntity.ok("파일 업로드 성공: " + filePath);
//		} catch (IOException e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패: " + e.getMessage());
//		}
//	}
//
//
//	/**
//	 * 파일 삭제
//	 */
//	@DeleteMapping("/delete")
//	public ResponseEntity<String> deleteFile(@RequestBody Map<String, String> payload) {
//		String fileName = payload.get("fileName");
//		if (fileName == null || fileName.isBlank()) {
//			return ResponseEntity.badRequest().body("fileName 매개변수가 누락되었습니다.");
//		}
//
//		File file = new File(BASE_DIR + File.separator + fileName);
//
//		// 파일 존재 여부 및 접근 가능 여부 확인
//		if (file.exists()) {
//			if (isFileAccessible(file)) {
//				return ResponseEntity.status(HttpStatus.CONFLICT).body("파일이 사용 중입니다: " + fileName);
//			}
//
//			if (file.delete()) {
//				return ResponseEntity.ok("파일 삭제 성공: " + fileName);
//			} else {
//				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 삭제 실패");
//			}
//		} else {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("파일 없음: " + fileName);
//		}
//	}
//	@GetMapping("/exists")
//	public ResponseEntity<Boolean> fileExists(@RequestParam String fileName) {
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
//
//
//	//테스트
//	private void logRequestParameters(HttpServletRequest request) {
//		Map<String, String[]> parameterMap = request.getParameterMap();
//
//		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
//			String paramName = entry.getKey();
//			String[] paramValues = entry.getValue();
//			System.out.println("파라미터 이름: " + paramName);
//			for (String paramValue : paramValues) {
//				System.out.println("파라미터 값: " + paramValue);
//			}
//		}
//	}
//}