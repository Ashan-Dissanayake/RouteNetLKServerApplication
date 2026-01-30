package lk.ashan.routenetlkserverapllication.module.roster.planner;

import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.constructionheuristic.ConstructionHeuristicPhaseConfig;
import org.optaplanner.core.config.constructionheuristic.ConstructionHeuristicType;
import org.optaplanner.core.config.localsearch.LocalSearchPhaseConfig;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RosterSolverConfig {

    @Bean
    public SolverFactory<RosterAssignmentSolution> solverFactory() {
        SolverConfig solverConfig = new SolverConfig()
                .withSolutionClass(RosterAssignmentSolution.class)
                .withEntityClasses(RosterAssignmentPlanning.class)
                .withConstraintProviderClass(RosterConstraintProvider.class)
                .withTerminationConfig(new TerminationConfig()
                        .withSecondsSpentLimit(120L) // Maximum 2 minutes
                        .withBestScoreLimit("0hard/*soft") // Stop if perfect hard score achieved
                )
                .withPhases(
                new ConstructionHeuristicPhaseConfig(),
                new LocalSearchPhaseConfig()
        );

        return SolverFactory.create(solverConfig);
    }

}
