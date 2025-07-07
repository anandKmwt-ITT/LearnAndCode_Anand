package com.itt.newsAggregation.service;

import com.itt.newsAggregation.exception.UserNotFoundException;
import com.itt.newsAggregation.model.Notification;
import com.itt.newsAggregation.model.User;
import com.itt.newsAggregation.repository.NotificationRepository;
import com.itt.newsAggregation.repository.UserRepository;
import com.itt.newsAggregation.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {

    @InjectMocks
    private NotificationServiceImpl service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationRepository notificationRepository;

    private User user;
    private Notification notification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1)
                .username("john")
                .build();

        notification = Notification.builder()
                .id(101)
                .message("New article available!")
                .user(user)
                .build();
    }

    @Test
    void getNotifications_shouldReturnList_whenNotificationsExist() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(notificationRepository.findByUser(user)).thenReturn(List.of(notification));

        List<Notification> result = service.getNotifications("john");

        assertEquals(1, result.size());
        assertEquals("New article available!", result.get(0).getMessage());
        verify(userRepository).findByUsername("john");
        verify(notificationRepository).findByUser(user);
    }

    @Test
    void getNotifications_shouldReturnEmptyList_whenNoNotifications() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(notificationRepository.findByUser(user)).thenReturn(Collections.emptyList());

        List<Notification> result = service.getNotifications("john");

        assertTrue(result.isEmpty());
    }

    @Test
    void getNotifications_shouldThrowException_whenUserNotFound() {
        when(userRepository.findByUsername("invalid")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.getNotifications("invalid"));
    }

    @Test
    void saveNotification_shouldSaveSuccessfully() {
        when(notificationRepository.save(notification)).thenReturn(notification);

        assertDoesNotThrow(() -> service.saveNotification(notification));

        verify(notificationRepository).save(notification);
    }

    @Test
    void saveNotification_shouldLogError_onException() {
        when(notificationRepository.save(notification)).thenThrow(new RuntimeException("DB error"));

        assertDoesNotThrow(() -> service.saveNotification(notification));

        verify(notificationRepository).save(notification);
    }
}
