package com.bigpicture.moonrabbit.domain.item.controller;

import com.bigpicture.moonrabbit.domain.item.dto.UserItemResponseDTO;
import com.bigpicture.moonrabbit.domain.item.service.UserItemService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/items")
@RequiredArgsConstructor
public class UserItemController {
    private final UserItemService userItemService;

    @GetMapping("/{userId}")
    @Operation(summary = "유저 인벤토리 조회", description = "특정 유저가 보유한 아이템 목록을 조회합니다.")
    public Page<UserItemResponseDTO> getUserItems(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return userItemService.getUserItems(userId, page, size);
    }

    @PostMapping("/buy")
    @Operation(summary = "아이템 구매", description = "유저가 아이템을 구매하는 기능")
    public UserItemResponseDTO buyItem(
            @RequestParam Long userId,
            @RequestParam Long itemId
    ) {
        return userItemService.buyItem(userId, itemId);
    }

    @PutMapping("/{userItemId}/equip")
    @Operation(summary = "아이템 장착", description = "유저가 아이템을 장착하는 기능")
    public UserItemResponseDTO equipItem(@PathVariable Long userItemId) {
        return userItemService.equipItem(userItemId);
    }

    @PutMapping("/{userItemId}/unequip")
    @Operation(summary = "아이템 장착해제", description = "유저가 아이템을 장착해제하는 기능")
    public UserItemResponseDTO unequipItem(@PathVariable Long userItemId) {
        return userItemService.unequipItem(userItemId);
    }
}
