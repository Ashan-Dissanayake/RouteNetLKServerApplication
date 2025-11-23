package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.DocumenttypeDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.DocumenttypeMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.DocumenttypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumenttypeService {

    private final DocumenttypeRepository documenttypeRepository;
    private final DocumenttypeMapper documenttypeMapper;

    public List<DocumenttypeDto> getDocumenttypes(){
       return documenttypeMapper.toDtoList(documenttypeRepository.findAll());
    }

}
