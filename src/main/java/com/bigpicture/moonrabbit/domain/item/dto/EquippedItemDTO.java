package com.bigpicture.moonrabbit.domain.item.dto;

import com.bigpicture.moonrabbit.domain.item.entity.ItemType;
import lombok.Getter;

@Getter
public class EquippedItemDTO {
    private ItemType type;
    private String imageUrl;

    public EquippedItemDTO(ItemType type, String imageUrl) {
        this.type = type;
        this.imageUrl = imageUrl;
    }
}