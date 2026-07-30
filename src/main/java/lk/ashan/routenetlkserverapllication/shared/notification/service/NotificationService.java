package lk.ashan.routenetlkserverapllication.shared.notification.service;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.User;
import lk.ashan.routenetlkserverapllication.module.user.repository.UserRepository;
import lk.ashan.routenetlkserverapllication.shared.notification.entity.Notification;
import lk.ashan.routenetlkserverapllication.shared.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final Map<Integer, SseEmitter> emitters = new ConcurrentHashMap<>();

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public SseEmitter subscribe(Integer userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        emitter.onError((ex) -> emitters.remove(userId));

        try {
            emitter.send(SseEmitter.event().name("INIT").data("Connected successfully"));
        } catch (IOException e) {
            emitters.remove(userId);
        }

        return emitter;
    }

    @Transactional(readOnly = true)
    public List<Notification> getUserNotifications(Integer userId) {
        return notificationRepository.findByUser_IdOrderByTocreatedDesc(userId);
    }

    @Transactional
    public void markNotificationAsRead(Integer notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with ID: " + notificationId));

        notification.setIsread(true);
        notificationRepository.save(notification);
    }

    @Transactional
    public void sendNotificationToBranchAndRole(Branch branch, String targetRoleName, String title, String message) {
        List<User> branchUsers = userRepository.findByEmployee_Branch_Id(branch.getId());

        for (User user : branchUsers) {
            // Check Many-to-Many roles list for the required role
            boolean hasRole = user.getUserRoles().stream()
                    .anyMatch(userRole -> userRole.getRole().getName().equalsIgnoreCase(targetRoleName));

            if (hasRole) {
                // A. Save to Database
                Notification notification = Notification.builder()
                        .branch(branch)
                        .user(user)
                        .title(title)
                        .message(message)
                        .isread(false)
                        .tocreated(LocalDateTime.now())
                        .build();

                Notification savedNotification = notificationRepository.save(notification);

                // B. Push live via SSE if user is currently connected
                SseEmitter emitter = emitters.get(user.getId());
                if (emitter != null) {
                    try {
                        emitter.send(SseEmitter.event().name("notification").data(savedNotification));
                    } catch (IOException e) {
                        emitters.remove(user.getId());
                    }
                }
            }
        }
    }
}
