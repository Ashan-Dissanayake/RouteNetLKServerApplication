package lk.ashan.routenetlkserverapllication.config.factory;


import lk.ashan.routenetlkserverapllication.module.permit.dto.*;

import java.time.LocalDate;

public class PermitDtoFactory {

    public static ServiceTypeDto serviceTypeDto(int id, String name) {
        return new ServiceTypeDto(id, name);
    }

    public static PermitStatusDto permitStatusDto(int id, String name) {
        return new PermitStatusDto(id, name);
    }

    public static RouteSummaryRequestDto routeSummaryRequestDto(int id, String number) {
        return new RouteSummaryRequestDto(id, number);
    }

    public static PermitCreateRequestDto createUniquePermitRequestDto(){
        return PermitCreateRequestDto.builder()
                .branch(DtoFactory.branchSummaryResponseDto(2,"Angoda"))
                .route(routeSummaryRequestDto(2,"5"))
                .number("4578")
                .vehicle(VehicleDtoFactory.vehicleSummaryResponseDto(7,"NA-1123"))
                .doissued(LocalDate.of(2002,5,20))
                .doexpired(LocalDate.of(2026,2,15))
                .servicetype(serviceTypeDto(4,"Luxury"))
                .permitestatus(permitStatusDto(1,"Active"))
                .build();
    }



}
