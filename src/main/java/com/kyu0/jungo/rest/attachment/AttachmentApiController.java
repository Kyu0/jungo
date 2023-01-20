package com.kyu0.jungo.rest.attachment;

import java.io.File;
import java.util.List;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.kyu0.jungo.aop.ExecutionTimeLog;
import com.kyu0.jungo.aop.LoginCheck;
import com.kyu0.jungo.rest.attachment.Attachment.FindResponse;
import com.kyu0.jungo.rest.attachment.Attachment.SaveRequest;
import com.kyu0.jungo.util.ApiUtils;
import com.kyu0.jungo.util.FileUploadService;
import com.kyu0.jungo.util.FileUtils;
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
    public ApiResult<?> save(@RequestParam("attachments") List<MultipartFile> attachments, @PathVariable("postId") Long postId) {
        try {
            List<File> uploadedFiles = fileUploadService.uploadFiles(attachments);
            SaveRequest requestDto = new SaveRequest(postId, attachments, uploadedFiles, attachments.size());
            
            return ApiUtils.success(attachmentService.save(requestDto));
        }
        catch (Exception e) {
            e.printStackTrace();
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
            log.info("얍");
            Attachment attachment = attachmentService.findById(id);
            log.info("File Name : {}", attachment.getFileName().getSavedPath());
            return FileUtils.downloadFile(attachment);
        }
        catch (Exception e) {
            log.info("흡");
            e.printStackTrace();
            return FileUtils.downloadFileFailed();
        }
    }

}
