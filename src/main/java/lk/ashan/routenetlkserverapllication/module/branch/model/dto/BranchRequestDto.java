package lk.ashan.routenetlkserverapllication.module.branch.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class BranchRequestDto {
    @NotBlank(message = "Name is mandatory")
    @Pattern(regexp = "^[A-Za-z0-9 .&'-]{1,30}$", message = "Invalid branch name format")
    private String name;

    @NotBlank(message = "Code is mandatory")
    @Pattern(regexp = "^[A-Z]{3}\\d{4}(-\\d+)?$", message = "Invalid code format")
    private String code;

    @NotBlank(message = "Address is mandatory")
    @Pattern(regexp = "^[A-Za-z0-9 ,.\\-'/]{0,50}$", message = "Invalid address format")
    private String address;

    @NotBlank(message = "Telephone number is mandatory")
    @Pattern(
            regexp = "^0(7\\d{8}|(11|21|23|24|25|26|27|31|32|33|34|35|36|37|38|41|45|51|52|54|55|57|63|65|66|67|81|91)\\d{7})$",
            message = "Invalid telephone number"
    )
    private String telephone;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Created date is mandatory")
    @PastOrPresent(message = "Creation date cannot be in the future")
    private LocalDate docreated;

    @Size(max = 500, message = "Remarks must not exceed 500 characters")
    private String remarks;

    @NotNull(message = "Branch type is mandatory")
    private BranchTypeDto branchtype;

    @NotNull(message = "Branch status is mandatory")
    private BranchStatusDto branchstatus;

    @NotNull(message = "Regional office is mandatory")
    private RegionalOfficeDto regionaloffice;


}
