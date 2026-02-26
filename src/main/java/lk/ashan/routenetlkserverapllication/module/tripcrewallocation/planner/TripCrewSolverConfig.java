package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.planner;

import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.constructionheuristic.ConstructionHeuristicPhaseConfig;
import org.optaplanner.core.config.localsearch.LocalSearchPhaseConfig;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TripCrewSolverConfig {

    @Bean
    public SolverFactory<TripCrewScheduleSolution> tripCrewSolverFactory() {
        SolverConfig solverConfig = new SolverConfig()
                .withSolutionClass(TripCrewScheduleSolution.class)
                .withEntityClasses(TripCrewAssignmentPlanning.class)
                .withConstraintProviderClass(TripCrewConstraintProvider.class)
                .withTerminationConfig(new TerminationConfig()
                        .withSecondsSpentLimit(120L)
                        .withBestScoreLimit("0hard/*soft"))
                .withPhases(
                        new ConstructionHeuristicPhaseConfig(),
                        new LocalSearchPhaseConfig()
                );

        return SolverFactory.create(solverConfig);
    }
}
