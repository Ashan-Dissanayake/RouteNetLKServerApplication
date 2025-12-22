package lk.ashan.routenetlkserverapllication.module.driver.controller;

import lk.ashan.routenetlkserverapllication.module.driver.dto.AllowedBusTypeDto;
import lk.ashan.routenetlkserverapllication.module.driver.dto.LicenseCategoryDto;
import lk.ashan.routenetlkserverapllication.module.driver.model.Licensecategory;
import lk.ashan.routenetlkserverapllication.module.driver.service.AllowedBusTypeService;
import lk.ashan.routenetlkserverapllication.module.driver.service.LicenseCategoryService;
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
@RequestMapping(value = "/licenseCategories")
@RequiredArgsConstructor
public class LicenseCategoryController {

    private final LicenseCategoryService licenseCategoryService;

    @GetMapping(path ="/list", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<LicenseCategoryDto>>> get() {
        List<LicenseCategoryDto> licenseCategories =licenseCategoryService.getLicenseCategories();
        return APIResponseBuilder.getResponse(licenseCategories,licenseCategories.size());
    }

}
