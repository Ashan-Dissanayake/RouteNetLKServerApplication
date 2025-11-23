package lk.ashan.routenetlkserverapllication.module.vehicle.controller;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.DocumenttypeDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.service.DocumenttypeService;
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
@RequestMapping(value = "/documenttypes")
@RequiredArgsConstructor
public class DocumenttypeController {

    private final DocumenttypeService documenttypeService;

    @GetMapping(path ="/list", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<DocumenttypeDto>>> get() {
        List<DocumenttypeDto> documenttypes = documenttypeService.getDocumenttypes();
        return APIResponseBuilder.getResponse(documenttypes, documenttypes.size());
    }

}
