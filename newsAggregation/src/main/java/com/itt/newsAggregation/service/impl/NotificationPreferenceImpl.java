package com.itt.newsAggregation.service.impl;

import com.itt.newsAggregation.dto.NotificationPreferenceDto;
import com.itt.newsAggregation.model.Category;
import com.itt.newsAggregation.model.NotificationPreference;
import com.itt.newsAggregation.model.User;
import com.itt.newsAggregation.repositoy.CategoryRepository;
import com.itt.newsAggregation.repositoy.NotificationPreferenceRepository;
import com.itt.newsAggregation.repositoy.UserRepository;
import com.itt.newsAggregation.service.NotificationPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class NotificationPreferenceImpl implements NotificationPreferenceService {

    @Autowired
    private NotificationPreferenceRepository notificationPreferenceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public boolean saveNotificationPreference(String username, String category, boolean isEnabled) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        Optional<Category> byCategory = categoryRepository.findByName(category);
        NotificationPreference notificationPreference = new NotificationPreference();
        if (byUsername.isPresent() && byCategory.isPresent()) {
            notificationPreference.setUser(byUsername.get());
            notificationPreference.setCategory(byCategory.get());
            notificationPreference.setEnabled(isEnabled);
            notificationPreferenceRepository.save(notificationPreference);
            return true;
        }
        return false;
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
    public boolean updateNotificationPreference(String username, String category, boolean isEnabled) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        Optional<Category> byCategory = categoryRepository.findByName(category);
        if (byUsername.isPresent() && byCategory.isPresent()) {
            NotificationPreference notificationPreference = notificationPreferenceRepository
                    .findByUserAndCategory(byUsername.get(), byCategory.get());
            if (notificationPreference != null) {
                notificationPreference.setEnabled(isEnabled);
                notificationPreferenceRepository.save(notificationPreference);
                return true;
            }
        }
        return false;
    }

}
