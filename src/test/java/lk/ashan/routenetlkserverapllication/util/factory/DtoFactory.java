package lk.ashan.routenetlkserverapllication.util.factory;

import lk.ashan.routenetlkserverapllication.module.branch.dto.*;

import java.time.LocalDate;
import java.util.List;

public class DtoFactory {

    public static LocalDate FIXED_DATE = LocalDate.parse("2025-09-18");

    public static BranchtypeDto branchTypeDto(int id, String name) {
        return new BranchtypeDto(id, name);
    }

    public static BranchstatusDto branchStatusDto(int id, String name) {
        return new BranchstatusDto(id, name);
    }

    public static ProvinceDto provinceDto(int id, String name) {
        return new ProvinceDto(id, name);
    }

    public static DistrictDto districtDto(int id, String name) {
        return new DistrictDto(id, name);
    }

    public static BranchDistrictCoverageDto branchCoverageDto(DistrictDto district) {
        return new BranchDistrictCoverageDto(null, district);
    }

    public static BranchCreateRequestDto createBranchRequest(String name, String code,String telephone) {
        return BranchCreateRequestDto.builder()
                .name(name)
                .code(code)
                .address("No.12 Kandy Road")
                .telephone(telephone)
                .email(name.toLowerCase().replace(" ","") + "@ntc.lk")
                .remarks("Test")
                .docreated(FIXED_DATE)
                .branchtype(branchTypeDto(1, "Region"))
                .branchstatus(branchStatusDto(1, "Active"))
                .branchcoverages(List.of(
                        branchCoverageDto(districtDto(4,"Kandy"))
                ))
                .build();
    }

    public static BranchUpdateRequestDto updateBranchRequest(String name, String code,String telephone) {
        return BranchUpdateRequestDto.builder()
                .name(name)
                .code(code)
                .address("No.12 Kandy Road")
                .telephone(telephone)
                .email(name.toLowerCase().replace(" ","") + "@ntc.lk")
                .remarks("Updated test")
                .docreated(FIXED_DATE)
                .branchtype(branchTypeDto(1, "Region"))
                .branchstatus(branchStatusDto(1, "Active"))
                .branchcoverages(List.of(
                        branchCoverageDto(districtDto(4,"Kandy"))
                ))
                .build();
    }
}
