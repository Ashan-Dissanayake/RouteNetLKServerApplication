package lk.ashan.routenetlkserverapllication.module.permit.service;

import lk.ashan.routenetlkserverapllication.module.permit.dto.RouteDto;
import lk.ashan.routenetlkserverapllication.module.permit.mapper.RouteMapper;
import lk.ashan.routenetlkserverapllication.module.permit.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;

    public List<RouteDto> getRoutes(){
       return routeMapper.toDtoList(routeRepository.findAll());
    }

}
