package lk.ashan.routenetlkserverapllication.util.factory;


import lk.ashan.routenetlkserverapllication.module.driver.dto.*;

import java.time.LocalDate;

public class DriverDtoFactory {

    public static LicenseCategoryDto licenseCategoryDto(int id, String name) {
        return new LicenseCategoryDto(id, name);
    }

    public static AllowedBusTypeDto allowedBusTypeDto(int id, String name) {
        return new AllowedBusTypeDto(id, name);
    }

    public static CrewStatusDto crewStatusDto(int id, String name) {
        return new CrewStatusDto(id, name);
    }

    public static RouteFamiliarityLevelDto routeFamiliarityLevelDto(int id, String name) {
        return new RouteFamiliarityLevelDto(id, name);
    }

    public static DriverCreateRequestDto createUniqueDriverCreateRequest(){
        return DriverCreateRequestDto.builder()
                .employee(DtoFactory.employeeSummaryyResponseDto(1,"sunil"))
                .number("DRV-2025-006")
                .licensenumber("B39345678905")
                .dolicenseissued(LocalDate.parse("2025-07-12"))
                .dolicenseexpired(LocalDate.parse("2028-07-12"))
                .domedicalissued(LocalDate.parse("2025-06-27"))
                .domedicalexpired(LocalDate.parse("2025-12-27"))
                .licensecategory(licenseCategoryDto(1,"B1"))
                .routefamiliaritylevel(routeFamiliarityLevelDto(1,"Low"))
                .crewstatus(crewStatusDto(1,"Eligible"))
                .build();
    }

}
