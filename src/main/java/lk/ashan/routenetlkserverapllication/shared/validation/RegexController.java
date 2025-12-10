package lk.ashan.routenetlkserverapllication.shared.validation;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleRequestDto;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lk.ashan.routenetlkserverapllication.shared.validation.vehicle.seed.VehicleValidationData;
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
        return APIResponseBuilder.getResponse(regexes, regexes.size());
    }

    @GetMapping(path ="/employees", produces = "application/json")
    public ResponseEntity<APISuccessResponse<HashMap<String,HashMap<String,String>>>> employeeStatic() {
       HashMap<String,HashMap<String,String>> regexes =  RegexProvider.get(new EmployeeRequestDto());
        assert regexes != null;
        return APIResponseBuilder.getResponse(regexes, regexes.size());
    }

    @GetMapping(path ="/vehicles", produces = "application/json")
    public ResponseEntity<APISuccessResponse<HashMap<String,HashMap<String,String>>>> vehicleStatic() {
       HashMap<String,HashMap<String,String>> regexes =  RegexProvider.get(new VehicleRequestDto());
        assert regexes != null;
        return APIResponseBuilder.getResponse(regexes, regexes.size());
    }

    @GetMapping(path = "/vehicles/{model}", produces = "application/json")
    public ResponseEntity<APISuccessResponse<HashMap<String, HashMap<String, String>>>> vehicleDynamic(
            @PathVariable String model) {

        HashMap<String, HashMap<String, String>> regexes = new HashMap<>();

        String chassisPattern = VehicleValidationData.CHASSIS_REGEX.get(model);
        String enginePattern = VehicleValidationData.ENGINE_REGEX.get(model);

        if (chassisPattern != null) {
            regexes.put("chasisnumber", new HashMap<>() {{
                put("regex", chassisPattern);
                put("message", "Invalid Chassis Number");
            }});
        }

        if (enginePattern != null) {
            regexes.put("enginenumber", new HashMap<>() {{
                put("regex", enginePattern);
                put("message", "Invalid Engine Number");
            }});
        }

        return APIResponseBuilder.getResponse(regexes, regexes.size());
    }


}


