package lk.ashan.routenetlkserverapllication.config.factory;

import lk.ashan.routenetlkserverapllication.module.branch.dto.*;
import lk.ashan.routenetlkserverapllication.module.employee.dto.*;

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
                .build();
    }

    public static BranchUpdateRequestDto updateBranchRequest(String name, String code,String telephone) {
        return BranchUpdateRequestDto.builder()
                .name(name)
                .code(code)
                .address("Kandy Road, Dambulla")
                .telephone(telephone)
                .email("dmb@sltbl.lk")
                .remarks("Updated test")
                .docreated(FIXED_DATE)
                .branchtype(branchTypeDto(1, "Region"))
                .branchstatus(branchStatusDto(1, "Active"))
                .build();
    }

    public static BranchCreateRequestDto branchSoftDeletedCreateRequest(String name,String code,String address,String telephone,String email){

        return BranchCreateRequestDto.builder()
                .name(name)
                .code(code)
                .address(address)
                .telephone(telephone)
                .email(email)
                .docreated(FIXED_DATE)
                .branchtype(branchTypeDto(1, "Region"))
                .branchstatus(branchStatusDto(1, "Active"))
                .build();

    }

    public static BranchUpdateRequestDto branchSoftDeletedUpdateRequest(Integer id,String name,String code,String address,String telephone,String email){

        return BranchUpdateRequestDto.builder()
                .id(id)
                .name(name)
                .code(code)
                .address(address)
                .telephone(telephone)
                .email(email)
                .docreated(FIXED_DATE)
                .branchtype(branchTypeDto(3, " Local/Sub Depot"))
                .branchstatus(branchStatusDto(1, "Active"))
                .build();

    }

    //Employee
    public static GenderDto genderDto(int id, String name) {
        return new GenderDto(id, name);
    }

    public static BranchSummaryResponseDto branchSummaryResponseDto(int id, String name) {
        return new BranchSummaryResponseDto(id, name);
    }

    public static DepartmentDto departmentDto(int id, String name) {
        return new DepartmentDto(id, name);
    }

    public static DesignationDto designationDto(int id, String name) {
        return new DesignationDto(id, name);
    }

    public static EmployeetypeDto employeetypeDto(int id, String name) {
        return new EmployeetypeDto(id, name);
    }

    public static EmployeestatusDto employeestatusDto(int id, String name) {
        return new EmployeestatusDto(id, name);
    }

    public static EmployeeCreateRequestDto createUniqueEmployeeRequestNoImage(){
        return EmployeeCreateRequestDto.builder()
                .number("EMPCLM0011")
                .fullname("Minuri Navoddika")
                .callingname("Minuri")
                .nic("200253171988")
                .gender(genderDto(2,"Female"))
                .mobile("0716042647")
                .email("minuri.EMPCLM0007@sltb.lk")
                .address("No 12, Pepiliyana, Kirindiwela")
                .emergencycontact("0331547842")
                .branch(branchSummaryResponseDto(4,"Avissawella"))
                .department(departmentDto(3,"Administrative "))
                .designation(designationDto(5,"Assistant Manager"))
                .employeetype(employeetypeDto(1,"Permanent"))
                .employeestatus(employeestatusDto(1,"Active"))
                .doj(LocalDate.parse("2025-10-28"))
                .build();
    }

    public static EmployeeCreateRequestDto createExistEmployeeRequestNoImage(){
        return EmployeeCreateRequestDto.builder()
                .number("EMPCLM0001")
                .fullname("Sunil Perera")
                .callingname("Sunil")
                .nic("200045602345")
                .gender(genderDto(1,"Male"))
                .mobile("0771234567")
                .email("sunil.EMPCLM0001@sltb.lk")
                .address("No 12, Maradana, Colombo 10")
                .emergencycontact("0712345678")
                .branch(branchSummaryResponseDto(1,"Colombo head office"))
                .department(departmentDto(1,"Operations (Traffic)"))
                .designation(designationDto(4,"Depot Manager"))
                .employeetype(employeetypeDto(1,"Permanent"))
                .employeestatus(employeestatusDto(2,"Suspend"))
                .doj(LocalDate.parse("2015-03-12"))
                .build();

    }

    public static EmployeeUpdateRequestDto createEmployeeUpateRequestNoImage(){
        return EmployeeUpdateRequestDto.builder()
                .id(1)
                .number("EMPCLM0001")
                .fullname("Sunil Perera")
                .callingname("Sunil")
                .nic("200045602345")
                .gender(genderDto(1,"Male"))
                .mobile("0771234567")
                .email("sunil.EMPCLM0001@sltb.lk")
                .address("No 12, Maradana, Colombo 10")
                .emergencycontact("0712345678")
                .branch(branchSummaryResponseDto(1,"Colombo head office"))
                .department(departmentDto(1,"Operations (Traffic)"))
                .designation(designationDto(4,"Depot Manager"))
                .employeetype(employeetypeDto(1,"Permanent"))
                .employeestatus(employeestatusDto(2,"Suspend"))
                .doj(LocalDate.parse("2015-03-12"))
                .build();
    }

    public static EmployeeSummaryResponseDto employeeSummaryyResponseDto(int id, String name) {
        return new EmployeeSummaryResponseDto(id, name);
    }

}
