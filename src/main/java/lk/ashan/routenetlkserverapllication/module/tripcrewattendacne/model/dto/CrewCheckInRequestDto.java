package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CrewCheckInRequestDto {

    @NotNull
    private Integer employeeId;
    private Integer attendanceId;
}
