package com.itt.newsAggregation.service.impl;

import com.itt.newsAggregation.dto.common.NotificationPreferenceDto;
import com.itt.newsAggregation.model.Category;
import com.itt.newsAggregation.model.NotificationPreference;
import com.itt.newsAggregation.model.User;
import com.itt.newsAggregation.repository.CategoryRepository;
import com.itt.newsAggregation.repository.NotificationPreferenceRepository;
import com.itt.newsAggregation.repository.UserRepository;
import com.itt.newsAggregation.service.NotificationPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationPreferenceImpl implements NotificationPreferenceService {

    @Autowired
    private NotificationPreferenceRepository notificationPreferenceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public NotificationPreferenceDto saveNotificationPreference(NotificationPreferenceDto notificationPreferenceDto) {
        Optional<User> byUsername = userRepository.findByUsername(notificationPreferenceDto.getUsername());
        Optional<Category> byCategory = categoryRepository.findByName(notificationPreferenceDto.getCategory());
        NotificationPreference notificationPreference = new NotificationPreference();
        if (byUsername.isPresent() && byCategory.isPresent()) {
            notificationPreference.setUser(byUsername.get());
            notificationPreference.setCategory(byCategory.get());
            notificationPreference.setEnabled(notificationPreferenceDto.getIsEnabled());
            NotificationPreference savedNotification = notificationPreferenceRepository.save(notificationPreference);
            return mapToDto(savedNotification);
        }
        return  null;
    }

    @Override
    public List<NotificationPreferenceDto> getAllNotificationPreferences(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            List<NotificationPreference> preferences = notificationPreferenceRepository.findByUser(user.get());
            List<NotificationPreferenceDto> list = preferences.stream()
                    .map(pref -> new NotificationPreferenceDto(pref.getUser().getUsername(), pref.getCategory().getName(), pref.getEnabled()))
                    .toList();
            return list;
        }
        return List.of();
    }

    public List<NotificationPreferenceDto> getAllNotificationPreferences() {
        List<NotificationPreference> notificationPreferences = notificationPreferenceRepository.findAll();
        if(notificationPreferences.isEmpty()) {
            return List.of();
        }
        return  notificationPreferences.stream()
                .map(pref -> new NotificationPreferenceDto(pref.getUser().getUsername(), pref.getCategory().getName(), pref.getEnabled()))
                .toList();
    }

    @Override
    public NotificationPreferenceDto updateNotificationPreference(NotificationPreferenceDto notificationPreferenceDto) {
        Optional<User> byUsername = userRepository.findByUsername(notificationPreferenceDto.getUsername());
        Optional<Category> byCategory = categoryRepository.findByName(notificationPreferenceDto.getCategory());
        if (byUsername.isPresent() && byCategory.isPresent()) {
            NotificationPreference notificationPreference = notificationPreferenceRepository
                    .findByUserAndCategory(byUsername.get(), byCategory.get());
            if (notificationPreference != null) {
                notificationPreference.setEnabled(notificationPreferenceDto.getIsEnabled());
                notificationPreferenceRepository.save(notificationPreference);
                return mapToDto(notificationPreference);
            }
        }
        return null;
    }

    private NotificationPreferenceDto mapToDto(NotificationPreference notificationPreference) {
        return NotificationPreferenceDto.builder()
                .username(notificationPreference.getUser().getUsername())
                .category(notificationPreference.getCategory().getName())
                .isEnabled(notificationPreference.getEnabled())
                .build();
    }

}
