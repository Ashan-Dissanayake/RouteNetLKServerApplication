package lk.ashan.routenetlkserverapllication.module.user.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserSummaryDto {
    private Integer id;
    private String username;
}
