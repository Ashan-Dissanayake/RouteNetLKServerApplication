    package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.planner;

    import lk.ashan.routenetlkserverapllication.module.user.model.entity.Role;
    import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shift;
    import lk.ashan.routenetlkserverapllication.module.roster.planner.EmployeeFact;
    import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import org.optaplanner.core.api.domain.entity.PlanningEntity;
    import org.optaplanner.core.api.domain.lookup.PlanningId;
    import org.optaplanner.core.api.domain.variable.PlanningVariable;

    /**
     * Planning Entity for Trip Crew Allocation.
     *
     * Represents ONE role assignment for a trip (e.g., "Trip 123 needs a Driver").
     * OptaPlanner will assign an employee to fill this role.
     *
     * For each trip requiring crew, we create multiple planning entities:
     * - One for Driver role
     * - One for Conductor role
     *
     * Example:
     * Trip 123 (08:00-10:00, Route 4-7) creates 2 entities:
     * 1. TripCrewAssignmentPlanning(trip=123, role=Driver, assignedEmployee=null)
     * 2. TripCrewAssignmentPlanning(trip=123, role=Conductor, assignedEmployee=null)
     *
     * OptaPlanner fills assignedEmployee based on constraints.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @PlanningEntity
    public class TripCrewAssignmentPlanning {

        /**
         * Unique ID for this assignment entity.
         * Format: {tripId * 100 + roleId} to ensure uniqueness
         */
        @PlanningId
        private Integer id;

        /**
         * The trip requiring crew (problem fact - immutable).
         */
        private Trip trip;

        /**
         * The role to fill (Driver or Conductor) (problem fact - immutable).
         */
        private Role role;

        /**
         * The shift derived from trip departure time (problem fact - immutable).
         * Used to match with roster assignments.
         */
        private Shift derivedShift;

        /**
         * The employee assigned to this role (planning variable - OptaPlanner changes this).
         *
         * This is THE variable OptaPlanner optimizes.
         * Starts as null, gets filled during solving.
         *
         * ValueRangeProvider "employeeRange" provides the list of candidate employees.
         */
        @PlanningVariable(valueRangeProviderRefs = "employeeRange")
        private EmployeeFact assignedEmployee;

        // Helper methods for constraints

        /**
         * Check if this assignment has been filled.
         */
        public boolean isAssigned() {
            return assignedEmployee != null;
        }

        /**
         * Get the employee ID (null-safe).
         */
        public Integer getAssignedEmployeeId() {
            return assignedEmployee != null ? assignedEmployee.getId() : null;
        }

        /**
         * Get trip ID.
         */
        public Integer getTripId() {
            return trip != null ? trip.getId() : null;
        }

        /**
         * Get role ID.
         */
        public Integer getRoleId() {
            return role != null ? role.getId() : null;
        }

        /**
         * Get role name.
         */
        public String getRoleName() {
            return role != null ? role.getName() : null;
        }

        /**
         * Get shift ID.
         */
        public Integer getShiftId() {
            return derivedShift != null ? derivedShift.getId() : null;
        }

        /**
         * Get branch ID from trip.
         */
        public Integer getTripBranchId() {
            return trip != null && trip.getBranch() != null
                    ? trip.getBranch().getId()
                    : null;
        }

        /**
         * Get route ID from trip permit.
         */
        public Integer getTripRouteId() {
            return trip != null && trip.getPermite() != null && trip.getPermite().getRoute() != null
                    ? trip.getPermite().getRoute().getId()
                    : null;
        }

        /**
         * Get vehicle bus type ID from trip permit.
         */
        public Integer getTripVehicleBusTypeId() {
            return trip != null
                    && trip.getPermite() != null
                    && trip.getPermite().getVehicle() != null
                    && trip.getPermite().getVehicle().getBustype() != null
                    ? trip.getPermite().getVehicle().getBustype().getId()
                    : null;
        }

        @Override
        public String toString() {
            return String.format("TripCrewAssignment[id=%d, trip=%d, role=%s, shift=%s, employee=%s]",
                    id,
                    getTripId(),
                    getRoleName(),
                    derivedShift != null ? derivedShift.getName() : "null",
                    assignedEmployee != null ? assignedEmployee.getNumber() : "UNASSIGNED"
            );
        }
    }
