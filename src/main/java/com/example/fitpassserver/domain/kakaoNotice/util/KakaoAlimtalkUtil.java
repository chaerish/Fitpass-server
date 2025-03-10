package com.example.fitpassserver.domain.kakaoNotice.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class KakaoAlimtalkUtil {

    @Value("${aligo.apiKey}")
    private  String API_KEY;
    @Value("${aligo.userId}")
    private  String USER_ID;
    @Value("${aligo.senderKey}")
    private  String SENDER_KEY;
    @Value("${aligo.sender}")
    private  String SENDER;

    private final RestTemplate restTemplate;

    public KakaoAlimtalkUtil() {
        this.restTemplate = new RestTemplate();
    }

        // 알림톡 기본 설정
    private MultiValueMap<String, String> init(){
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("apikey", API_KEY);
        params.add("userid", USER_ID);
        params.add("senderkey", SENDER_KEY);
        params.add("sender", SENDER);

        return params;
    }

    private void send(MultiValueMap<String, String> params){
        String url = "https://kakaoapi.aligo.in/akv10/alimtalk/send/";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class
        );
        System.out.println("Alimtalk response: " + response.getBody());
    }

    private void addChannelButton(MultiValueMap<String, String> params){
        String buttonJson = "{\"button\":[{\"name\":\"채널 추가\",\"linkType\":\"AC\",\"linkTypeName\":\"채널 추가\"}]}";
        params.add("button_1", buttonJson);
    }


    //요금제, 코인 구매 완료 알림톡
    public void coinPaymentAlimtalk(String receiver, int amount, String name, String type){
        MultiValueMap<String, String> params = init();
        params.add("tpl_code", "TY_5388");
        params.add("receiver_1", receiver);
        params.add("subject_1", "요금제, 코인 구매 완료");

        // 강조 메시지
        String title = String.format("%,d원",amount);
        params.add("emtitle_1", title);

        // 내용
        String message = String.format(
                "[핏패스]\n결제가 완료되었어요.\n\n- 구매처 : %s\n- 상품명 : %s\n- 결제일시 : %s\n- 결제수단 : %s\n",
                "핏패스", name, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), type);
        params.add("message_1", message);
        // 채널 추가 버튼 추가
        addChannelButton(params);
        send(params);
    }

    // 패스 구매 완료 알림톡
    public void passPaymentAlimtalk(String receiver, int amount, String name){
        MultiValueMap<String, String> params = init();
        params.add("tpl_code", "TY_5389");
        params.add("receiver_1", receiver);
        params.add("subject_1", "패스 구매 완료");

        // 강조 메시지
        String title = String.format("%d코인",amount);
        params.add("emtitle_1", title);

        // 내용
        String message = String.format(
                "[핏패스]\n결제가 완료되었어요.\n\n- 구매처 : %s\n- 상품명 : %s\n- 결제일시 : %s\n- 결제 코인 : %d\n",
                "핏패스", name, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), amount);
        params.add("message_1", message);
        // 채널 추가 버튼 추가
        addChannelButton(params);

        send(params);
    }

    // 결제 실패 알림톡
    public void paymentFailAlimtalk(String receiver, String name){
        MultiValueMap<String, String> params = init();
        params.add("tpl_code", "TY_5390");
        params.add("receiver_1", receiver);
        params.add("subject_1", "결제 실패");

        // 내용
        String message = String.format(
                "[핏패스]\n%s 구독 상품 결제에 실패했습니다.\n\n결제일 포함 3일차까지 자동 결제가 시도되며 결제일 포함 4일차 결제 실패 시 자동 구독 취소됩니다.\n잔고를 충전해 결제를 시도해주세요.\n",
                name);
        params.add("message_1", message);
        // 채널 추가 버튼 추가
        addChannelButton(params);

        send(params);
    }

    // 플랜 구독 자동 해지
    public void cancelPlanAlimtalk(String receiver, String name){
        MultiValueMap<String, String> params = init();
        params.add("tpl_code", "TY_5392");
        params.add("receiver_1", receiver);
        params.add("subject_1", "플랜 구독 자동 해지");

        // 내용
        String message = String.format(
                "[핏패스]\n%s 구독이 자동 해지되었습니다.\n\n재구독을 원하시면 홈페이지 '구독하기'에서 결제하실 수 있습니다. 감사합니다.",
                name
        );
        params.add("message_1", message);
        // 채널 추가 버튼, 웹링크 버튼 추가
        String buttonJson = "{\"button\":[{\"name\":\"채널 추가\",\"linkType\":\"AC\",\"linkTypeName\":\"채널 추가\"},{\"name\":\"구독하러 가기\",\"linkType\":\"WL\",\"linkTypeName\":\"웹링크\",\"linkPc\":\"https://fitpass.co.kr/\",\"linkMo\":\"https://fitpass.co.kr/\"}]}";
        params.add("button_1", buttonJson);

        send(params);
    }

    // 플랜 변경 완료
    public void planChangeAlimtalk(String receiver, String name){
        MultiValueMap<String, String> params = init();
        params.add("tpl_code", "TY_5394");
        params.add("receiver_1", receiver);
        params.add("subject_1", "플랜 변경 완료");

        // 내용
        String message = String.format(
                "[핏패스]\n%s 플랜으로 변경되었습니다.\n다음 결제일부터 적용됩니다.",
                name

        );
        params.add("message_1", message);
        // 채널 추가 버튼 추가
        addChannelButton(params);

        send(params);
    }

    // 플랜 구독 비활성화 완료
    public void deactivatePlanAlimtalk(String receiver, String name){
        MultiValueMap<String, String> params = init();
        params.add("tpl_code", "TY_5395");
        params.add("receiver_1", receiver);
        params.add("subject_1", "플랜 구독 비활성화");

        // 내용
        String message = String.format(
                "[핏패스]\n%s 플랜 구독이 비활성화되었습니다.\n다음 결제일부터 자동 결제가 중단됩니다.",
                name
        );

        params.add("message_1", message);
        // 채널 추가 버튼 추가
        addChannelButton(params);

        send(params);
    }
}