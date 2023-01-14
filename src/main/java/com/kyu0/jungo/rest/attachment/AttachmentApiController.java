package com.kyu0.jungo.rest.attachment;

import java.io.File;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.kyu0.jungo.aop.ExecutionTimeLog;
import com.kyu0.jungo.aop.LoginCheck;
import com.kyu0.jungo.rest.attachment.Attachment.SaveRequest;
import com.kyu0.jungo.util.ApiUtils;
import com.kyu0.jungo.util.FileUploadService;
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
            // Upload Files
            List<File> uploadedFiles = fileUploadService.uploadFiles(attachments);
            SaveRequest requestDto = new SaveRequest(postId, attachments, uploadedFiles, attachments.size());
            
            return ApiUtils.success(attachmentService.save(requestDto));
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
