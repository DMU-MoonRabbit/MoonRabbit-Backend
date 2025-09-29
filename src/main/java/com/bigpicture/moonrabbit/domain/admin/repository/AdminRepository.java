package com.bigpicture.moonrabbit.domain.admin.repository;

import com.bigpicture.moonrabbit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<User, Long> {

}
