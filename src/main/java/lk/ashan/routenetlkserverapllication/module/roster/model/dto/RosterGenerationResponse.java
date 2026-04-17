package lk.ashan.routenetlkserverapllication.module.roster.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RosterGenerationResponse {
   private Integer rosterId;
    private String message;
    private String status;
    private LocalDateTime startTime;
}
