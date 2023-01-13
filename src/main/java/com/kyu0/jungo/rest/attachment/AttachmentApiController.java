package com.kyu0.jungo.rest.attachment;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.kyu0.jungo.aop.ExecutionTimeLog;
import com.kyu0.jungo.aop.LoginCheck;
import com.kyu0.jungo.rest.attachment.Attachment.SaveRequest;
import com.kyu0.jungo.util.ApiUtils;
import com.kyu0.jungo.util.ApiUtils.ApiResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@PropertySource("file.properties")
@RestController
@RequiredArgsConstructor
public class AttachmentApiController {
    @Value("${upload.location}")
    private String SAVED_PATH;

    private final AttachmentService attachmentService;

    @LoginCheck
    @ExecutionTimeLog
    @PostMapping("/api/attachments/{postId}")
    public ApiResult<?> save(@RequestParam("attachments") List<MultipartFile> attachments, @PathVariable("postId") Long postId) {
        try {
            log.info("save, {}", SAVED_PATH);
            log.info("File Count : {}", attachments.size());
            Attachment.SaveRequest requestDto = new SaveRequest(postId, attachments, getFullSavedPath(postId));
            return ApiUtils.success(attachmentService.save(requestDto));
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getFullSavedPath(Long postId) {
        StringBuilder sb = new StringBuilder();
        LocalDate ld = LocalDate.now();

        return sb.append(SAVED_PATH)
            .append('/')
            .append(ld.getYear())
            .append('/')
            .append(ld.getMonthValue())
            .append('/')
            .append(ld.getDayOfMonth())
            .append('/')
            .append(postId)
            .append('/')
        .toString();
    }
}
