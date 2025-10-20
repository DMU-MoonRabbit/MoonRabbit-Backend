package com.bigpicture.moonrabbit.domain.fine.repository;

import com.bigpicture.moonrabbit.domain.fine.entity.FineDataset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FineDatasetRepository extends JpaRepository<FineDataset, Long> {
}
