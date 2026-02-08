package lk.ashan.routenetlkserverapllication.module.permit.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;


@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class PermitCreateRequestDto extends PermitRequestDto{

}
