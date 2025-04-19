package com.example.fitpassserver.domain.kakaoNotice.template;

public enum AlimtalkTemplate {
    COIN_PAYMENT("TZ_2553", "{\"button\":[{\"name\":\"채널 추가\",\"linkType\":\"AC\",\"linkTypeName\":\"채널 추가\"},{\"name\":\"시설 둘러보기\",\"linkType\":\"WL\",\"linkTypeName\":\"웹링크\",\"linkPc\":\"https://fitpass.co.kr/fitness\",\"linkMo\":\"https://fitpass.co.kr/fitness\"}]}"),
    PASS_PAYMENT("TZ_2554", "{\"button\":[{\"name\":\"채널 추가\",\"linkType\":\"AC\"},{\"name\":\"패스 확인하기\",\"linkType\":\"WL\",\"linkPc\":\"https://fitpass.co.kr/use-pass\",\"linkMo\":\"https://fitpass.co.kr/use-pass\"}]}"),
    PAYMENT_FAIL_1("TZ_2555", "{\"button\":[{\"name\":\"채널 추가\",\"linkType\":\"AC\"}]}"),
    PAYMENT_FAIL_2("TZ_2556", "{\"button\":[{\"name\":\"채널 추가\",\"linkType\":\"AC\"},{\"name\":\"구독 관리 바로가기\",\"linkType\":\"WL\",\"linkPc\":\"https://fitpass.co.kr/subscribe\",\"linkMo\":\"https://fitpass.co.kr/subscribe\"}]}"),
    PLAN_CHANGE("TZ_0745", "{\"button\":[{\"name\":\"채널 추가\",\"linkType\":\"AC\"},{\"name\":\"핏패스 바로가기\",\"linkType\":\"WL\",\"linkPc\":\"https://fitpass.co.kr/\",\"linkMo\":\"https://fitpass.co.kr/\"}]}"),
    REGISTER_SUCCESS("TZ_2552", "{\"button\":[{\"name\":\"채널 추가\",\"linkType\":\"AC\"},{\"name\":\"시설 둘러보기\",\"linkType\":\"WL\",\"linkPc\":\"https://fitpass.co.kr/\",\"linkMo\":\"https://fitpass.co.kr/\"}]}"),
    REVIEW_NOTICE("TZ_2547", "{\"button\":[{\"name\":\"채널 추가\",\"linkType\":\"AC\"},{\"name\":\"후기 남기기\",\"linkType\":\"WL\",\"linkPc\":\"https://fitpass.co.kr/use-pass\",\"linkMo\":\"https://fitpass.co.kr/use-pass\"}]}"),
    SEND_CODE("TZ_4879", "{\"button\":[{\"name\":\"채널 추가\",\"linkType\":\"AC\"}]}");

    private final String templateCode;
    private final String buttonJson;

    AlimtalkTemplate(String templateCode, String buttonJson) {
        this.templateCode = templateCode;
        this.buttonJson = buttonJson;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public String getButtonJson() {
        return buttonJson;
    }
}
