package com.example.fitpassserver.admin.dashboard.entity;

import com.example.fitpassserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "dash_board")
public class DashBoard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "new_member_count")
    private int newMemberCount;

    @Column(name = "visitant")
    private int visitant;

    @Column(name = "page_view")
    private int pageView;

    @Column(name = "buy_pass")
    private int buyPass;

    @Column(name = "use_pass")
    private int usePass;

    @Column(name = "date")
    private LocalDate date;
}
