package lk.ashan.routenetlkserverapllication.module.crew.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.crew.dto.DriverDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.crew.mapper.DriverMapper;
import lk.ashan.routenetlkserverapllication.module.crew.model.Driver;
import lk.ashan.routenetlkserverapllication.module.crew.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    public List<DriverDetailResponseDto> getDrivers(){
       return driverMapper.toDtoList(driverRepository.findAll());
    }

    public List<DriverDetailResponseDto> searchDriver(@NotNull HashMap<String, String> params) {

        String allowedbustypeid = params.get("ssallowedbustype");
        String crewstatusid = params.get("sscrewstatus");
        String routefamilitylevelid = params.get("ssroutefamilitylevel");

        Stream<Driver> driverStream = driverRepository.findAll().stream();

        if (allowedbustypeid != null)
            driverStream = driverStream.filter(d -> d.getAllowedbustype().getId() == Integer.parseInt(allowedbustypeid));
        if (crewstatusid != null) driverStream = driverStream.filter(d->d.getCrewstatus().getId()==Integer.parseInt(crewstatusid));
        if (routefamilitylevelid != null)
            driverStream = driverStream.filter(d -> d.getRoutefamiliaritylevel().getId()== Integer.parseInt(routefamilitylevelid));

        return driverMapper.toDtoList(driverStream.collect(Collectors.toList()));

    }


}
