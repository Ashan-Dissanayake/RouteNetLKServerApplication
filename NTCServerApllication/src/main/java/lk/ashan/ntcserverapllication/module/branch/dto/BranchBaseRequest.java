package lk.ashan.ntcserverapllication.module.branch.dto;

import jakarta.validation.constraints.*;
import lk.ashan.ntcserverapllication.module.branch.model.Branchcoverage;
import lk.ashan.ntcserverapllication.module.branch.model.Branchstatus;
import lk.ashan.ntcserverapllication.module.branch.model.Branchtype;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Date;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BranchBaseRequest {
    @NotBlank(message = "Branch name is mandatory")
    @Pattern(regexp = "^[\\p{L} .'-]{1,100}$", message = "Invalid branch name format")
    private String name;
    @NotBlank(message = "Branch code is mandatory")
    @Size(max = 20, message = "Branch code max length is 20")
    @Pattern(regexp = "^[A-Z]{3}\\d{4}(-\\d+)?$", message = "Branch code must be 3 uppercase letters, 4 digits, optional dash and branch number")
    private String code;
    @Pattern(regexp = "^[\\p{L}0-9 ,.\\-'/]{0,255}$", message = "Invalid address format")
    private String address;
    @Pattern(regexp = "\\+?[0-9]{7,15}", message = "Invalid telephone number")
    private String telephone;
    @Email(message = "Invalid email format")
    private String email;
    @PastOrPresent(message = "Creation date cannot be in the future")
    private Date docreated;
    @Size(max = 500, message = "Remarks must not exceed 500 characters")
    private String remarks;
    @NotNull(message = "Branch type is mandatory")
    private Branchtype branchtype;
    @NotNull(message = "Branch status is mandatory")
    private Branchstatus branchstatus;
    @NotNull(message = "Branch coverages are mandatory")
    private Collection<Branchcoverage> branchcoverages;
}
