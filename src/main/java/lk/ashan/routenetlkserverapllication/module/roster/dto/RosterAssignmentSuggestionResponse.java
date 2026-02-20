package lk.ashan.routenetlkserverapllication.module.roster.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RosterAssignmentSuggestionResponse {
    private Integer rosterId;

    private Integer totalAssignmentsNeeded;

    private Integer assignmentsFilled;

    private Integer assignmentsUnfilled;

    private String score;

    private Boolean feasible;

    private String message;
}
