package lk.ashan.routenetlkserverapllication.shared.validation;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartRequestDto;
import lk.ashan.routenetlkserverapllication.module.user.model.dto.UserCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleCreateRequestDto;
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
       HashMap<String,HashMap<String,String>> regexes =  RegexProvider.get(new BranchCreateRequestDto());
        assert regexes != null;
        return APIResponseBuilder.list(regexes, regexes.size());
    }

    @GetMapping(path ="/employees", produces = "application/json")
    public ResponseEntity<APISuccessResponse<HashMap<String,HashMap<String,String>>>> employeeStatic() {
       HashMap<String,HashMap<String,String>> regexes =  RegexProvider.get(new EmployeeCreateRequestDto());
        assert regexes != null;
        return APIResponseBuilder.list(regexes, regexes.size());
    }

    @GetMapping(path ="/vehicles", produces = "application/json")
    public ResponseEntity<APISuccessResponse<HashMap<String,HashMap<String,String>>>> vehicleStatic() {
       HashMap<String,HashMap<String,String>> regexes =  RegexProvider.get(new VehicleCreateRequestDto());
        assert regexes != null;
        return APIResponseBuilder.list(regexes, regexes.size());
    }


    @GetMapping(path ="/drivers", produces = "application/json")
    public ResponseEntity<APISuccessResponse<HashMap<String,HashMap<String,String>>>> driverStatic() {
        HashMap<String,HashMap<String,String>> regexes =  RegexProvider.get(new DriverCreateRequestDto());
        assert regexes != null;
        return APIResponseBuilder.list(regexes, regexes.size());
    }

    @GetMapping(path = "/drivers/{licenseCategory}", produces = "application/json")
    public ResponseEntity<APISuccessResponse<HashMap<String, HashMap<String, String>>>> driverDynamic(
            @PathVariable String licenseCategory) {

        HashMap<String, HashMap<String, String>> regexes = new HashMap<>();

        String licenseCategoryPattern = DriverValidationData.LICENSE_CATEGORY_LICENSE_NUMBER_REGEX.get(licenseCategory);

        if (licenseCategoryPattern != null) {
            regexes.put("licensenumber", new HashMap<>() {{
                put("regex", licenseCategoryPattern);
                put("message", "Invalid license number");
            }});
        }

        return APIResponseBuilder.list(regexes, regexes.size());
    }

    @GetMapping(path ="/conductors", produces = "application/json")
    public ResponseEntity<APISuccessResponse<HashMap<String,HashMap<String,String>>>> conductorStatic() {
        HashMap<String,HashMap<String,String>> regexes =  RegexProvider.get(new ConductorRequestDto());
        assert regexes != null;
        return APIResponseBuilder.list(regexes, regexes.size());
    }

    @GetMapping(path ="/permits", produces = "application/json")
    public ResponseEntity<APISuccessResponse<HashMap<String,HashMap<String,String>>>> permitStatic() {
        HashMap<String,HashMap<String,String>> regexes =  RegexProvider.get(new PermitCreateRequestDto());
        assert regexes != null;
        return APIResponseBuilder.list(regexes, regexes.size());
    }

    @GetMapping(path ="/parts", produces = "application/json")
    public ResponseEntity<APISuccessResponse<HashMap<String,HashMap<String,String>>>> partStatic() {
        HashMap<String,HashMap<String,String>> regexes =  RegexProvider.get(new PartRequestDto());
        assert regexes != null;
        return APIResponseBuilder.list(regexes, regexes.size());
    }

    @GetMapping(path ="/users", produces = "application/json")
    public ResponseEntity<APISuccessResponse<HashMap<String,HashMap<String,String>>>> userStatic() {
        HashMap<String,HashMap<String,String>> regexes =  RegexProvider.get(new UserCreateRequestDto());
        assert regexes != null;
        return APIResponseBuilder.list(regexes, regexes.size());
    }


}


