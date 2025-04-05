package com.example.fitpassserver.owner.owner.dto;

import com.example.fitpassserver.global.common.dto.CommonRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OwnerRequestDTO {
    /**
     * 사업자 회원가입 dto
     **/
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OwnerJoinDTO extends CommonRequestDTO.JoinDTO {

        @NotBlank(message = "상호는 필수 입력 값입니다.")
        String corporation;

        @NotBlank(message = "사업자등록번호는 필수 입력 값입니다.")
        String businessRegistrationNumber;

        @NotBlank(message = "은행명은 필수 입력 값입니다.")
        String bankName;

        @NotBlank(message = "예금주명은 필수 입력 값입니다.")
        String depositAccountName;

        @NotBlank(message = "사업자계좌번호는 필수 입력 값입니다.")
        String depositAccountNumber;

        @NotBlank
        @Schema(description = "S3 업로드 후 받은 사업자등록증 파일의 key 값 (예: owner/business-registration/123/uuid/filename.pdf)")
        String businessRegistrationUrl;

        @NotBlank
        @Schema(description = "S3 업로드 후 받은 통장사본 파일의 key 값 (예: owner/bank-copy/123/uuid/filename.jpg)")
        String bankCopyUrl;

    }
}
