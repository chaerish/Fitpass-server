package com.example.fitpassserver.domain.coinPaymentHistory.listener;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.fitpassserver.domain.coin.repository.CoinRepository;
import com.example.fitpassserver.domain.coin.service.CoinService;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.event.CoinSuccessEvent;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.KakaoPaymentApproveDTO;
import com.example.fitpassserver.domain.coinPaymentHistory.dto.response.KakaoPaymentApproveDTO.Amount;
import com.example.fitpassserver.domain.coinPaymentHistory.repository.CoinPaymentRepository;
import com.example.fitpassserver.domain.coinPaymentHistory.service.CoinPaymentHistoryService;
import com.example.fitpassserver.domain.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class CoinPaymentEventListenerTest {

    @Mock
    private CoinService coinService;

    @Mock
    private CoinPaymentHistoryService coinPaymentHistoryService;

    @Mock
    private CoinRepository coinRepository;

    @Mock
    private CoinPaymentRepository coinPaymentRepository;

    @InjectMocks
    private CoinPaymentEventListener coinPaymentEventListener;

    @Test
    @Transactional
    public void testTransactionRollbackOnError() {
        // Given: 실패할 수 있도록 설정
        Member member = mock(Member.class);
        KakaoPaymentApproveDTO dto = mock(KakaoPaymentApproveDTO.class);
        when(dto.amount()).thenReturn(new Amount(550, 0, 0, 0, 0, 0));
        when(dto.quantity()).thenReturn(1);
        when(coinService.createNewCoin(any(), any())).thenThrow(new RuntimeException("Test exception"));

        // When: handle 메서드 실행
        assertThrows(RuntimeException.class, () -> {
            coinPaymentEventListener.handle(new CoinSuccessEvent(member, dto));
        });

        // Then: 트랜잭션이 롤백되었는지 확인 (예: save 메서드가 호출되지 않음)
        verify(coinRepository, never()).save(any());
        verify(coinPaymentRepository, never()).save(any());
    }
}
