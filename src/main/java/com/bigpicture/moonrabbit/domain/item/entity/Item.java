package com.bigpicture.moonrabbit.domain.item.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;   // 아이템 이름

    @Column(nullable = false)
    private int price;     // 가격 (포인트)

    @Enumerated(EnumType.STRING)
    private ItemType type; // 아이템 타입 (예: BADGE, BACKGROUND, AVATAR 등)

    private boolean equippable = true; // 장착 가능한 아이템인지 여부

    private String imageUrl;
}
