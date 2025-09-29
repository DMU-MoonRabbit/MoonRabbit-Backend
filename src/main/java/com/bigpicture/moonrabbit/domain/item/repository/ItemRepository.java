package com.bigpicture.moonrabbit.domain.item.repository;

import com.bigpicture.moonrabbit.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item,Long> {
}
