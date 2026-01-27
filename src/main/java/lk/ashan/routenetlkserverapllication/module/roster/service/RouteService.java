package lk.ashan.routenetlkserverapllication.module.roster.service;

import lk.ashan.routenetlkserverapllication.module.roster.dto.RouteDto;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.RouteMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.Route;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;

    public List<RouteDto> getRoutes() {
        List<Route> shifts = routeRepository.findAll();
        return routeMapper.toDtoList(shifts);
    }

}

