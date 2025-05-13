package com.example.fitpassserver.domain.coinPaymentHistory.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PortOneUtil {

    @Value("${port-one.secret.api}")
    private String api;

    @Value("${port-one.secret.webhook}")
    private String webhook;

    public String getApi() {
        return api;
    }

    public String getWebhook() {
        return webhook;
    }
}
