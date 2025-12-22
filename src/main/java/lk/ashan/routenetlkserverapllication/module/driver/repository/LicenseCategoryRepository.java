package lk.ashan.routenetlkserverapllication.module.driver.repository;

import lk.ashan.routenetlkserverapllication.module.driver.model.Allowedbustype;
import lk.ashan.routenetlkserverapllication.module.driver.model.Licensecategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseCategoryRepository extends JpaRepository<Licensecategory, Integer> {
}
