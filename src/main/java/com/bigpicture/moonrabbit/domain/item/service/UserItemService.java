package com.bigpicture.moonrabbit.domain.item.service;

import com.bigpicture.moonrabbit.domain.item.dto.EquippedItemDTO;
import com.bigpicture.moonrabbit.domain.item.dto.UserItemResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserItemService {

    Page<UserItemResponseDTO> getUserItems(Long userId, int page, int size);

    UserItemResponseDTO buyItem(Long userId, Long itemId);

    UserItemResponseDTO equipItem(Long userItemId);

    UserItemResponseDTO unequipItem(Long userItemId);

    List<EquippedItemDTO> getEquippedItems(Long userId);

}
