package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripCrewAllocationSuggestionResponseDto {
    private Integer tripId;
    private LocalDate doservice;
    private LocalTime todepature;
    private boolean feasible;
    private String score;
    private List<TripCrewAllocationDetailResponseDto> suggestedAllocations;
    private Integer assignmentsFilled;
    private Integer assignmentsUnfilled;
}
