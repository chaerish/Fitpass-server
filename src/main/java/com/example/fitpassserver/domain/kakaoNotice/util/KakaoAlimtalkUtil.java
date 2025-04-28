package com.example.fitpassserver.domain.kakaoNotice.util;

import com.example.fitpassserver.domain.kakaoNotice.template.AlimtalkTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoAlimtalkUtil {

    private final RestTemplate restTemplate;

    @Value("${aligo.apiKey}")
    private String apiKey;

    @Value("${aligo.userId}")
    private String userId;

    @Value("${aligo.senderKey}")
    private String senderKey;

    @Value("${aligo.sender}")
    private String sender;

    private MultiValueMap<String, String> initParams() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("apikey", apiKey);
        params.add("userid", userId);
        params.add("senderkey", senderKey);
        params.add("sender", sender);
        return params;
    }

    private void addDefaultButtons(MultiValueMap<String, String> params, AlimtalkTemplate template) {
        params.add("button_1", template.getButtonJson());
    }

    private void sendTemplate(String receiver, AlimtalkTemplate template, String message, String subject, String emTitle) {
        MultiValueMap<String, String> params = initParams();
        params.add("tpl_code", template.getTemplateCode());
        params.add("receiver_1", receiver);
        params.add("subject_1", subject);
        if (emTitle != null) {
            params.add("emtitle_1", emTitle);
        }
        params.add("message_1", message);
        addDefaultButtons(params, template);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kakaoapi.aligo.in/akv10/alimtalk/send/", HttpMethod.POST, entity, String.class);
        log.info("Alimtalk Response: {}", response.getBody());
    }

    //요금제, 코인 구매 완료 알림톡
    public void sendCoinOrPlanPayment(String receiver, String name, String type) {
        String message = String.format(
                "%s 구매가 완료 되었어요. 지금 바로 원하는 운동 시설을 찾아보세요\n\n- 구매처 : %s\n- 상품명 : %s\n- 결제일시 : %s\n- 결제수단 : %s\n",
                name, "핏패스", name, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), type);
        sendTemplate(receiver, AlimtalkTemplate.COIN_PAYMENT, message, "요금제/코인 구매 완료", "결제가 완료 되었어요");
    }

    // 패스 구매 완료 알림톡
    public void sendPassPayment(String receiver, int amount, String name) {
        String message = String.format(
                "패스 구매가 완료 되었어요. 패스는 24시간 안에 사용해야 하며, 사용 클릭  후 1시간 이내로 시설에 입장하여 패스 사용을 보여주세요.\n\n- 구매처 : %s\n- 상품명 : %s\n- 결제일시 : %s\n- 결제코인 : %d\n",
                "핏패스", name, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), amount);
        sendTemplate(receiver, AlimtalkTemplate.PASS_PAYMENT, message, "구매 완료", "패스 구매");
    }

    // 1차 결제 실패 알림톡
    public void sendFirstPaymentFail(String receiver) {
        String message = "구독 상품은 결제일 00시 명일 00시에 자동 결제가 시도 되며, 2회 결제 실패시 자동 구독 해지됩니다.\n";
        sendTemplate(receiver, AlimtalkTemplate.PAYMENT_FAIL_1, message, "결제 실패", "결제에 실패했습니다");
    }

    // 2차 결제 실패 알림톡
    public void sendSecondPaymentFail(String receiver) {
        String message = "자동 결제가 이루어 지지 않으며, 관련 서비스 이용이 제한될 수 있습니다.\n서비스 재이용을 원하시는 경우 아래를 참고해주세요\n";
        sendTemplate(receiver, AlimtalkTemplate.PAYMENT_FAIL_2, message, "구독 해지", "구독이 종료되었습니다");
    }

    // 플랜 변경 완료 알림톡
    public void sendPlanChange(String receiver, String oldPlanName, String newPlanName) {
        String message = String.format(
                "요금제 변경이 완료되었어요!\n\n%s → %s\n다음 결제일부터 적용됩니다.\n",
                oldPlanName, newPlanName);
        sendTemplate(receiver, AlimtalkTemplate.PLAN_CHANGE, message, "플랜 변경 완료", null);
    }

    // 회원 가입 인증번호 알림톡
    public void sendCode(String receiver, String code) {
        String message = String.format("인증번호는 [%s]입니다. (유효시간 3분)", code);
        sendTemplate(receiver, AlimtalkTemplate.SEND_CODE, message, "회원가입 인증번호 발송", null);
    }

    // 회원 가입 완료 알림톡
    public void sendRegisterSuccess(String receiver) {
        String message = "핏패스 회원 가입이 완료 되었습니다. 지금 바로 다양한 운동 시설을 자유롭게 둘러보세요\n";
        sendTemplate(receiver, AlimtalkTemplate.REGISTER_SUCCESS, message, "회원 가입 완료", "가입을 환영합니다");
    }

    // 후기 알림톡
    public void sendReviewNotice(String receiver) {
        String message = "오늘 이용한 시설은 어떠셨나요? 리뷰를 남겨 경험을 공유해주세요\n";
        sendTemplate(receiver, AlimtalkTemplate.REVIEW_NOTICE, message, "후기 알림", null);
    }
}
