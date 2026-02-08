package lk.ashan.routenetlkserverapllication.module.permit.model;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface ServiceTypeRepository extends JpaRepository<Servicetype, Integer> {
    Optional<Servicetype> findByName(String number);
}
