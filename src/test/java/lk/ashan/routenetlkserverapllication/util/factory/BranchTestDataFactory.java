package lk.ashan.routenetlkserverapllication.util.factory;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchstatusDto;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchtypeDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.*;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

public class BranchTestDataFactory {

    private static final Date FIXED_DATE = Date.valueOf("2025-07-14");

    public static Date getFixedDate() {
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

    public static District buildDistrictWithProvince(int id,String name,Province province){
        District district = new District();
        district.setId(id);
        district.setName(name);
        district.setProvince(province);
        return district;
    }

    public static Province buildProvince(int id, String name) {
        Province province = new Province();
        province.setId(id);
        province.setName(name);
        return  province;
    }


    public static List<Branch> buildMockBranches(Date createdDate) {
        Branch colomboBranch = buildBranch(
                "Colombo Branch", "CLB0001-1", "colombo@ntc.gov.lk", "0112345678",
                createdDate, buildBranchType(1, "Head"), buildBranchStatus(1, "Active")
        );

        Branch ratnapuraBranch = buildBranch(
                "Ratnapura Branch", "RAT0007-1", "ratnapura@ntc.gov.lk", "0452233445",
                createdDate, buildBranchType(2, "Region"), buildBranchStatus(2, "Inactive")
        );

        return List.of(colomboBranch, ratnapuraBranch);
    }

    public static Branch buildBranch(String name, String code, String email, String telephone, Date createdDate, Branchtype type, Branchstatus status) {
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

    public static Branch buildBranchWithBranchcoverages() {
        Branch branch = Branch.builder()
                .name("UNIQUE Branch")
                .code("UNIQUE001-1")
                .email("unique@ntc.gov.lk")
                .telephone("0119998888")
                .address("123 Test Street")
                .remarks("Test branch")
                .docreated(FIXED_DATE)
                .branchtype(buildBranchType(1,"Region"))
                .branchstatus(buildBranchStatus(1,"Active"))
                .build();

        branch.setBranchcoverages(buildBranchCoverages(branch));

        return branch;
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

}
