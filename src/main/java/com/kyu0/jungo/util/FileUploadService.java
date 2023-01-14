package com.kyu0.jungo.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.util.Pair;
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

    public List<File> uploadFiles(List<MultipartFile> files) {
        List<Future<File>> tempResult = new ArrayList<>();
        List<File> result = new ArrayList<>();

        log.info("Executor Service : {}", executorService.toString());
        files.forEach(file -> tempResult.add(executorService.submit(() -> uploadFile(file))));
        
        for (Future<File> temp : tempResult) {
            try {
                result.add(temp.get());
            } catch (InterruptedException | ExecutionException e) {
                log.error("쓰레드의 결과를 받아오는 도중 오류가 발생했습니다. 오류가 발생한 메소드 : uploadFiles()\n{}", e.getMessage());
                result.add(null);
            }
        }

        executorService.shutdown();
        return result;
    }

    public File uploadFile(MultipartFile file) {
        try {
            File tempFile = getTempFile(file);
            file.transferTo(Path.of(tempFile.getAbsolutePath()));
            return tempFile;
        }
        catch (IllegalStateException | IOException e) {
            log.error("파일을 업로드하는 도중 오류가 발생했습니다. 파일명 : {}\n{}", file.getOriginalFilename(), e.getMessage());
            return null;
        }
    }

    private File getTempFile(MultipartFile file) throws IOException {
        File folder = new File(getDailySavedPath());
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }

        String extensionName = FileUtils.getExtensionName(file.getOriginalFilename());

        File tempFile = new File(
            new StringBuilder(getDailySavedPath())
                .append('/')
                .append(UUID.randomUUID())
                .append(extensionName)
            .toString()
        );

        while (tempFile.exists()) {
            tempFile = new File(
                new StringBuilder(getDailySavedPath())
                    .append('/')
                    .append(UUID.randomUUID())
                    .append(extensionName)
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
