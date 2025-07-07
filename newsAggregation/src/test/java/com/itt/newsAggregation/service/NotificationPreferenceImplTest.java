package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.common.NotificationPreferenceDto;
import com.itt.newsAggregation.model.Category;
import com.itt.newsAggregation.model.NotificationPreference;
import com.itt.newsAggregation.model.User;
import com.itt.newsAggregation.repository.CategoryRepository;
import com.itt.newsAggregation.repository.NotificationPreferenceRepository;
import com.itt.newsAggregation.repository.UserRepository;
import com.itt.newsAggregation.service.impl.NotificationPreferenceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationPreferenceImplTest {

    @InjectMocks
    private NotificationPreferenceImpl service;

    @Mock
    private NotificationPreferenceRepository preferenceRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private CategoryRepository categoryRepo;

    private final User user = User.builder().id(1).username("john").build();
    private final Category category = Category.builder().id(1).name("Tech").build();
    private final NotificationPreference preference = NotificationPreference.builder()
            .user(user)
            .category(category)
            .enabled(true)
            .build();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void savePreference_ShouldReturnDto_WhenUserAndCategoryExist() {
        NotificationPreferenceDto dto = new NotificationPreferenceDto("john", "Tech", true);

        when(userRepo.findByUsername("john")).thenReturn(Optional.of(user));
        when(categoryRepo.findByName("Tech")).thenReturn(Optional.of(category));
        when(preferenceRepo.save(any())).thenReturn(preference);

        NotificationPreferenceDto result = service.saveNotificationPreference(dto);

        assertNotNull(result);
        assertEquals("john", result.getUsername());
        assertEquals("Tech", result.getCategory());
        assertTrue(result.getIsEnabled());
    }

    @Test
    void savePreference_ShouldReturnNull_WhenUserOrCategoryMissing() {
        NotificationPreferenceDto dto = new NotificationPreferenceDto("unknown", "Tech", true);

        when(userRepo.findByUsername("unknown")).thenReturn(Optional.empty());

        assertNull(service.saveNotificationPreference(dto));
    }

    @Test
    void updatePreference_ShouldUpdate_WhenExists() {
        NotificationPreferenceDto dto = new NotificationPreferenceDto("john", "Tech", false);
        NotificationPreference updated = NotificationPreference.builder().user(user).category(category).enabled(false).build();

        when(userRepo.findByUsername("john")).thenReturn(Optional.of(user));
        when(categoryRepo.findByName("Tech")).thenReturn(Optional.of(category));
        when(preferenceRepo.findByUserAndCategory(user, category)).thenReturn(preference);
        when(preferenceRepo.save(any())).thenReturn(updated);

        NotificationPreferenceDto result = service.updateNotificationPreference(dto);

        assertNotNull(result);
        assertFalse(result.getIsEnabled());
    }

    @Test
    void updatePreference_ShouldReturnNull_WhenNotFound() {
        NotificationPreferenceDto dto = new NotificationPreferenceDto("john", "Tech", true);

        when(userRepo.findByUsername("john")).thenReturn(Optional.of(user));
        when(categoryRepo.findByName("Tech")).thenReturn(Optional.of(category));
        when(preferenceRepo.findByUserAndCategory(user, category)).thenReturn(null);

        assertNull(service.updateNotificationPreference(dto));
    }

    @Test
    void getAllPreferencesByUsername_ShouldReturnList_WhenUserExists() {
        when(userRepo.findByUsername("john")).thenReturn(Optional.of(user));
        when(preferenceRepo.findByUser(user)).thenReturn(List.of(preference));

        List<NotificationPreferenceDto> result = service.getAllNotificationPreferences("john");

        assertEquals(1, result.size());
        assertEquals("Tech", result.get(0).getCategory());
    }

    @Test
    void getAllPreferencesByUsername_ShouldReturnEmpty_WhenUserNotFound() {
        when(userRepo.findByUsername("john")).thenReturn(Optional.empty());

        List<NotificationPreferenceDto> result = service.getAllNotificationPreferences("john");

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllNotificationPreferences_ShouldReturnAllPreferences() {
        when(preferenceRepo.findAll()).thenReturn(List.of(preference));

        List<NotificationPreferenceDto> result = service.getAllNotificationPreferences();

        assertEquals(1, result.size());
        assertEquals("john", result.get(0).getUsername());
    }

    @Test
    void getAllNotificationPreferences_ShouldReturnEmpty_WhenNoneExist() {
        when(preferenceRepo.findAll()).thenReturn(List.of());

        List<NotificationPreferenceDto> result = service.getAllNotificationPreferences();

        assertTrue(result.isEmpty());
    }
}
