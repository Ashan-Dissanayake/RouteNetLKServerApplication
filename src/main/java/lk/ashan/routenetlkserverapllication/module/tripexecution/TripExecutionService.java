package lk.ashan.routenetlkserverapllication.module.tripexecution;

import jdk.jfr.TransitionTo;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.repository.TripExecutionRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripExecutionService {

    private final TripExecutionRepository tripExecutionRepository;

    @Transactional(readOnly = true)
    public List<TripExecution> getTripExecutionByTripId(Integer tripId){
        return tripExecutionRepository.findAllByTrip_Id(tripId).orElseThrow(()->new ResourceNotFoundException("TripExecution with id "+tripId+" not found"));
    }
}
