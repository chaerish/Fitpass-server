package com.example.fitpassserver.domain.kakaoNotice.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class KakaoAlimtalkService {

    @Value("${aligo.apiKey}")
    private  String API_KEY;
    @Value("${aligo.userId}")
    private  String USER_ID;
    @Value("${aligo.senderKey}")
    private  String SENDER_KEY;
    @Value("${aligo.sender}")
    private  String SENDER;

    private final RestTemplate restTemplate;

    public KakaoAlimtalkService() {
        this.restTemplate = new RestTemplate();
    }

    public void sendAlimtalk(String receiver, String tplCode, String subject, String message,
                             String failoverSubject, String failoverMessage) {
        String url = "https://kakaoapi.aligo.in/akv10/alimtalk/send/";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("apikey", API_KEY);
        params.add("userid", USER_ID);
        params.add("senderkey", SENDER_KEY);
        params.add("tpl_code", tplCode);
        params.add("sender", SENDER);
        params.add("receiver_1", receiver);
        params.add("subject_1", subject);
        params.add("message_1", message);

//        // 대체문자 설정 (선택사항)
//        params.add("failover", "Y");
//        params.add("fsubject_1", failoverSubject);
//        params.add("fmessage_1", failoverMessage);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, String.class
            );
            System.out.println("Alimtalk response: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Failed to send Alimtalk: " + e.getMessage());
        }
    }

}