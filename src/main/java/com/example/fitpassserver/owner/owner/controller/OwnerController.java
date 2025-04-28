package com.example.fitpassserver.owner.owner.controller;

import com.example.fitpassserver.domain.member.validation.validator.CheckLoginIdValidator;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import com.example.fitpassserver.global.aws.s3.dto.S3UrlResponseDTO;
import com.example.fitpassserver.global.aws.s3.service.S3Service;
import com.example.fitpassserver.owner.owner.converter.OwnerConverter;
import com.example.fitpassserver.owner.owner.dto.OwnerRequestDTO;
import com.example.fitpassserver.owner.owner.dto.OwnerResponseDTO;
import com.example.fitpassserver.owner.owner.entity.Owner;
import com.example.fitpassserver.owner.owner.service.command.OwnerCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/owner")
@Tag(name = "회원가입 사장님 API", description = "사장님 회원가입 관련 API입니다.")
public class OwnerController {
    private final OwnerCommandService ownerCommandService;
    private final CheckLoginIdValidator checkLoginIdValidator;
    private final S3Service s3Service;

    @InitBinder("joinDTO")
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkLoginIdValidator);
    }

    @Operation(summary = "사업자등록증 업로드할 url 요청 ", description = "사업자등록증 업로드 위한 presigned url 요청 api입니다.")
    @GetMapping("/registration")
    public ApiResponse<S3UrlResponseDTO> getOwnerPresignedUrlRegistration(
            @RequestParam String filename
    ) {
        S3UrlResponseDTO presignedUrl = s3Service.getOwnerPresignedUrl("businessRegistration", filename);
        return ApiResponse.onSuccess(presignedUrl);
    }

    @Operation(summary = "통장사본 업로드할 url 요청 ", description = "통장사본 업로드 위한 presigned url 요청 api입니다.")
    @GetMapping("/bank-copy")
    public ApiResponse<S3UrlResponseDTO> getOwnerPresignedUrlBankCopy(
            @RequestParam String filename
    ) {
        S3UrlResponseDTO presignedUrl = s3Service.getOwnerPresignedUrl("bankCopy", filename);
        return ApiResponse.onSuccess(presignedUrl);
    }

    @Operation(summary = "사장님 회원가입 api", description = "사장님 회원가입을 위한 api입니다.")
    @PostMapping("/register")
    public ApiResponse<OwnerResponseDTO.JoinResultDTO> join(@RequestBody @Valid OwnerRequestDTO.OwnerJoinDTO request) {
        Owner owner = ownerCommandService.joinOwner(request);
        return ApiResponse.onSuccess(OwnerConverter.toJoinResultDTO(owner));
    }
}
