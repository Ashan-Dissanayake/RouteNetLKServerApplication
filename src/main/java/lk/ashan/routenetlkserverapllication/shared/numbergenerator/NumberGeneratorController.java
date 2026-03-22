package lk.ashan.routenetlkserverapllication.shared.numbergenerator;

import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/number-generator")
@RequiredArgsConstructor
public class NumberGeneratorController {
    private final NumberGeneratorService numberGeneratorService;

    @GetMapping(value = "/branch/{branchName}", produces = "application/json")
    public ResponseEntity<APISuccessResponse<String>> get(
            @PathVariable String branchName
    ) {
        String branchCode = numberGeneratorService.nextBranchNumber(branchName);
        return APIResponseBuilder.ok(
                branchCode,
                Map.of("branchName", branchName)
        );
    }

    @GetMapping(value = "/employee", produces = "application/json")
    public ResponseEntity<APISuccessResponse<String>> get() {
        String employeeNumber = numberGeneratorService.nextEmployeeNumber();
        return APIResponseBuilder.ok(
                employeeNumber,
                Map.of("employeeNumber", employeeNumber)
        );
    }
}
