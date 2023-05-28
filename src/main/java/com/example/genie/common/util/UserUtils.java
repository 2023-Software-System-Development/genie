package com.example.genie.common.util;

import com.example.genie.domain.auth.service.CustomUserDetails;
import com.example.genie.domain.user.entity.User;
import com.example.genie.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

@Component
@RequiredArgsConstructor
public class UserUtils {

    private final UserRepository userRepository;

    public User getUser(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
