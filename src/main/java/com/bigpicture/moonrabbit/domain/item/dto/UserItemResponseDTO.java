package com.bigpicture.moonrabbit.domain.item.dto;

import com.bigpicture.moonrabbit.domain.item.entity.ItemType;
import com.bigpicture.moonrabbit.domain.item.entity.UserItem;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserItemResponseDTO {
    private Long id;            // UserItem id
    private Long itemId;        // Item id
    private String itemName;
    private int price;
    private ItemType type;
    private boolean equipped;
    private LocalDateTime createdAt;
    private String imageUrl;

    // 엔티티 기반 생성자 — service에서 .map(UserItemResponseDTO::new)으로 사용
    public UserItemResponseDTO(UserItem userItem) {
        this.id = userItem.getId();
        this.itemId = userItem.getItem().getId();
        this.itemName = userItem.getItem().getName();
        this.price = userItem.getItem().getPrice();
        this.type = userItem.getItem().getType();
        this.equipped = userItem.isEquipped();
        this.createdAt = userItem.getCreatedAt();
        this.imageUrl = userItem.getItem().getImageUrl();
    }
}
