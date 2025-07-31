package lk.ashan.ntcserverapllication.module.branch.controller;

import lk.ashan.ntcserverapllication.module.branch.dto.DistrictResponse;
import lk.ashan.ntcserverapllication.module.branch.service.DistrictService;
import lk.ashan.ntcserverapllication.paylaod.response.APISuccessResponse;
import lk.ashan.ntcserverapllication.shared.APIResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/districts")
@RequiredArgsConstructor
public class DistrictController {

    private final DistrictService districtService;

    @GetMapping(path ="/list", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<DistrictResponse>>> get() {
        List<DistrictResponse> districts = districtService.getDistricts();
        return APIResponseBuilder.getResponse(districts, districts.size());
    }

}
