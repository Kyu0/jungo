package com.kyu0.jungo.rest.attachment;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kyu0.jungo.rest.attachment.Attachment.SaveRequest;
import com.kyu0.jungo.rest.post.Post;
import com.kyu0.jungo.rest.post.PostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class AttachmentService {
    
    private final AttachmentRepository attachmentRepository;
    private final PostRepository postRepository;

    @Transactional(rollbackFor = {EntityNotFoundException.class})
    public List<Long> save(@Valid SaveRequest requestDto) throws EntityNotFoundException{
        List<Long> result = new ArrayList<>();

        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new EntityNotFoundException("해당 게시물을 찾을 수 없습니다."));

        for (int i = 0 ; i < requestDto.getSize() ; ++i) {
            MultipartFile origin = requestDto.getAttachments().get(i);
            File copy = requestDto.getUploadedFiles().get(i);

            if (origin == null || copy == null) {
                continue;
            }

            Attachment entity = Attachment.builder()
                .post(post)
                .size(origin.getSize())
                .fileName(new FileName(origin.getOriginalFilename(), copy.getName(), copy.getPath()))
            .build();

            result.add(attachmentRepository.save(entity).getId());
        }

        return result;
    }
}
