package com.example.fitpassserver.admin.notice.converter;

import com.example.fitpassserver.admin.notice.dto.request.NoticeAdminReqDTO;
import com.example.fitpassserver.admin.notice.dto.response.NoticeAdminResDTO;
import com.example.fitpassserver.domain.notice.entity.Notice;
import com.example.fitpassserver.domain.notice.service.NoticeService;

import java.util.List;
import java.util.stream.Collectors;

public class NoticeAdminConverter {
    public static NoticeAdminResDTO toNoticeAdminResDTO(Notice notice, NoticeService noticeService) {
        return new NoticeAdminResDTO(
                notice.getId(),
                noticeService.getNoticeImage(notice.getId()),
                notice.getTitle(),
                notice.getType().getValue(),
                notice.getCreatedAt(),
                notice.isDraft() ? "임시저장" : "게시중",
                notice.isHomeSlide(),
                notice.isMemberSlide(),
                notice.isOwnerSlide()
        );
    }
    public static List<NoticeAdminResDTO> toNoticeAdminResDTOList(List<Notice> notices, NoticeService noticeService) {
        return notices.stream()
                .map(notice -> toNoticeAdminResDTO(notice, noticeService))
                .collect(Collectors.toList());
    }

    public static Notice toEntity(NoticeAdminReqDTO request) {
        return Notice.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .type(request.getType())
                .build();
    }
}