package lk.ashan.routenetlkserverapllication.config.factory;


import lk.ashan.routenetlkserverapllication.module.crew.dto.*;

import java.time.LocalDate;

public class ConductorDtoFactory {
    
    public static CrewStatusDto crewStatusDto(int id, String name) {
        return new CrewStatusDto(id, name);
    }

    public static RouteFamiliarityLevelDto routeFamiliarityLevelDto(int id, String name) {
        return new RouteFamiliarityLevelDto(id, name);
    }

    public static ConductorCreateRequestDto createUniqueConductorCreateRequest(){
        return ConductorCreateRequestDto.builder()
                .employee(DtoFactory.employeeSummaryyResponseDto(13,"Sameera"))
                .number("CON-2025-005")
                .domedicalissued(LocalDate.parse("2025-08-01"))
                .domedicalexpired(LocalDate.parse("2026-02-01"))
                .routefamiliaritylevel(routeFamiliarityLevelDto(1,"Low"))
                .crewstatus(crewStatusDto(1,"Eligible"))
                .build();
    }
}
