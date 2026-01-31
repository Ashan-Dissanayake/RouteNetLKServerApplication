package lk.ashan.routenetlkserverapllication.module.roster.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RosterConfirmationResponseDto {
    Integer rosterId;
    Integer branchId;
    LocalDate date;
    String message;
}
