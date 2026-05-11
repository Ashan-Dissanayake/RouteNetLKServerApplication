package lk.ashan.routenetlkserverapllication.module.tripexecution.planner;

import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.api.solver.SolverManager;
import ai.timefold.solver.core.config.solver.SolverConfig;
import ai.timefold.solver.core.config.solver.termination.TerminationConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
public class TripExecutionSolverConfig {

    @Bean("tripExecutionSolverConfiguration")
    public SolverConfig tripExecutionConfiguration() {
        return new SolverConfig()
                .withSolutionClass(TripExecutionSolution.class)
                .withEntityClassList(List.of(TripExecutionPlanning.class))
                .withConstraintProviderClass(TripExecutionConstraintProvider.class)
                .withTerminationConfig(new TerminationConfig()
                        .withSpentLimit(Duration.ofSeconds(5)));
    }

    @Bean("tripExecutionSolver")
    public SolverManager<TripExecutionSolution, Integer> tripExecutionSolverManager() {
        SolverFactory<TripExecutionSolution> solverFactory =
                SolverFactory.create(tripExecutionConfiguration());
        return SolverManager.create(solverFactory);
    }
}
