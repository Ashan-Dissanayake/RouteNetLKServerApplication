package lk.ashan.routenetlkserverapllication.module.roster.planner;

import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.api.solver.SolverManager;
import ai.timefold.solver.core.config.solver.SolverConfig;
import ai.timefold.solver.core.config.solver.termination.TerminationConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
public class RosterSolverConfig {

    @Bean("rosterSolverConfiguration")
    public SolverConfig rosterSolverConfiguration() {
        return new SolverConfig()
                .withSolutionClass(RosterShiftAssignmentSolution.class)
                .withEntityClassList(List.of(RosterShiftAssignmentPlanning.class))
                .withConstraintProviderClass(RosterConstraintProvider.class)
                .withTerminationConfig(new TerminationConfig()
                        .withSpentLimit(Duration.ofSeconds(5)));
    }

    @Bean("rosterSolver")
    public SolverManager<RosterShiftAssignmentSolution, Integer> rosterSolverManager() {
        SolverFactory<RosterShiftAssignmentSolution> solverFactory =
                SolverFactory.create(rosterSolverConfiguration());
        return SolverManager.create(solverFactory);
    }
}
