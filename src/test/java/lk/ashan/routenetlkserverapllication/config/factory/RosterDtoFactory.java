package lk.ashan.routenetlkserverapllication.config.factory;


import lk.ashan.routenetlkserverapllication.module.roster.dto.*;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterassignementstatus;

import java.time.LocalDate;

public class RosterDtoFactory {

    public static RosterStatusDto createRosterStatus(int id, String name) {
        return new RosterStatusDto(id, name);
    }

    public static ShiftStatusDto createShiftStatusDto(int id,String name){
        return new ShiftStatusDto(id,name);
    }

    public static ShiftTypeDto createShiftTypeDto(int id, String name) {
        return new ShiftTypeDto(id, name);
    }

    public static ShiftDto createShiftDto(int id) {
        return ShiftDto.builder().id(id).build();
    }

    public static RouteDto createRouteDto(int id,String number,String name){
        return RouteDto.builder().id(id).number(number).name(name).build();
    }

    public static RosterAssignmentStatusDto createRosterAssigmentStatusDto(int id, String name){
        return RosterAssignmentStatusDto.builder().id(id).name(name).build();
    }

    public static RosterCreateRequestDto createUniqueRosterCreateRequest(){
        return RosterCreateRequestDto.builder()
                .doroster(LocalDate.of(2026, 8,12))
                .shift(createShiftDto(1))
                .rosterstatus(createRosterStatus(1,"Draft"))
                .branch(DtoFactory.branchSummaryResponseDto(1,"Colombo head office"))
                .build();
    }

    public static RosterConfirmationRequestDto CreateConfirmationRequestDto(Integer branchId, LocalDate date,Boolean isConfirm){
       return RosterConfirmationRequestDto.builder()
                        .branchId(branchId)
                        .date(date)
                        .confirm(isConfirm)
                        .build();
    }


}
