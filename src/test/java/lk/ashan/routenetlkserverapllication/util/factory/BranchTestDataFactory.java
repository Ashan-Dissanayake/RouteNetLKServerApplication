package lk.ashan.routenetlkserverapllication.util.factory;

import lk.ashan.routenetlkserverapllication.module.branch.dto.*;
import lk.ashan.routenetlkserverapllication.module.branch.model.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class BranchTestDataFactory {

    // ────────────── SERVICE LAYER TESTS ──────────────
    private static final LocalDate FIXED_DATE = LocalDate.parse("2025-07-14");

    public static LocalDate getFixedDate() {
        return FIXED_DATE;
    }

    public static Branchtype buildBranchType(int id, String name) {
        Branchtype branchtype = new Branchtype();
        branchtype.setId(id);
        branchtype.setName(name);
        return branchtype;
    }

    public static BranchtypeDto buildBranchTypeDto(int id, String name) {
        BranchtypeDto branchtypeDto = new BranchtypeDto();
        branchtypeDto.setId(id);
        branchtypeDto.setName(name);
        return branchtypeDto;
    }

    public static Branchstatus buildBranchStatus(int id, String name) {
        Branchstatus branchstatus =new Branchstatus();
        branchstatus.setId(id);
        branchstatus.setName(name);
        return branchstatus;
    }

    public static BranchstatusDto buildBranchStatusDto(int id, String name) {
        BranchstatusDto branchstatusDto =new BranchstatusDto();
        branchstatusDto.setId(id);
        branchstatusDto.setName(name);
        return branchstatusDto;
    }

    public static District buildDistrict(int id, String name) {
        District district = new District();
        district.setId(id);
        district.setName(name);
        return district;
    }


    public static Branch buildBranch(String name, String code, String email, String telephone, LocalDate createdDate, Branchtype type, Branchstatus status) {
        return Branch.builder()
                .name(name)
                .code(code)
                .email(email)
                .telephone(telephone)
                .address("123 Test Street")
                .remarks("Test branch")
                .docreated(createdDate)
                .branchtype(type)
                .branchstatus(status)
                .branchcoverages(Collections.emptyList())
                .build();
    }

    public static BranchCreateRequestDto buildCreateBranchRequest(String name, String code, String email, String telephone) {
        return BranchCreateRequestDto.builder()
                .name(name)
                .code(code)
                .email(email)
                .telephone(telephone)
                .address("123 Test Street")
                .remarks("Test branch")
                .docreated(FIXED_DATE)
                .branchtype(buildBranchTypeDto(1, "Head"))
                .branchstatus(buildBranchStatusDto(1, "Active"))
                .branchcoverages(Collections.emptyList())
                .build();
    }

    public static BranchUpdateRequestDto buildUpdateBranchRequest(String name, String code, String email, String telephone) {
        return BranchUpdateRequestDto.builder()
                .name(name)
                .code(code)
                .email(email)
                .telephone(telephone)
                .address("123 Test Street")
                .remarks("Test branch")
                .docreated(FIXED_DATE)
                .branchtype(buildBranchTypeDto(1, "Head"))
                .branchstatus(buildBranchStatusDto(1, "Active"))
                .branchcoverages(Collections.emptyList())
                .build();
    }

    private static List<Branchcoverage> buildBranchCoverages(Branch branch){
        Branchcoverage bc1 = new Branchcoverage();
        bc1.setDistrict(buildDistrict(1,"Colombo"));
        bc1.setBranch(branch);

        Branchcoverage bc2 = new Branchcoverage();
        bc2.setDistrict(buildDistrict(2,"Gampaha"));
        bc2.setBranch(branch);

        return List.of(bc1,bc2);
    }


    // ────────────── CONTROLLER LAYER TESTS ──────────────

    public static BranchCreateRequestDto validRequest() {
        return BranchCreateRequestDto.builder()
                .name("Dambulla Branch")
                .code("DMB0010")
                .address("No.12 Kandy Road, Dambulla")
                .telephone("0665714120")
                .docreated(LocalDate.parse("2025-09-18"))
                .email("dmb@ntc.lk")
                .remarks("Test")
                .branchtype(new BranchtypeDto(1, "Region"))
                .branchstatus(new BranchstatusDto(1, "Active"))
                .branchcoverages(List.of(
                        new BranchDistrictCoverageDto(null, new DistrictDto(4, "Kandy", new ProvinceDto(1, "Central")))
                ))

                .build();
    }

}
