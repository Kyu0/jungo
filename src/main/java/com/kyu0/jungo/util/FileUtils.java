package com.kyu0.jungo.util;

import java.io.*;
import java.net.URLEncoder;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;

import com.kyu0.jungo.rest.attachment.Attachment;

import lombok.extern.log4j.Log4j2;

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

    /**
     * 전달받은 파일 이름에서, 확장자명을 제외한 문자열을 반환한다.
     */
    public static String getFileNameWithoutExtension(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    public static String getPathWithoutFileName(String path) {
        return path.substring(0, path.lastIndexOf('/') + 1);
    }

    /**
     * 파일을 다운로드할 수 있는 ResponseEntity를 반환한다.
     * 
     * @param attachment
     * @return 파일 다운로드 정보가 담겨있는 ResponseEntity
     * @throws IOException
     */
    public static ResponseEntity<?> downloadFile(Attachment attachment) throws IOException {
        File savedFile = new File(attachment.getFileName().getPathWithFileName());
        String originalName = attachment.getFileName().getOriginalName();

        return downloadFile(savedFile, originalName);
    }

    /**
     * 파일을 다운로드할 수 있는 ResponseEntity를 반환한다.
     * 
     * @param file 다운받을 파일 객체
     * @param originalName 다운받을 파일의 기본 이름
     * @return 파일 다운로드 정보가 담겨있는 ResponseEntity
     * @throws IOException
     */
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

    /**
     * 파일 다운로드에 실패했을 경우, 메시지가 담긴 ResponseEntity를 반환한다
     * 
     * @return 실패 메시지가 담긴 ResponseEntity
     */
    public static ResponseEntity<String> downloadFileFailed() {
        return ResponseEntity.badRequest()
            .contentLength(0)
        .body("파일 다운로드에 실패했습니다.");
    }

    /**
     * 전달받은 파일 객체를 byte 배열 객체로 변환하여 반환한다.
     * 
     * @return 파일 객체를 변환한 bytes 배열
     */
    public static byte[] fileToByteArray(File file) throws IOException {
        byte[] bytes = new byte[(int)file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
            return bytes;
        }
    }

    /**
     * 전달받은 파일 객체가 유효한 파일인지 반환한다.
     * 
     * @param file
     * @return 유효한 파일일 경우 true, 아닐 경우 false
     */
    public static boolean isAvailable(File file) {
        return file.exists() && file.isFile() && file.getTotalSpace() != 0L;
    }
}
