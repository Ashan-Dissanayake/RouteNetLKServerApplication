package lk.ashan.routenetlkserverapllication.module.roster.planner;

import ai.timefold.solver.core.config.solver.SolverConfig;
import ai.timefold.solver.core.config.solver.termination.TerminationConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
public class TimeFoldConfiguration {

    @Bean
    public SolverConfig solverConfig() {
        return new SolverConfig()
                .withSolutionClass(RosterShiftAssignmentSolution.class)
                .withEntityClassList(List.of(RosterShiftAssignmentPlanning.class))
                .withConstraintProviderClass(RosterConstraintProvider.class)
                .withTerminationConfig(new TerminationConfig()
                        .withSpentLimit(Duration.ofSeconds(5)));
    }
}
