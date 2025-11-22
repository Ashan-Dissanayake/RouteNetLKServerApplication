package lk.ashan.routenetlkserverapllication.module.vehicle.repository;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Integer> {
}
