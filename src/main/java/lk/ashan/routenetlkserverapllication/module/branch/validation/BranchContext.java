package lk.ashan.routenetlkserverapllication.module.branch.validation;

import lombok.Builder;
import lombok.Data;

/**
 * Represents the context of a branch with its details.
 * This class is used to encapsulate branch-related information.
 */
@Data
@Builder
public class BranchContext {

    /** The unique identifier of the branch. */
    private Integer id;

    /** The code representing the branch. */
    private String code;

    /** The name of the branch. */
    private String name;

    /** The email address associated with the branch. */
    private String email;

    /** The telephone number of the branch. */
    private String telephone;

    /** The physical address of the branch. */
    private String address;

    /** The status ID representing the branch's current status. */
    private Integer branchStatusId;
}
