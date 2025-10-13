package com.bigpicture.moonrabbit.domain.item.controller;

import com.bigpicture.moonrabbit.domain.item.entity.Item;
import com.bigpicture.moonrabbit.domain.item.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@Tag(name = "아이템 상점", description = "아이템 목록 및 상세 조회 API")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    @Operation(summary = "전체 아이템 조회", description = "상점에서 구매 가능한 전체 아이템 목록을 조회합니다.")
    public Page<Item> getAllItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        return itemService.getAllItems(page, size);
    }

    @GetMapping("/{itemId}")
    @Operation(summary = "아이템 상세 조회", description = "아이템 상세 정보를 조회합니다.")
    public Item getItemDetail(@PathVariable Long itemId) {
        return itemService.getItemDetail(itemId);
    }
}