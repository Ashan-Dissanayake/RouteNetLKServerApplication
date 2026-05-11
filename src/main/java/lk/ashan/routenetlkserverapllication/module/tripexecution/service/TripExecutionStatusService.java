package lk.ashan.routenetlkserverapllication.module.tripexecution.service;

import lk.ashan.routenetlkserverapllication.module.tripexecution.mapper.TripExecutionStatusMapper;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto.TripExecutionStatusDto;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecutionStatus;
import lk.ashan.routenetlkserverapllication.module.tripexecution.repository.TripExecutionStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TripExecutionStatusService {

    private final TripExecutionStatusRepository tripExecutionStatusRepository;
    private final TripExecutionStatusMapper tripExecutionStatusMapper;

    @Transactional(readOnly = true)
    public List<TripExecutionStatusDto> getTripExecutionStatuses(){
        return tripExecutionStatusMapper.toDtoList(tripExecutionStatusRepository.findAll());
    }


    @Transactional(readOnly = true)
    public TripExecutionStatus getById(Integer id) {
        return tripExecutionStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status not found"
                ));
    }

    @Transactional(readOnly = true)
    public TripExecutionStatus getByName(String name) {
        return tripExecutionStatusRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status not found"
                ));
    }

}
