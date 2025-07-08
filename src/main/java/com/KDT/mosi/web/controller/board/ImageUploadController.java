package com.KDT.mosi.web.controller.board;

import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/uploads")
public class ImageUploadController {

  private final Path root = Paths.get("uploads/images");

  @PostConstruct
  public void init() throws IOException {
    Files.createDirectories(root);
  }

  @PostMapping("/images")
  public ResponseEntity<Map<String,String>> uploadImage(
      @RequestParam("file") MultipartFile file) {
    if (file.isEmpty()) {
      return ResponseEntity
          .badRequest()
          .body(Map.of("error", "파일이 비어 있습니다."));
    }

    try {
      // 저장할 파일명 만들기
      String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
      Path dest = root.resolve(filename);
      // 파일 저장
      try (InputStream in = file.getInputStream()) {
        Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
      }
      // 접근 가능한 URL 생성
      String url = ServletUriComponentsBuilder.fromCurrentContextPath()
          .path("/uploads/images/")
          .path(filename)
          .toUriString();
      // Quill에 넘길 때는 { url: ... } 형태
      return ResponseEntity.ok(Map.of("url", url));
    } catch (IOException e) {
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("error", "파일 저장 실패"));
    }
  }
}
