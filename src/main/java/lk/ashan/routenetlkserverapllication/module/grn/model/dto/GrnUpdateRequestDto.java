package lk.ashan.routenetlkserverapllication.module.grn.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GrnUpdateRequestDto {
    @NotNull(message = "GRN ID is mandatory")
    private Integer id;
    @NotNull(message = "Received is mandatory")
    private LocalDate doreceived;
    private String remarks;
    @NotNull(message = "GRN parts are mandatory")
    private List<GrnPartRequestItemDto> grnpartrequestitems;
}
