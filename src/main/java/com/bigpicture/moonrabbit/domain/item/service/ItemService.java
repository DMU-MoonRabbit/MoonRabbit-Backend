package com.bigpicture.moonrabbit.domain.item.service;

import com.bigpicture.moonrabbit.domain.item.entity.Item;
import org.springframework.data.domain.Page;

public interface ItemService {
    Page<Item> getAllItems(int page, int size);
    Item getItemDetail(Long itemId);
}