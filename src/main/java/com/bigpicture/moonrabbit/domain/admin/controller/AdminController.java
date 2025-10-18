package com.bigpicture.moonrabbit.domain.admin.controller;

import com.bigpicture.moonrabbit.domain.admin.dto.AdminResponseDTO;
import com.bigpicture.moonrabbit.domain.admin.dto.UserAdminResponseDTO;
import com.bigpicture.moonrabbit.domain.admin.service.AdminService;
import com.bigpicture.moonrabbit.domain.board.dto.BoardRequestDTO;
import com.bigpicture.moonrabbit.domain.board.dto.BoardResponseDTO;
import com.bigpicture.moonrabbit.domain.dailyquestion.dto.DailyQuestionRequestDTO;
import com.bigpicture.moonrabbit.domain.dailyquestion.dto.DailyQuestionResponseDTO;
import com.bigpicture.moonrabbit.domain.dailyquestion.service.DailyQuestionService;
import com.bigpicture.moonrabbit.domain.item.entity.Item;
import com.bigpicture.moonrabbit.domain.item.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final DailyQuestionService dailyQuestionService;
    private final ItemService itemService;

    @GetMapping("/users")
    @Operation(summary = "유저 목록 조회", description = "유저들의 목록을 조회하는 기능")
    public Page<UserAdminResponseDTO> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return adminService.getUserList(page, size);
    }

    @PutMapping("/users/{userId}/point")
    @Operation(summary = "특정 유저 포인트 지급/감소", description = "특정 유저의 포인트를 지급/감소 하는 기능")
    public UserAdminResponseDTO updateUserPoint(@PathVariable Long userId, @RequestParam int point) {
       return adminService.updateUserPoint(userId, point);
    }

    @PutMapping("/users/{userId}/trust")
    @Operation(summary = "특정 유저 신뢰도 지급/감소", description = "특정 유저의 신뢰도를 지급/감소 하는 기능")
    public UserAdminResponseDTO updateUserTrust(@PathVariable Long userId, @RequestParam int point) {
        return adminService.updateUserTrust(userId, point);
    }

    @PutMapping("/boards/{boardId}")
    @Operation(summary = "특정 게시물 수정", description = "특정 게시물을 수정하는 기능")
    public BoardResponseDTO updateBoardAsAdmin(@PathVariable Long boardId,
                                               @RequestBody BoardRequestDTO boardDTO) {
        return adminService.updateBoardAsAdmin(boardId, boardDTO);
    }

    @DeleteMapping("/boards/{boardId}")
    @Operation(summary = "특정 게시글 삭제하는 기능", description = "특정 게시글을 삭제하는 기능")
    public AdminResponseDTO deleteBoardAsAdmin(@PathVariable Long boardId) {
        adminService.deleteBoardAsAdmin(boardId);
        return new AdminResponseDTO("게시글 ID : "+boardId+" 삭제되었습니다.");
    }

    @PutMapping("/items/{itemId}")
    @Operation(summary = "특정 아이템 정보 수정", description = "관리자가 아이템 이름, 가격, 이미지 등을 수정")
    public ResponseEntity<Item> updateItemAsAdmin(
            @PathVariable Long itemId,
            @RequestParam String name,
            @RequestParam int price,
            @RequestParam String imageUrl
    ) {
        Item updatedItem = itemService.updateItem(itemId, name, price, imageUrl);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "특정 아이템 삭제", description = "관리자가 아이템을 영구적으로 삭제")
    public AdminResponseDTO deleteItemAsAdmin(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return new AdminResponseDTO("아이템 ID : "+itemId+" 삭제되었습니다.");
    }
}
