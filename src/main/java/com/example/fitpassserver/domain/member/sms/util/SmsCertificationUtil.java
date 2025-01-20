package com.example.fitpassserver.domain.member.sms.util;

import com.example.fitpassserver.domain.fitness.entity.MemberFitness;
import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmsCertificationUtil {

    @Value("${coolsms.apikey}") // coolsms의 API 키 주입
    private String apiKey;

    @Value("${coolsms.apisecret}") // coolsms의 API 비밀키 주입
    private String apiSecret;

    @Value("${coolsms.fromnumber}") // 발신자 번호 주입
    private String fromNumber;

    DefaultMessageService messageService; // 메시지 서비스를 위한 객체


    private static final String COIN_MESSAGE_FORMAT = "[FitPass]\n%s원 결제 되었습니다.\n코인 %s개를 구매하셨습니다.\n";
    private static final String PASS_MESSAGE_FORMAT = "[FitPass]\n[%s] 시설 패스 결제하였습니다.\n%s코인을 사용했습니다.\n";
    private static final String PLAN_MESSAGE_FORMAT = "[FitPass]\n[%s] 플랜을 결제하였습니다.\n%s원을 결제 완료되었습니다.\n";

    @PostConstruct // 의존성 주입이 완료된 후 초기화를 수행하는 메서드
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr"); // 메시지 서비스 초기화
    }

    // 단일 메시지 발송
    public void sendSMS(String to, String certificationCode) {
        Message message = new Message(); // 새 메시지 객체 생성
        message.setFrom(fromNumber); // 발신자 번호 설정
        message.setTo(to); // 수신자 번호 설정
        message.setText("본인확인 인증번호는 " + certificationCode + "입니다."); // 메시지 내용 설정

        this.messageService.sendOne(new SingleMessageSendingRequest(message)); // 메시지 발송 요청
    }

    // 코인 단건 결제 메시지 발송
    public void sendCoinPaymentSMS(String to, int quantity, int total) {
        sendPaymentSMS(to, String.format(COIN_MESSAGE_FORMAT, total, quantity));
    }

    // 패스 구매 메시지 발송
    public void sendPassPaymentSMS(String to, Long price, MemberFitness memberFitness) {
        sendPaymentSMS(to, String.format(PASS_MESSAGE_FORMAT, memberFitness.getFitness().getName(), price));
    }

    // 플랜 결제 메시지 발송
    public void sendPlanPaymentSMS(String to, String name, int price) {
        sendPaymentSMS(to, String.format(PLAN_MESSAGE_FORMAT, name, price));
    }

    // 메시지 초기 설정
    private void sendPaymentSMS(String to, String text) {
        Message message = new Message();
        message.setFrom(fromNumber);
        message.setTo(to);
        message.setText(text);

        messageService.sendOne(new SingleMessageSendingRequest(message)); // 메시지 발송 요청
    }
}