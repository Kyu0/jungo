package com.kyu0.jungo.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

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

    private final ExecutorService executorService;

    public List<Future<File>> uploadFiles(List<MultipartFile> files) {
        List<Future<File>> result = new ArrayList<>();

        files.forEach(file -> result.add(executorService.submit(() -> uploadFile(file))));
        
        return result;
    }

    public File uploadFile(MultipartFile file) throws IOException {
        log.info("Thread Name : {}", Thread.currentThread().getName());
        log.info("Uploading : {}", file.getOriginalFilename());
        File copy = getTempFile();
        file.transferTo(Path.of(copy.getAbsolutePath()));
        log.info("Uploaded : {}", file.getOriginalFilename());
        
        return copy;
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
