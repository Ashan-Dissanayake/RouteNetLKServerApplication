package lk.ashan.routenetlkserverapllication.module.employee.controller;

import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeetypeDto;
import lk.ashan.routenetlkserverapllication.module.employee.service.EmployeetypeService;
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
@RequestMapping(value = "/employeetypes")
@RequiredArgsConstructor
public class EmployeetypeController {

    private final EmployeetypeService employeetypeService;

    @GetMapping(path ="/list", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<EmployeetypeDto>>> get() {
        List<EmployeetypeDto> employeetypes = employeetypeService.getEmployeetypes();
        return APIResponseBuilder.getResponse(employeetypes, employeetypes.size());
    }

}
