package lk.ashan.routenetlkserverapllication.config.factory;


import lk.ashan.routenetlkserverapllication.module.permit.dto.*;
import lk.ashan.routenetlkserverapllication.module.trip.dto.*;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleSummaryResponseDto;

import java.time.LocalDate;
import java.time.LocalTime;

public class TripDtoFactory {

    public static TripTypeDto tripTypeDto(int id, String name) {
        return new TripTypeDto(id, name);
    }

    public static OriginTerminalDto originTerminalDto(int id, String name) {
        return new OriginTerminalDto(id, name);
    }

    public static OverrideStatusDto overrideStatusDto(int id, String number) {
        return new OverrideStatusDto(id, number);
    }

    public static TripStatusDto tripStatusDto(int id, String number) {
        return new TripStatusDto(id, number);
    }

    public static TripVehicleOverrideDto tripVehicleOverrideDto(
            int id,
            String reason,
            LocalDate doOverride,
            OverrideStatusDto overrideStatusDto,
            VehicleSummaryResponseDto vehicle) {
        return new TripVehicleOverrideDto(id, reason,doOverride,vehicle,overrideStatusDto);
    }


    public static TripCreateRequestDto createUniqueTripRequestDto(){
        return TripCreateRequestDto.builder()
                .branch(DtoFactory.branchSummaryResponseDto(2,"Angoda"))
                .triptype(tripTypeDto(1,"Daily"))
                .permite(PermitSummaryRequestDto.builder().id(1).number("ANG-NA7845-103-3").build())
                .doservice(LocalDate.parse("2026-02-15"))
                .todepature(LocalTime.parse("08:50:00"))
                .toarrival(LocalTime.parse("09:30:00"))
                .notrip(2)
                .tripstatus(tripStatusDto(1,"Planned"))
                .originterminal(originTerminalDto(1,"Pettah"))
                .build();
    }



}
