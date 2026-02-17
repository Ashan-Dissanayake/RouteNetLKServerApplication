package lk.ashan.routenetlkserverapllication.module.permit.repository;

import lk.ashan.routenetlkserverapllication.module.permit.model.Servicetype;
import lk.ashan.routenetlkserverapllication.shared.validation.RegexPattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface ServiceTypeRepository extends JpaRepository<Servicetype, Integer> {
    Optional<Servicetype> findByName(String number);
}
