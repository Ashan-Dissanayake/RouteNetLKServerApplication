package lk.ashan.routenetlkserverapllication.module.trip.model.dto;

import lombok.*;

/**
 * Data Transfer Object (DTO) representing an Origin Terminal.
 * This class is used to transfer data related to origin terminals
 * between different layers of the application.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OriginTerminalDto {
    private Integer id;
    private String name;
}
