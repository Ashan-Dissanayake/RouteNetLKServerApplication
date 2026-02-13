package lk.ashan.routenetlkserverapllication.module.trip.planner;

import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.constructionheuristic.ConstructionHeuristicPhaseConfig;
import org.optaplanner.core.config.localsearch.LocalSearchPhaseConfig;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TripSolverConfig {

    @Bean
    public SolverFactory<TripSchedule> solverFactory() {
        SolverConfig solverConfig = new SolverConfig()
                .withSolutionClass(TripSchedule.class)
                .withEntityClasses(TripOverrideAssignment.class)
                .withConstraintProviderClass(TripConstraintProvider.class)
                .withTerminationConfig(new TerminationConfig()
                        .withSecondsSpentLimit(120L)
                        .withBestScoreLimit("0hard/*soft")
                )
                .withPhases(
                        new ConstructionHeuristicPhaseConfig(),
                        new LocalSearchPhaseConfig()
                );

        return SolverFactory.create(solverConfig);
    }
}
