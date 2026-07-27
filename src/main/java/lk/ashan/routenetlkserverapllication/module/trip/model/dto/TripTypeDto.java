package lk.ashan.routenetlkserverapllication.module.trip.model.dto;

import lombok.*;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) representing a type of trip.
 * This class is used to transfer trip type data between different layers of the application.
 * It includes fields for the trip type ID and name.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TripTypeDto {
    private Integer id;
    private String name;
}
