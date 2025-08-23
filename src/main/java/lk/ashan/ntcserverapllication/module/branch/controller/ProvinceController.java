package lk.ashan.ntcserverapllication.module.branch.controller;

import lk.ashan.ntcserverapllication.module.branch.dto.ProvinceResponse;
import lk.ashan.ntcserverapllication.module.branch.service.ProvinceService;
import lk.ashan.ntcserverapllication.shared.api.dto.APISuccessResponse;
import lk.ashan.ntcserverapllication.shared.api.APIResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/provinces")
@RequiredArgsConstructor
public class ProvinceController {

    private final ProvinceService provinceService;

    @GetMapping(path ="/list", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<ProvinceResponse>>> get() {
        List<ProvinceResponse> provinces = provinceService.getProvinces();
        return APIResponseBuilder.getResponse(provinces, provinces.size());
    }

}
