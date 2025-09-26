package lk.ashan.routenetlkserverapllication.module.branch.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class BranchRequestDto {
    @NotBlank(message = "Branch name is mandatory")
    @Pattern(regexp = "^[A-Za-z0-9 .&'-]{1,100}$", message = "Invalid branch name format")
    private String name;

    @NotBlank(message = "Branch code is mandatory")
    @Size(max = 7, message = "Branch code max length is 7")
    @Pattern(regexp = "^[A-Z]{3}\\d{4}(-\\d+)?$", message = "Branch code must be 3 uppercase letters, 4 digits, optional dash and branch number")
    private String code;

    @NotBlank(message = "Branch address is mandatory")
    @Pattern(regexp = "^[A-Za-z0-9 ,.\\-'/]{0,255}$", message = "Invalid address format")
    private String address;

    @NotBlank(message = "Branch telephone number is mandatory")
    @Pattern(regexp = "^\\+?[0-9]{10}$" , message = "Invalid telephone number")
    private String telephone;

    @NotBlank(message = "Branch email is mandatory")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Branch date of created is mandatory")
    @PastOrPresent(message = "Creation date cannot be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate docreated;

    @Size(max = 500, message = "Remarks must not exceed 500 characters")
    private String remarks;

    @NotNull(message = "Branch type is mandatory")
    private BranchtypeDto branchtype;

    @NotNull(message = "Branch status is mandatory")
    private BranchstatusDto branchstatus;

    @NotNull(message = "Branch coverages are mandatory")
    private Collection<BranchDistrictCoverageDto> branchcoverages;
}
