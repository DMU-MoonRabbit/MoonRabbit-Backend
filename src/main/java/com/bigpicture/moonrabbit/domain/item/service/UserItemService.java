package com.bigpicture.moonrabbit.domain.item.service;

import com.bigpicture.moonrabbit.domain.item.dto.UserItemResponseDTO;
import org.springframework.data.domain.Page;

public interface UserItemService {

    public Page<UserItemResponseDTO> getUserItems(Long userId, int page, int size);

    public UserItemResponseDTO buyItem(Long userId, Long itemId);

    public UserItemResponseDTO equipItem(Long userItemId);

    public UserItemResponseDTO unequipItem(Long userItemId);

}
