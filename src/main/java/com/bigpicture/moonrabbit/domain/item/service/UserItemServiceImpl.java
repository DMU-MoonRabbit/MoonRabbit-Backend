package com.bigpicture.moonrabbit.domain.item.service;

import com.bigpicture.moonrabbit.domain.item.dto.EquippedItemDTO;
import com.bigpicture.moonrabbit.domain.item.dto.UserItemResponseDTO;
import com.bigpicture.moonrabbit.domain.item.entity.Item;
import com.bigpicture.moonrabbit.domain.item.entity.UserItem;
import com.bigpicture.moonrabbit.domain.item.repository.ItemRepository;
import com.bigpicture.moonrabbit.domain.item.repository.UserItemRepository;
import com.bigpicture.moonrabbit.domain.user.entity.User;
import com.bigpicture.moonrabbit.domain.user.repository.UserRepository;
import com.bigpicture.moonrabbit.domain.user.service.UserService;
import com.bigpicture.moonrabbit.global.exception.CustomException;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserItemServiceImpl implements UserItemService{
    private final UserItemRepository userItemRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;


    @Override
    public Page<UserItemResponseDTO> getUserItems(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return userItemRepository.findByUserId(userId, pageable)
                .map(userItem -> new UserItemResponseDTO(
                        userItem,
                        "" // content를 여기서 지정
                ));
    }


    @Override
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
        return new UserItemResponseDTO(userItem, "구매가 완료되었습니다.");
    }

    @Transactional
    @Override
    public UserItemResponseDTO equipItem(Long userItemId) {

        // 1. 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        UserItem userItem = userItemRepository.findById(userItemId)
                .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));

        // 3. 현재 사용자와 소유자가 일치하는지 확인
        if (!userItem.getUser().getId().equals(currentUser.getId())) {
            throw new CustomException(ErrorCode.USER_INCORRECT);
        }

        // 동일 타입의 다른 아이템이 장착되어 있으면 해제
        List<UserItem> equippedItems = userItemRepository.findByUserIdAndItemTypeAndEquipped(userItem.getUser().getId(),
                        userItem.getItem().getType(), true);
        for (UserItem ui : equippedItems) {
            ui.setEquipped(false);
            userItemRepository.save(ui); // 변경 사항 저장
        }

        userItem.setEquipped(true);
        return new UserItemResponseDTO(userItem, "아이템이 장착되었습니다.");
    }

    @Transactional
    @Override
    public UserItemResponseDTO unequipItem(Long userItemId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserItem userItem = userItemRepository.findById(userItemId)
                .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));

        // 소유자 확인
        if (!userItem.getUser().getId().equals(currentUser.getId())) {
            throw new CustomException(ErrorCode.USER_INCORRECT);
        }

        userItem.setEquipped(false);
        UserItem savedItem = userItemRepository.save(userItem);
        return new UserItemResponseDTO(savedItem, "아이템이 장착해제 되었습니다.");
    }

    @Override
    public List<EquippedItemDTO> getEquippedItems(Long userId) {
        return userItemRepository.findByUserIdAndEquipped(userId, true) // equipped가 true인 아이템만 조회
                .stream()
                .map(userItem -> new EquippedItemDTO(
                        userItem.getItem().getType(),
                        userItem.getItem().getImageUrl()
                ))
                .collect(Collectors.toList());
    }
}
