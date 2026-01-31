package lk.ashan.routenetlkserverapllication.module.roster.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RosterConfirmationRequestDto {
    @NotNull
    Integer branchId;
    @NotNull
    LocalDate date;
    @NotNull
    Boolean confirm;
    @Size(max=255)
    String reason;
}
