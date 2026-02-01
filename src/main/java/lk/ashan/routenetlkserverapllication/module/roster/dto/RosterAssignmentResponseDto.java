package lk.ashan.routenetlkserverapllication.module.roster.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RosterAssignmentResponseDto {

    private Integer branchId;
    private LocalDate date;

    private int totalSlots;
    private int assignedSlots;
    private int unassignedSlots;

    private List<Integer> rosterIds;

    private String message;
}
