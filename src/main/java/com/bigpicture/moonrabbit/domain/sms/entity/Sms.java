package com.bigpicture.moonrabbit.domain.sms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Sms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String phone;

    private String certification;
}
