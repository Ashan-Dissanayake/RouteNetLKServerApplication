package lk.ashan.routenetlkserverapllication.util.factory;


import lk.ashan.routenetlkserverapllication.module.vehicle.dto.*;

import java.time.LocalDate;
import java.time.Year;

public class VehicleDtoFactory {

    public static MakeRequestDto makeDto(int id, String name) {
        return new MakeRequestDto(id, name);
    }

    public static SeatingcapacityRequestDto seatingcapacityRequestDto(int id, Integer amount) {
        return new SeatingcapacityRequestDto(id, amount);
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

    public static ServicetypeDto servicetypeDto(int id, String name) {
        return new ServicetypeDto(id, name);
    }

    public static VehicleCreateRequestDto createUniqueVehicleRequest(){
        return VehicleCreateRequestDto.builder()
                .make(makeDto(1,"Ashok Leyland Viking 193"))
                .code("BS-ALV00013")
                .number("NB-7701")
                .yom(Year.of(2016))
                .dob(LocalDate.parse("2016-03-11"))
                .mileage(120210)
                .chasisnumber("KLXP712345ABE6781")
                .enginenumber("VMK193C1A2D3")
                .fueltype(fueltypeDto(2,"Disel"))
                .vehiclestatus(vehiclestatusDto(1,"Available"))
                .conditionrate(conditionrateDto(2,"Good"))
                .servicetype(servicetypeDto(1,"Passenger"))
                .seatingcapacity(seatingcapacityRequestDto(1,42))
                .employee(DtoFactory.employeeSummaryyResponseDto(1,"sunil"))
                .branch(DtoFactory.branchSummaryResponseDto(2,"Angoda"))
                .build();
    }


}
