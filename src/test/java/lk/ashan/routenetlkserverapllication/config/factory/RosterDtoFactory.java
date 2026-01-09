package lk.ashan.routenetlkserverapllication.config.factory;


import lk.ashan.routenetlkserverapllication.module.roster.dto.*;

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

    public static RosterCreateRequestDto createUniqueRosterCreateRequest(){
        return RosterCreateRequestDto.builder()
                .doroster(LocalDate.now().plusDays(10))
                .shift(createShiftDto(1))
                .rosterstatus(createRosterStatus(1,"Draft"))
                .branch(DtoFactory.branchSummaryResponseDto(1,"Colombo head office"))
                .build();
    }

}
