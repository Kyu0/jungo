package com.kyu0.jungo.util;

import java.io.*;
import java.net.URLEncoder;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;

import com.kyu0.jungo.rest.attachment.Attachment;

public class FileUtils {
    
    /**
     * 전달받은 파일 이름에서, 가장 마지막에 있는 '.' 문자부터 끝까지의 문자열을 반환한다.
     * 
     * @param fileName 확장자가 포함된 파일의 이름
     * @return 파일의 확장자명
     */
    public static String getExtensionName(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'));
    }

    public static ResponseEntity<?> downloadFile(Attachment attachment) throws IOException {
        return downloadFile(new File(attachment.getFileName().getSavedPath()), attachment.getFileName().getOriginalName());
    }

    public static ResponseEntity<?> downloadFile(File file, String originalName) throws IOException {
        if (!isAvailable(file)) {
            throw new IOException("해당 파일을 찾을 수 없습니다.");
        }

        byte[] bytes = fileToByteArray(file);
        
        return ResponseEntity.ok()
            .contentLength(bytes.length)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
            .header(HttpHeaders.CONTENT_DISPOSITION, new StringBuilder("attachment; filename=\"")
                .append(URLEncoder.encode(originalName, "utf-8"))
                .append("\"")
            .toString())
            .body(new ByteArrayResource(bytes));
    }

    public static ResponseEntity<String> downloadFileFailed() {
        return ResponseEntity.badRequest()
            .contentLength(0)
        .body("파일 다운로드에 실패했습니다.");
    }

    public static byte[] fileToByteArray(File file) throws IOException {
        byte[] bytes = new byte[(int)file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
            return bytes;
        }
    }

    public static boolean isAvailable(File file) {
        return file.exists() && file.isFile() && file.getTotalSpace() != 0L;
    }
}
