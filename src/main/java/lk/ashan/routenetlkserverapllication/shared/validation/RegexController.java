package lk.ashan.routenetlkserverapllication.shared.validation;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeRequestDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleRequestDto;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lk.ashan.routenetlkserverapllication.module.crew.validation.annotation.DriverValidationData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping(value = "/regexes")
public class RegexController {

    @GetMapping(path ="/branches", produces = "application/json")
    public ResponseEntity<APISuccessResponse<HashMap<String,HashMap<String,String>>>> branchStatic() {
       HashMap<String,HashMap<String,String>> regexes =  RegexProvider.get(new BranchRequestDto());
        assert regexes != null;
        return APIResponseBuilder.list(regexes, regexes.size());
    }

    @GetMapping(path ="/employees", produces = "application/json")
    public ResponseEntity<APISuccessResponse<HashMap<String,HashMap<String,String>>>> employeeStatic() {
       HashMap<String,HashMap<String,String>> regexes =  RegexProvider.get(new EmployeeRequestDto());
        assert regexes != null;
        return APIResponseBuilder.list(regexes, regexes.size());
    }

    @GetMapping(path ="/vehicles", produces = "application/json")
    public ResponseEntity<APISuccessResponse<HashMap<String,HashMap<String,String>>>> vehicleStatic() {
       HashMap<String,HashMap<String,String>> regexes =  RegexProvider.get(new VehicleRequestDto());
        assert regexes != null;
        return APIResponseBuilder.list(regexes, regexes.size());
    }


    @GetMapping(path ="/driver", produces = "application/json")
    public ResponseEntity<APISuccessResponse<HashMap<String,HashMap<String,String>>>> driverStatic() {
        HashMap<String,HashMap<String,String>> regexes =  RegexProvider.get(new DriverRequestDto());
        assert regexes != null;
        return APIResponseBuilder.list(regexes, regexes.size());
    }

    @GetMapping(path = "/driver/{licensecategory}", produces = "application/json")
    public ResponseEntity<APISuccessResponse<HashMap<String, HashMap<String, String>>>> driverDynamic(
            @PathVariable String licensecategory) {

        HashMap<String, HashMap<String, String>> regexes = new HashMap<>();

        String licenseCategoryPattern = DriverValidationData.LICENSE_CATEGORY_LICENSE_NUMBER_REGEX.get(licensecategory);

        if (licenseCategoryPattern != null) {
            regexes.put("licensenumber", new HashMap<>() {{
                put("regex", licenseCategoryPattern);
                put("message", "Invalid license number");
            }});
        }

        return APIResponseBuilder.list(regexes, regexes.size());
    }

    @GetMapping(path ="/conductor", produces = "application/json")
    public ResponseEntity<APISuccessResponse<HashMap<String,HashMap<String,String>>>> conductorStatic() {
        HashMap<String,HashMap<String,String>> regexes =  RegexProvider.get(new ConductorRequestDto());
        assert regexes != null;
        return APIResponseBuilder.list(regexes, regexes.size());
    }

    @GetMapping(path ="/permits", produces = "application/json")
    public ResponseEntity<APISuccessResponse<HashMap<String,HashMap<String,String>>>> permitStatic() {
        HashMap<String,HashMap<String,String>> regexes =  RegexProvider.get(new PermitRequestDto());
        assert regexes != null;
        return APIResponseBuilder.list(regexes, regexes.size());
    }


}


