package lk.ashan.routenetlkserverapllication.module.privilege.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OperationDto {
    private Integer id;
    private String displayname;
    private String operation;
    private ModuleDto module;
}
