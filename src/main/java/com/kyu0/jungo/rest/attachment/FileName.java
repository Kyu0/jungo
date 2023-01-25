package com.kyu0.jungo.rest.attachment;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class FileName {

    @NotBlank(message = "원본 파일을 입력해주세요.")
    @Column(name = "ORIGINAL_NAME", nullable = false)
    private String originalName;

    @NotBlank(message = "업로드된 파일명을 입력해주세요.")
    @Column(name = "SAVED_NAME", nullable = false)
    private String savedName;

    @NotBlank(message = "업로드된 파일의 경로를 입력해주세요.")
    @Column(name = "SAVED_PATH", nullable = false)
    private String savedPath;

    @NotBlank(message = "파일의 확장자명을 입력해주세요.")
    @Column(name = "EXTENSION_NAME", columnDefinition = "CHAR(8)", nullable = false)
    private String extensionName;

    public String getSavedPathWithFileName() {
        return new StringBuilder()
            .append(savedPath)
            .append(savedName)
        .toString();
    }
}
