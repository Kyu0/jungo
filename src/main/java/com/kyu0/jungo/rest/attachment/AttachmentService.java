package com.kyu0.jungo.rest.attachment;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.util.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kyu0.jungo.rest.attachment.Attachment.SaveRequest;
import com.kyu0.jungo.rest.post.Post;
import com.kyu0.jungo.rest.post.PostRepository;
import com.kyu0.jungo.util.FileUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class AttachmentService {
    
    private final AttachmentRepository attachmentRepository;
    private final PostRepository postRepository;

    @Transactional(rollbackFor = {EntityNotFoundException.class, FileAlreadyExistsException.class, IOException.class})
    public List<Long> save(@Valid SaveRequest requestDto) throws EntityNotFoundException, IllegalStateException, IOException {
        List<Long> savedFileList = new ArrayList<>();

        for (MultipartFile attachment : requestDto.getAttachments()) {
            File tempFile = getSavedFileName(requestDto.getSavedPath(), FileUtils.getExtensionName(attachment.getOriginalFilename()));
            
            attachment.transferTo(Path.of(tempFile.getAbsolutePath()));
            Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new EntityNotFoundException("해당 게시물을 찾을 수 없습니다."));

            Attachment entity = Attachment.builder()
            .fileName(new FileName(attachment.getOriginalFilename(), tempFile.getName(), requestDto.getSavedPath()))
            .post(post)
            .size(attachment.getSize())
            .build();
            
            savedFileList.add(attachmentRepository.save(entity).getId());
        }
        
        return savedFileList;
    }

    private File getSavedFileName(String savedPath, String extensionName) throws IOException {
        File folder = new File(savedPath);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }

        File tempFile = new File(
            new StringBuilder(savedPath)
                .append('/')
                .append(UUID.randomUUID())
                .append(extensionName)
            .toString()
        );

        while (tempFile.exists()) {
            tempFile = new File(
                new StringBuilder(savedPath)
                    .append('/')
                    .append(UUID.randomUUID())
                    .append(extensionName)
                .toString()
            );
        }
        
        tempFile.createNewFile();
        return tempFile;
    }
}
