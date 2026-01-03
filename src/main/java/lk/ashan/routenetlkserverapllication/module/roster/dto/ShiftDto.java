package lk.ashan.routenetlkserverapllication.module.roster.dto;

import lombok.*;

import java.sql.Time;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ShiftDto {
    private Integer id;
    private Time tostarted;
    private Time toend;
    private ShiftTypeDto shifttype;
    private ShiftStatusDto shiftstatus;

}
