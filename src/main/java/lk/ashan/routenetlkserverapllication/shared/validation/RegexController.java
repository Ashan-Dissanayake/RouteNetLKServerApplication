package lk.ashan.routenetlkserverapllication.shared.validation;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeRequestDto;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping(value = "/regexes")
public class RegexController {

    @GetMapping(path ="/branches", produces = "application/json")
    public ResponseEntity<APISuccessResponse<HashMap<String,HashMap<String,String>>>> branch() {
       HashMap<String,HashMap<String,String>> regexes =  RegexProvider.get(new BranchRequestDto());
        assert regexes != null;
        return APIResponseBuilder.getResponse(regexes, regexes.size());
    }

    @GetMapping(path ="/employees", produces = "application/json")
    public ResponseEntity<APISuccessResponse<HashMap<String,HashMap<String,String>>>> employee() {
       HashMap<String,HashMap<String,String>> regexes =  RegexProvider.get(new EmployeeRequestDto());
        assert regexes != null;
        return APIResponseBuilder.getResponse(regexes, regexes.size());
    }
}


