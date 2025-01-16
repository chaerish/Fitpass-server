package com.example.fitpassserver.domain.plan.dto.request;

public record SubscriptionCancelRequestDTO(
        String cid,
        String sid
) {
}
