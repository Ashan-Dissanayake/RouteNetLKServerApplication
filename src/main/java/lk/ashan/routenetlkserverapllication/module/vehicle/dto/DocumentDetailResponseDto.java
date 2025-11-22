package lk.ashan.routenetlkserverapllication.module.vehicle.dto;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.Documentstatus;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Documenttype;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DocumentDetailResponseDto {
    private  Integer id;
    private  byte[] document;
    private Integer version;
    private LocalDate doi;
    private LocalDate doe;
    private String remarks;
    private Documenttype documenttype;
    private Documentstatus documentstatus;

}
