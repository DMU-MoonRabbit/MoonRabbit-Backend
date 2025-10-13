package com.bigpicture.moonrabbit.domain.item.service;

import com.bigpicture.moonrabbit.domain.item.entity.Item;
import com.bigpicture.moonrabbit.domain.item.repository.ItemRepository;
import com.bigpicture.moonrabbit.global.exception.CustomException;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public Page<Item> getAllItems(int page, int size) {
        return itemRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"))
        );
    }

    @Override
    public Item getItemDetail(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));
    }
}