package com.bigpicture.moonrabbit.domain.example.aiservice.entity;

import com.bigpicture.moonrabbit.domain.board.entity.Board;
import com.bigpicture.moonrabbit.domain.example.aiservice.enums.AssistantCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssistantReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시글과 1:1 관계
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", unique = true)
    private Board board;

    @Enumerated(EnumType.STRING)
    private AssistantCategory category;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String userMessage;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String assistantResponse;
}