package com.bigpicture.moonrabbit.domain.user.service;

import com.bigpicture.moonrabbit.domain.user.entity.User;
//import com.bigpicture.moonrabbit.global.exception.UserAlreadyExistsException;
//import com.bigpicture.moonrabbit.global.exception.UserNotFoundException;
import com.bigpicture.moonrabbit.domain.user.repository.UserRepository;
import com.bigpicture.moonrabbit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.bigpicture.moonrabbit.global.exception.CustomException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // 유저 저장
    public User saveUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            CustomException.UserAlreadyExistsException();
        }
        return userRepository.save(user);
    }

    // 이메일로 유저 찾기
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
