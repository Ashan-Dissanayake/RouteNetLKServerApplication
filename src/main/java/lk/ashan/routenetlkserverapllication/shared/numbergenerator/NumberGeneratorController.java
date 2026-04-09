package lk.ashan.routenetlkserverapllication.shared.numbergenerator;

import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/number-generator")
@RequiredArgsConstructor
public class NumberGeneratorController {
    private final NumberGeneratorService numberGeneratorService;

    @GetMapping(value = "/branch/{branchName}", produces = "application/json")
    public ResponseEntity<APISuccessResponse<String>> getBranchCode(
            @PathVariable String branchName
    ) {
        String branchCode = numberGeneratorService.nextBranchNumber(branchName);
        return APIResponseBuilder.ok(
                branchCode,
                Map.of("branchName", branchName)
        );
    }

    @GetMapping(value = "/employee", produces = "application/json")
    public ResponseEntity<APISuccessResponse<String>> getEmployeeNumber() {
        String employeeNumber = numberGeneratorService.nextEmployeeNumber();
        return APIResponseBuilder.ok(
                employeeNumber,
                Map.of("employeeNumber", employeeNumber)
        );
    }

    @GetMapping(value = "/driver", produces = "application/json")
    public ResponseEntity<APISuccessResponse<String>> getDriverNumber() {
        String driverNumber = numberGeneratorService.nextDriverNumber();
        return APIResponseBuilder.ok(
                driverNumber,
                Map.of("driverNumber", driverNumber)
        );
    }

    @GetMapping(value = "/part-request", produces = "application/json")
    public ResponseEntity<APISuccessResponse<String>> getParRequestNumber() {
        String partRequestNumber = numberGeneratorService.nextPartRequestNumber("CLM", YearMonth.now());
        return APIResponseBuilder.ok(
                partRequestNumber,
                Map.of("partRequestNumber", partRequestNumber)
        );
    }

    @GetMapping(value = "/grn", produces = "application/json")
    public ResponseEntity<APISuccessResponse<String>> getGrnNumber() {
        String grnNumber = numberGeneratorService.nextGrnNumber("CLM0001", YearMonth.now());
        return APIResponseBuilder.ok(
                grnNumber,
                Map.of("grnNumber", grnNumber)
        );
    }
}
