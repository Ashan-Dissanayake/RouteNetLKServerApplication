package lk.ashan.routenetlkserverapllication.shared.notification.controller;

import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lk.ashan.routenetlkserverapllication.shared.notification.model.Notification;
import lk.ashan.routenetlkserverapllication.shared.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // 1. SSE Stream Endpoint (SSE වලදී text/event-stream return වන නිසා ResponseBuilder එක පාවිච්චි කරන්නේ නැත)
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamNotifications(@RequestParam Integer userId) {
        return notificationService.subscribe(userId);
    }

    // 2. Fetch past notifications (List එකක් එන නිසා APIResponseBuilder.list පාවිච්චි කළ හැක)
    @GetMapping("/user/{userId}")
    public ResponseEntity<APISuccessResponse<List<Notification>>> getUserNotifications(@PathVariable Integer userId) {
        List<Notification> notifications = notificationService.getUserNotifications(userId);

        // Return using list builder with element count
        return APIResponseBuilder.list(notifications, notifications.size());
    }

    // 3. Mark as read (Content එකක් return නොවන නිසා, නැතහොත් OK response එකක් දෙන නිසා)
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<APISuccessResponse<Void>> markAsRead(@PathVariable Integer notificationId) {
        notificationService.markNotificationAsRead(notificationId);

        // Return success with no data payload (or APIResponseBuilder.ok(null))
        return APIResponseBuilder.ok(null);
    }
}
