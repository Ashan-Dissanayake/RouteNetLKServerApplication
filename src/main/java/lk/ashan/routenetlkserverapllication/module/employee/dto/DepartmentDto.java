package lk.ashan.routenetlkserverapllication.module.employee.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DepartmentDto {
    Integer id;
    String name;
}
