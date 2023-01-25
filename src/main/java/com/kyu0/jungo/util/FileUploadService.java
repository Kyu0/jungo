package com.kyu0.jungo.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@PropertySource("file.properties")
@RequiredArgsConstructor
@Service
public class FileUploadService {
    
    @Value("${upload.location}")
    private String SAVED_PATH;

    private final Executor executor;

    public List<File> uploadFiles(List<MultipartFile> files) {
        List<File> result = new ArrayList<>();

        files.forEach(file -> result.add(uploadFile(file)));
        
        return result;
    }

    public File uploadFile(MultipartFile file) {
        try {
            File tempFile = getTempFile();
            executor.execute(transferFile(file, tempFile));
            return tempFile;
        }
        catch (IOException e) {
            log.error("파일을 업로드하는 도중 오류가 발생했습니다. 파일명 : {}\n{}", file.getOriginalFilename(), e.getMessage());
            return null;
        }
    }

    private Runnable transferFile(MultipartFile origin, File copy){
        return () -> {
            try {
                log.info("Thread Name : {}", Thread.currentThread().getName());
                origin.transferTo(Path.of(copy.getAbsolutePath()));
            } catch (IllegalStateException | IOException e) {
                log.error("파일을 업로드하는 도중 오류가 발생했습니다.\n{}", e.getMessage());
            }
        };
    }

    private File getTempFile() throws IOException {
        File folder = new File(getDailySavedPath());
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }

        File tempFile = new File(
            new StringBuilder(getDailySavedPath())
                .append('/')
                .append(UUID.randomUUID())
            .toString()
        );

        while (tempFile.exists()) {
            tempFile = new File(
                new StringBuilder(getDailySavedPath())
                    .append('/')
                    .append(UUID.randomUUID())
                .toString()
            );
        }

        tempFile.createNewFile();
        return tempFile;
    }

    private String getDailySavedPath() {
        LocalDate ld = LocalDate.now();

        return new StringBuilder(SAVED_PATH)
            .append('/')
            .append(ld.getYear())
            .append('/')
            .append(ld.getMonthValue())
            .append('/')
            .append(ld.getDayOfMonth())
        .toString();
    }
}
