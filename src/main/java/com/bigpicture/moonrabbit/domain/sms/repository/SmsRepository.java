package com.bigpicture.moonrabbit.domain.sms.repository;

import com.bigpicture.moonrabbit.domain.sms.entity.Sms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsRepository extends JpaRepository<Sms, Long> {
    Sms findByPhone(String phone);
    void deleteByPhone(String phone);
}
