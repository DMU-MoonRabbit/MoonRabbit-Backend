package com.bigpicture.moonrabbit.domain.item.repository;

import com.bigpicture.moonrabbit.domain.item.entity.ItemType;
import com.bigpicture.moonrabbit.domain.item.entity.UserItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserItemRepository extends JpaRepository<UserItem,Long> {
    Page<UserItem> findByUserId(Long userId, Pageable pageable);
    boolean existsByUserIdAndItemId(Long userId, Long itemId);
    List<UserItem> findByUserIdAndItemTypeAndEquipped(Long userId, ItemType type, boolean equipped);
    List<UserItem> findByUserIdAndEquipped(Long userId, boolean equipped);
}
