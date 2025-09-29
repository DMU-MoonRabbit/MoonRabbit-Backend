package com.bigpicture.moonrabbit.domain.item.service;

import com.bigpicture.moonrabbit.domain.item.dto.UserItemResponseDTO;
import com.bigpicture.moonrabbit.domain.item.entity.Item;
import com.bigpicture.moonrabbit.domain.item.entity.UserItem;
import com.bigpicture.moonrabbit.domain.item.repository.ItemRepository;
import com.bigpicture.moonrabbit.domain.item.repository.UserItemRepository;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.domain.user.repository.UserRepository;
import com.bigpicture.moonrabbit.global.exception.CustomException;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class UserItemServiceImpl implements UserItemService{
    private final UserItemRepository userItemRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public Page<UserItemResponseDTO> getUserItems(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return userItemRepository.findByUserId(userId, pageable)
                .map(UserItemResponseDTO::new);
    }

    @Transactional
    public UserItemResponseDTO buyItem(Long userId, Long itemId) {
        // 1. 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 2. 아이템 조회
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));

        // 재구매 방지 로직
        if(userItemRepository.existsByUserIdAndItemId(userId, itemId)){
            throw new CustomException(ErrorCode.ALREADY_OWNED_ITEM);
        }

        // 3. 포인트 충분한지 체크
        if (user.getPoint() < item.getPrice()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_POINT);
        }

        // 4. 포인트 차감
        user.changePoint(-item.getPrice());

        // 5. UserItem 생성 및 저장
        UserItem userItem = new UserItem();
        userItem.setUser(user);
        userItem.setItem(item);
        userItem.setEquipped(false); // 기본 장착 상태는 false
        userItem.setCreatedAt(LocalDateTime.now());

        // UserItem userItem = new UserItem(user, item);

        userItemRepository.save(userItem);

        // 6. DTO 반환
        return new UserItemResponseDTO(userItem);
    }
}
