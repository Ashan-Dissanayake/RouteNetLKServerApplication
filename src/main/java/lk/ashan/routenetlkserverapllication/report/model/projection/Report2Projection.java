package lk.ashan.routenetlkserverapllication.report.model.projection;

import java.math.BigDecimal;

public interface Report2Projection {
    String getDepotName();
    BigDecimal getCashAmount();
    BigDecimal getDigitalAmount();
}
