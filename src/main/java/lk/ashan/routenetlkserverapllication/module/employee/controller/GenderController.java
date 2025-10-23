package lk.ashan.routenetlkserverapllication.module.employee.controller;

import lk.ashan.routenetlkserverapllication.module.employee.dto.GenderDto;
import lk.ashan.routenetlkserverapllication.module.employee.service.GenderService;
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
@RequestMapping(value = "/genders")
@RequiredArgsConstructor
public class GenderController {

    private final GenderService genderService;

    @GetMapping(path ="/list", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<GenderDto>>> get() {
        List<GenderDto> genders = genderService.getGenders();
        return APIResponseBuilder.getResponse(genders, genders.size());
    }

}
