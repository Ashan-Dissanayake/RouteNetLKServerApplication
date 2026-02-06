package lk.ashan.routenetlkserverapllication.config.factory;


import lk.ashan.routenetlkserverapllication.module.vehicle.dto.*;

import java.time.LocalDate;
import java.time.Year;

public class VehicleDtoFactory {

    public static MakeRequestDto makeDto(int id, String name) {
        return new MakeRequestDto(id, name);
    }

    public static FueltypeDto fueltypeDto(int id, String name) {
        return new FueltypeDto(id, name);
    }

    public static VehiclestatusDto vehiclestatusDto(int id, String name) {
        return new VehiclestatusDto(id, name);
    }

    public static ConditionrateDto conditionrateDto(int id, String name) {
        return new ConditionrateDto(id, name);
    }

    public static ModelDto modelDto(int id, String name) {
        return new ModelDto(id, name);
    }

    public static VehicleCreateRequestDto createUniqueVehicleRequest(){
        return VehicleCreateRequestDto.builder()
                .make(makeDto(1,"Ashok Leyland"))
                .number("NB-7701")
                .mileage(120210)
                .fueltype(fueltypeDto(2,"Disel"))
                .vehiclestatus(vehiclestatusDto(1,"Available"))
                .conditionrate(conditionrateDto(2,"Good"))
                .model(modelDto(3,"Ashok Leyland Viking 210 Turbo"))
                .branch(DtoFactory.branchSummaryResponseDto(2,"Angoda"))
                .build();
    }

    public static VehicleUpdateRequestDto createUniqueVehicleUpdateRequest(){
        return VehicleUpdateRequestDto.builder()
                .id(181)
                .make(makeDto(9,"Volvo B7RLE"))
                .number("ND-1299")
                .mileage(7654)
                .model(modelDto(3,"Ashok Leyland Viking 210 Turbo"))
                .fueltype(fueltypeDto(2,"Disel"))
                .vehiclestatus(vehiclestatusDto(1,"Available"))
                .conditionrate(conditionrateDto(2,"Good"))
                .branch(DtoFactory.branchSummaryResponseDto(2,"Angoda"))
                .build();
    }


}
