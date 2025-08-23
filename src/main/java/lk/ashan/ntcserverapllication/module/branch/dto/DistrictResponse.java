package lk.ashan.ntcserverapllication.module.branch.dto;

import lombok.Value;

@Value
public class DistrictResponse {
    Integer id;
    String name;
    ProvinceResponse province;
}
