package com.example.fitpassserver.admin.payment.service;

import com.example.fitpassserver.admin.payment.dto.response.CoinPaymentHistoryResponseDTO;
import com.example.fitpassserver.admin.payment.dto.response.PassPaymentHistoryResponseDTO;

public interface PaymentAdminService {
    CoinPaymentHistoryResponseDTO getCoinPaymentHistory(String memberName, int size, int page);

    PassPaymentHistoryResponseDTO getPassPaymentHistory(String memberName, int size, int page);
}
