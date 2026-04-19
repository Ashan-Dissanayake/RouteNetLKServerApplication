package lk.ashan.routenetlkserverapllication.module.trip.planner;

import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.api.solver.SolverManager;
import ai.timefold.solver.core.config.solver.SolverConfig;
import ai.timefold.solver.core.config.solver.termination.TerminationConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class TripSolverConfig {

    @Bean
    public SolverConfig tripSolverConfiguration() {
        return new SolverConfig()
                .withSolutionClass(TripVehicleOverRideSolution.class)
                .withEntityClasses(TripVehicleOverRidePlanning.class)
                .withConstraintProviderClass(TripConstraintProvider.class)
                .withTerminationConfig(new TerminationConfig()
                        .withSpentLimit(Duration.ofSeconds(120))
                        .withBestScoreLimit("0hard/*soft"));
    }

    @Bean("tripSolver")
    public SolverManager<TripVehicleOverRideSolution, Integer> tripSolverManager(SolverConfig tripSolverConfiguration) {
        SolverFactory<TripVehicleOverRideSolution> solverFactory = SolverFactory.create(tripSolverConfiguration);
        return SolverManager.create(solverFactory);
    }

}
