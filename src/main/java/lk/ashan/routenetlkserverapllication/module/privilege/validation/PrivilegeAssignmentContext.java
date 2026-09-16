package lk.ashan.routenetlkserverapllication.module.privilege.validation;

import lk.ashan.routenetlkserverapllication.module.privilege.model.entity.Module;
import lk.ashan.routenetlkserverapllication.module.privilege.model.entity.Operation;
import lk.ashan.routenetlkserverapllication.module.user.model.entity.Role;

public record PrivilegeAssignmentContext(
        Role role,
        Module module,
        Operation operation
) {
}