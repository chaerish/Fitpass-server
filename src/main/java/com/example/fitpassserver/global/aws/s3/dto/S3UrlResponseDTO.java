package com.example.fitpassserver.global.aws.s3.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class S3UrlResponseDTO {
    //presigned url과 url 발급받은 key를 전달하는 dto
    private String preSignedUrl;

    private String key;

    @Builder
    public S3UrlResponseDTO(String preSignedUrl, String key) {
        this.preSignedUrl = preSignedUrl;
        this.key = key;
    }
}
