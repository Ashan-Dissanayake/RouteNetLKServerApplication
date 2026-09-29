package lk.ashan.routenetlkserverapllication.shared.notification.service;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
import lk.ashan.routenetlkserverapllication.module.roster.event.RosterShiftStatusChangedEvent;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.Role;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.UserRole;
import lk.ashan.routenetlkserverapllication.module.user.repository.UserRepository;
import lk.ashan.routenetlkserverapllication.shared.notification.model.AppRoles;
import lk.ashan.routenetlkserverapllication.shared.notification.model.Notification;
import lk.ashan.routenetlkserverapllication.shared.notification.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationService notificationService;


    @Test
    void shouldCreateRosterStatusNotification() {

        Branch branch = new Branch();
        branch.setId(10);

        RosterShiftStatusChangedEvent event =
                new RosterShiftStatusChangedEvent(
                        1,
                        100,
                        10,
                        "DRIVER",
                        "PROPOSED",
                        "CONFIRMED"
                );

        when(branchRepository.findById(10))
                .thenReturn(Optional.of(branch));

        when(userRepository.findByEmployee_Branch_Id(10))
                .thenReturn(Collections.emptyList());

        notificationService.createRosterStatusNotification(event);

        verify(branchRepository).findById(10);

        verify(userRepository)
                .findByEmployee_Branch_Id(10);
    }

    @Test
    void shouldSaveNotificationForMatchingBranchRole() {

        Branch branch = new Branch();
        branch.setId(10);

        Role role = new Role();
        role.setName("DEPOT_MANAGER");

        UserRole userRole = new UserRole();
        userRole.setRole(role);

        User user = new User();
        user.setId(50);
        user.setUserRoles(List.of(userRole));

        when(userRepository.findByEmployee_Branch_Id(10))
                .thenReturn(List.of(user));

        Notification savedNotification = new Notification();
        savedNotification.setId(1);

        when(notificationRepository.save(any(Notification.class)))
                .thenReturn(savedNotification);

        notificationService.sendNotificationToBranchAndRole(
                branch,
                AppRoles.DEPOT_MANAGER,
                "Test Title",
                "Test Message"
        );

        verify(notificationRepository).save(
                argThat(notification ->
                        notification.getBranch() == branch
                                && notification.getUser() == user
                                && notification.getTitle().equals("Test Title")
                                && notification.getMessage().equals("Test Message")
                                && Boolean.FALSE.equals(notification.getIsread())
                )
        );
    }

    @Test
    void shouldSendNotificationThroughSseWhenUserIsConnected() throws Exception {

        Branch branch = new Branch();
        branch.setId(10);

        Role role = new Role();
        role.setName("DEPOT_MANAGER");

        UserRole userRole = new UserRole();
        userRole.setRole(role);

        User user = new User();
        user.setId(50);
        user.setUserRoles(List.of(userRole));

        Notification savedNotification = new Notification();
        savedNotification.setId(1);

        when(userRepository.findByEmployee_Branch_Id(10))
                .thenReturn(List.of(user));

        when(notificationRepository.save(any(Notification.class)))
                .thenReturn(savedNotification);

        SseEmitter emitter = Mockito.spy(
                new SseEmitter(Long.MAX_VALUE)
        );

        ReflectionTestUtils.setField(
                notificationService,
                "emitters",
                new ConcurrentHashMap<>(Map.of(user.getId(), emitter))
        );

        notificationService.sendNotificationToBranchAndRole(
                branch,
                AppRoles.DEPOT_MANAGER,
                "Test Title",
                "Test Message"
        );

        verify(notificationRepository)
                .save(any(Notification.class));

        verify(emitter).send(
                any(SseEmitter.SseEventBuilder.class)
        );
    }

    @Test
    void shouldRemoveEmitterWhenSseSendFails() throws Exception {

        Branch branch = new Branch();
        branch.setId(10);

        Role role = new Role();
        role.setName("DEPOT_MANAGER");

        UserRole userRole = new UserRole();
        userRole.setRole(role);

        User user = new User();
        user.setId(50);
        user.setUserRoles(List.of(userRole));

        Notification savedNotification = new Notification();
        savedNotification.setId(1);

        when(userRepository.findByEmployee_Branch_Id(10))
                .thenReturn(List.of(user));

        when(notificationRepository.save(any(Notification.class)))
                .thenReturn(savedNotification);

        SseEmitter emitter = Mockito.mock(SseEmitter.class);

        doThrow(new IOException("SSE connection failed"))
                .when(emitter)
                .send(any(SseEmitter.SseEventBuilder.class));

        Map<Integer, SseEmitter> emitters = new ConcurrentHashMap<>();
        emitters.put(user.getId(), emitter);

        ReflectionTestUtils.setField(
                notificationService,
                "emitters",
                emitters
        );

        notificationService.sendNotificationToBranchAndRole(
                branch,
                AppRoles.DEPOT_MANAGER,
                "Test Title",
                "Test Message"
        );

        verify(emitter)
                .send(any(SseEmitter.SseEventBuilder.class));

        assertFalse(
                emitters.containsKey(user.getId())
        );
    }

    @Test
    void shouldNotSaveNotificationForNonMatchingRole() {

        Branch branch = new Branch();
        branch.setId(10);

        Role role = new Role();
        role.setName("MAINTENANCE_OFFICER");

        UserRole userRole = new UserRole();
        userRole.setRole(role);

        User user = new User();
        user.setId(50);
        user.setUserRoles(List.of(userRole));

        when(userRepository.findByEmployee_Branch_Id(10))
                .thenReturn(List.of(user));

        notificationService.sendNotificationToBranchAndRole(
                branch,
                AppRoles.DEPOT_MANAGER,
                "Test Title",
                "Test Message"
        );

        verify(notificationRepository, never())
                .save(any(Notification.class));
    }
}