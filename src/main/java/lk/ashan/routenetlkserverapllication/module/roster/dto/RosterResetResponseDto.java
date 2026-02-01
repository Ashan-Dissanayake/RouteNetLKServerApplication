package lk.ashan.routenetlkserverapllication.module.roster.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RosterResetResponseDto {

    private Integer branchId;
    private LocalDate date;

    private int resetRosterCount;
    private int cancelledAssignmentCount;

    private List<Integer> resetRosterIds;

    private String message;
}
