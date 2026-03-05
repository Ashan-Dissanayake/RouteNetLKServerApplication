package lk.ashan.routenetlkserverapllication.module.sparepart.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.sparepart.dto.PartDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.mapper.PartMapper;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.Part;
import lk.ashan.routenetlkserverapllication.module.sparepart.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PartService {
    
    private final PartRepository partRepository;
    private final PartMapper partMapper;

    @Transactional(readOnly = true)
    public List<PartDetailResponseDto> getParts(){
        return partMapper.toDtoList(partRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<PartDetailResponseDto> searchParts(@NotNull HashMap<String, String> params) {

        List<Part> parts = partRepository.findAll();

        String partCategoryId = params.get("sscategory");
        String partStatusId= params.get("sspartstatus");

        Stream<Part> partStream = parts.stream();

        if(partCategoryId!=null)partStream = partStream.filter(r->r.getPartcategory().getId() == Integer.parseInt(partCategoryId));
        if(partStatusId!=null)partStream = partStream.filter(r->r.getPartstatus().getId()==Integer.parseInt(partStatusId));

        return partMapper.toDtoList( partStream.collect(Collectors.toList()));
    }
    
}
