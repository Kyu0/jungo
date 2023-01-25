package com.kyu0.jungo.rest.attachment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.kyu0.jungo.aop.*;
import com.kyu0.jungo.rest.attachment.Attachment.*;
import com.kyu0.jungo.util.*;
import com.kyu0.jungo.util.ApiUtils.ApiResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
public class AttachmentApiController {

    private final AttachmentService attachmentService;
    private final FileUploadService fileUploadService;

    @LoginCheck
    @ExecutionTimeLog
    @PostMapping("/api/attachments/{postId}")
    public ApiResult<?> save(@RequestParam("attachments") List<MultipartFile> attachments, @PathVariable("postId") Long postId) throws InterruptedException, ExecutionException {
        List<File> uploadedFiles = new ArrayList<>();

        try {
            for (Future<File> file : fileUploadService.uploadFiles(attachments)) {
                uploadedFiles.add(file.get());
            }
            SaveRequest requestDto = new SaveRequest(postId, attachments, uploadedFiles, attachments.size());
            
            List<Long> savedFileIds = attachmentService.save(requestDto);
            SaveResponse responseDto = new SaveResponse(attachments.size(), (int)uploadedFiles.stream().filter(file -> file != null).count() , savedFileIds);
            log.info("response : {}, {}, {}", responseDto.getRequestCount(), responseDto.getResponseCount(), responseDto.getSavedFileIds());
            return ApiUtils.success(responseDto);
        }
        catch (EntityNotFoundException e) {
            for (File uploadedFile : uploadedFiles) {
                if (uploadedFile != null && uploadedFile.exists()) {
                    uploadedFile.delete();
                }
            }
            log.error(e.getMessage());
            return ApiUtils.error(e, HttpStatus.BAD_REQUEST);
        }
    }

    @LoginCheck
    @GetMapping("/api/attachments/{id}")
    public ApiResult<?> findById(@PathVariable Long id) {
        try {
            Attachment attachment = attachmentService.findById(id);
            return ApiUtils.success(new FindResponse(attachment));
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @LoginCheck
    @GetMapping("/api/attachments/download/{id}")
    public ResponseEntity<?> download(HttpServletResponse response, @PathVariable Long id) {
        try {
            Attachment attachment = attachmentService.findById(id);
            return FileUtils.downloadFile(attachment);
        }
        catch (Exception e) {
            e.printStackTrace();
            return FileUtils.downloadFileFailed();
        }
    }

}
