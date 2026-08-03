package lk.ashan.routenetlkserverapllication.shared.notification.repository;

import lk.ashan.routenetlkserverapllication.shared.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByUser_IdOrderByTocreatedDesc(Integer userId);
}
