package com.example.fitpassserver.admin.management.controller;

import com.example.fitpassserver.admin.management.dto.request.ManagementAdminRequestDTO;
import com.example.fitpassserver.admin.management.dto.response.ManagementAdminResponseDTO;
import com.example.fitpassserver.admin.management.service.ManagementAdminService;
import com.example.fitpassserver.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "코인/구독 상품관리 어드민 API", description = "구독/코인 상품관리 API입니다.")
public class ManagementAdminController {
    private final ManagementAdminService managementAdminService;

    @Operation(
            summary = "구독 상품 전체 조회",
            description = "구독 상품 조회 위한 api"
    )
    @GetMapping("/management/plan")
    public ApiResponse<List<ManagementAdminResponseDTO.PlanInfoDTO>> getAllPlans() {
        List<ManagementAdminResponseDTO.PlanInfoDTO> plans = managementAdminService.getAllPlans();
        return ApiResponse.onSuccess(plans);
    }

    @Operation(
            summary = "구독 상품 변경",
            description = "구독 상품 변경을 위한 api"
    )
    @PutMapping("/admin/management/plan")
    public ApiResponse<Void> updatePlans(
            @RequestBody @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "구독 정보 업데이트(전체 다 보내시면 됩니다.)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ManagementAdminRequestDTO.UpdatePlanManagementDTO.class),
                            examples = @ExampleObject(
                                    name = "요청 예시",
                                    summary = "플랜 업데이트 요청 예시",
                                    value = "[\n" +
                                            "  {\n" +
                                            "    \"planType\": \"BASIC\",\n" +
                                            "    \"price\": 10000,\n" +
                                            "    \"coinQuantity\": 100,\n" +
                                            "    \"coinAddition\": 10,\n" +
                                            "    \"expirationPeriod\": \"30\"\n" +
                                            "  },\n" +
                                            "  {\n" +
                                            "    \"planType\": \"STANDARD\",\n" +
                                            "    \"price\": 15000,\n" +
                                            "    \"coinQuantity\": 150,\n" +
                                            "    \"coinAddition\": 15,\n" +
                                            "    \"expirationPeriod\": \"30\"\n" +
                                            "  },\n" +
                                            "  {\n" +
                                            "    \"planType\": \"PRO\",\n" +
                                            "    \"price\": 20000,\n" +
                                            "    \"coinQuantity\": 200,\n" +
                                            "    \"coinAddition\": 20,\n" +
                                            "    \"expirationPeriod\": \"30\"\n" +
                                            "  }\n" +
                                            "]"
                            )
                    )
            )
            List<ManagementAdminRequestDTO.UpdatePlanManagementDTO> requestList
    ) {
        managementAdminService.updateAllPlans(requestList);
        return ApiResponse.onSuccess(null);
    }

    @Operation(
            summary = "코인 상품 전체 조회",
            description = "코인 상품 조회 위한 api"
    )
    @GetMapping("/management/coin")
    public ApiResponse<List<ManagementAdminResponseDTO.CoinInfoDTO>> getAllCoins() {
        List<ManagementAdminResponseDTO.CoinInfoDTO> coins = managementAdminService.getAllCoins();
        return ApiResponse.onSuccess(coins);
    }

    @Operation(
            summary = "코인 상품 변경",
            description = "코인 상품 변경을 위한 api"
    )
    @PutMapping("/admin/management/coin")
    public ApiResponse<Void> updateCoins(
            @RequestBody @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "코인 정보 업데이트(전체 다 보내시면 됩니다.)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ManagementAdminRequestDTO.UpdatePlanManagementDTO.class),
                            examples = @ExampleObject(
                                    name = "요청 예시",
                                    summary = "코인 상품 업데이트 요청 예시",
                                    value = "[\n" +
                                            "  {\n" +
                                            "    \"name\": \"1코인\",\n" +
                                            "    \"price\": 10000,\n" +
                                            "    \"coinQuantity\": 100,\n" +
                                            "    \"coinAddition\": 10,\n" +
                                            "    \"expirationPeriod\": \"30\"\n" +
                                            "  },\n" +
                                            "  {\n" +
                                            "    \"name\": \"5코인\",\n" +
                                            "    \"price\": 15000,\n" +
                                            "    \"coinQuantity\": 150,\n" +
                                            "    \"coinAddition\": 15,\n" +
                                            "    \"expirationPeriod\": \"30\"\n" +
                                            "  },\n" +
                                            "  {\n" +
                                            "    \"name\": \"10코인\",\n" +
                                            "    \"price\": 10000,\n" +
                                            "    \"coinQuantity\": 100,\n" +
                                            "    \"coinAddition\": 10,\n" +
                                            "    \"expirationPeriod\": \"30\"\n" +
                                            "  },\n" +
                                            "  {\n" +
                                            "    \"name\": \"20코인\",\n" +
                                            "    \"price\": 15000,\n" +
                                            "    \"coinQuantity\": 150,\n" +
                                            "    \"coinAddition\": 15,\n" +
                                            "    \"expirationPeriod\": \"30\"\n" +
                                            "  },\n" +
                                            "  {\n" +
                                            "    \"name\": \"30코인\",\n" +
                                            "    \"price\": 10000,\n" +
                                            "    \"coinQuantity\": 100,\n" +
                                            "    \"coinAddition\": 10,\n" +
                                            "    \"expirationPeriod\": \"30\"\n" +
                                            "  },\n" +
                                            "  {\n" +
                                            "    \"name\": \"100코인\",\n" +
                                            "    \"price\": 15000,\n" +
                                            "    \"coinQuantity\": 150,\n" +
                                            "    \"coinAddition\": 15,\n" +
                                            "    \"expirationPeriod\": \"30\"\n" +
                                            "  },\n" +
                                            "  {\n" +
                                            "    \"name\": \"300코인\",\n" +
                                            "    \"price\": 20000,\n" +
                                            "    \"coinQuantity\": 200,\n" +
                                            "    \"coinAddition\": 20,\n" +
                                            "    \"expirationPeriod\": \"30\"\n" +
                                            "  }\n" +
                                            "]"
                            )
                    )
            )
            List<ManagementAdminRequestDTO.UpdateCoinManagementDTO> requestList
    ) {
        managementAdminService.updateAllCoins(requestList);
        return ApiResponse.onSuccess(null);
    }
}
