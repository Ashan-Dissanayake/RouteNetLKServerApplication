package lk.ashan.routenetlkserverapllication.module.user.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OperationDto {
    private String id;
    private String displayname;
    private String operation;
    private ModuleDto module;
}
