//package com.example.fitpassserver.domain.coinPaymentHistory.util;
//
//import com.example.fitpassserver.domain.coinPaymentHistory.dto.request.PortOneRequestDTO;
//import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.PortOneResponseDTO;
//import com.example.fitpassserver.domain.coinPaymentHistory.exception.PortOneErrorCode;
//import com.example.fitpassserver.domain.coinPaymentHistory.exception.PortOneException;
//import io.netty.channel.ChannelOption;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.MediaType;
//import org.springframework.http.client.reactive.ReactorClientHttpConnector;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.ClientResponse;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//import reactor.netty.http.client.HttpClient;
//
//import java.net.URI;
//
//@Slf4j
//@Component
//public class PortOneApiUtil {
//
//    @Value("${port-one.api-secret}")
//    private String apiSecret;
//
//    @Value("${port-one.baseUrl}")
//    private String baseUrl;
//
//    private final String AUTHORIZATION_HEADER = "Authorization";
//    private final String AUTHORIZATION_PREFIX = "PortOne ";
//
//    public PortOneResponseDTO.SearchSinglePaymentDTO searchSinglePayment(String paymentId) {
//        WebClient webClient = getPortOneWebClient();
//        Mono<PortOneResponseDTO.SearchSinglePaymentDTO> response = webClient.get()
//                .uri("/payments/" + paymentId)
//                .retrieve()
//                .onStatus(HttpStatusCode::isError, this::handleError)
//                .bodyToMono(PortOneResponseDTO.SearchSinglePaymentDTO.class)
//                .doOnError((e) -> log.error("API Error {}", e.getMessage()));
//        return response.block();
//    }
//
//    public PortOneResponseDTO.BillingKeyInfo searchCards(PortOneRequestDTO.SearchCardRequestDTO dto) {
//        WebClient webClient = getPortOneWebClient();
//        Mono<PortOneResponseDTO.BillingKeyInfo> response = webClient.method(HttpMethod.GET)
//                .uri("/billing-keys"
////                        , uriBuilder -> URI.create(uriBuilder.queryParam("requestBody", dto).toUriString())
//                )
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(dto)
//                .retrieve()
//                .onStatus(HttpStatusCode::isError, this::handleError)
//                .bodyToMono(PortOneResponseDTO.BillingKeyInfo.class)
//                .doOnError((e) -> log.error("API Error {}", e.getMessage()));
//        return response.block();
//    }
//
//    public PortOneResponseDTO.BillingKeyPaymentSummary payWithBillingKey(String paymentId, PortOneRequestDTO.BillingKeyPaymentRequestDTO dto) {
//        WebClient webClient = getPortOneWebClient();
//        Mono<PortOneResponseDTO.BillingKeyPaymentSummary> response = webClient.post()
//                .uri("/payments/{paymentId}/billing-key", paymentId)
//                .bodyValue(dto)
//                .retrieve()
//                .onStatus(HttpStatusCode::isError, this::handleError)
//                .bodyToMono(PortOneResponseDTO.BillingKeyPaymentSummary.class)
//                .doOnError((e) -> log.error("API Error {}", e.getMessage()));
//
//        return response.block();
//    }
//
//    private Mono<? extends Throwable> handleError(ClientResponse clientResponse) {
//        return clientResponse.bodyToMono(String.class)
//                .flatMap(errorBody -> {
//                    log.error("PortOne API Error Response: {}", errorBody);
//                    return Mono.error(new PortOneException(PortOneErrorCode.PORT_ONE_ERROR_CODE));
//                });
//    }
//
//    private WebClient getPortOneWebClient() {
//        return WebClient.builder()
//                .clientConnector(
//                        new ReactorClientHttpConnector(
//                                HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
//                        )
//                )
//                .baseUrl(this.baseUrl)
//                .defaultHeader(AUTHORIZATION_HEADER, AUTHORIZATION_PREFIX + apiSecret)
//                .build();
//    }
//}
