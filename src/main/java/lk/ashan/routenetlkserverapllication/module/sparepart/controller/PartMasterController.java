package lk.ashan.routenetlkserverapllication.module.sparepart.controller;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartMasterDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.service.PartMasterService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/part-masters")
@RequiredArgsConstructor
public class PartMasterController {

    private final PartMasterService partMasterService;

    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<PartMasterDto>>> get() {
        List<PartMasterDto> partMasters = partMasterService.getPartMasters();
        return APIResponseBuilder.list(partMasters, partMasters.size());
    }

}
