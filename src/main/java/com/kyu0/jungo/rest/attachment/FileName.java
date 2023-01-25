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

    @NotBlank
    @Column(name = "ORIGINAL_NAME")
    private String originalName;

    @NotBlank
    @Column(name = "SAVED_NAME")
    private String savedName;

    @NotBlank
    @Column(name = "SAVED_PATH")
    private String savedPath;

    @NotBlank
    @Column(name = "EXTENSION_NAME", columnDefinition = "CHAR(8)")
    private String extensionName;

    public String getPathWithFileName() {
        return new StringBuilder()
            .append(savedPath)
            .append(savedName)
        .toString();
    }
}
