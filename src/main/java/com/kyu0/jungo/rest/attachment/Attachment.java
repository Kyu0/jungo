package com.kyu0.jungo.rest.attachment;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.web.multipart.MultipartFile;

import com.kyu0.jungo.rest.post.Post;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "ATTACHMENT")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private FileName fileName;

    @Min(value = 1, message = "첨부 파일의 크기가 잘못되었습니다.")
    @Column(name = "SIZE")
    private long size;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    public void setPost(Post post) {
        this.post = post;
        
        if (!post.getAttaches().contains(this)) {
            post.getAttaches().add(this);
        }
    }

    /**
     * DTO 선언부
     */

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveRequest {
        @NotNull
        private Long postId;

        @NotEmpty
        private List<MultipartFile> attachments;

        @NotBlank
        private String savedPath;

        public SaveRequest(Long postId, MultipartFile[] attachments, String savedPath) {
            this.postId = postId;
            this.attachments = Stream.of(attachments).collect(Collectors.toList());
            this.savedPath = savedPath;
        }
    }
}
