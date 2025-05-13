package com.example.fitpassserver.global.config;

import com.example.fitpassserver.domain.coinPaymentHistory.util.PortOneUtil;
import io.portone.sdk.server.payment.PaymentClient;
import io.portone.sdk.server.webhook.WebhookVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PortOneConfig {

    @Value("${port-one.base-url}")
    private String apiBaseUrl;

    @Bean
    public PaymentClient paymentClient(PortOneUtil util) {
        return new PaymentClient(util.getApi(), apiBaseUrl, null);
    }

    @Bean
    public WebhookVerifier webhookVerifier(PortOneUtil util) {
        return new WebhookVerifier(util.getWebhook());
    }
}
