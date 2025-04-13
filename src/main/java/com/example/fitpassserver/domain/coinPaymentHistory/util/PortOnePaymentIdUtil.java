package com.example.fitpassserver.domain.coinPaymentHistory.util;

import java.util.UUID;

public class PortOnePaymentIdUtil {
    public static String getRandomPaymentId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
