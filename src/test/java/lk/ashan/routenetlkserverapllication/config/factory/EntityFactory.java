package lk.ashan.routenetlkserverapllication.config.factory;

import lk.ashan.routenetlkserverapllication.module.branch.model.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class EntityFactory {

    public static LocalDate FIXED_DATE = LocalDate.parse("2025-07-14");

    public static Branchtype branchType(int id, String name) {
        Branchtype bt = new Branchtype();
        bt.setId(id);
        bt.setName(name);
        return bt;
    }

    public static Branchstatus branchStatus(int id, String name) {
        Branchstatus bs = new Branchstatus();
        bs.setId(id);
        bs.setName(name);
        return bs;
    }


    public static Branch branch(String name, String code, LocalDate createdDate, Branchtype type, Branchstatus status) {
        return Branch.builder()
                .name(name)
                .code(code)
                .address("123 Test Street")
                .telephone("0112345678")
                .email(name.toLowerCase().replace(" ","") + "@ntc.lk")
                .remarks("Test branch")
                .docreated(createdDate)
                .branchtype(type)
                .branchstatus(status)
                .build();
    }


    public static List<Branch> buildMockBranches(LocalDate createdDate) {
        Branch colomboBranch = branch(
                "Colombo Branch",
                "CLB0001-1",
                createdDate,
                branchType(1, "Head"),
                branchStatus(1, "Active")
        );

        Branch ratnapuraBranch = branch(
                "Ratnapura Branch",
                "RAT0007-1",
                createdDate,
                branchType(2, "Region"),
                branchStatus(2, "Inactive")
        );

        return List.of(colomboBranch, ratnapuraBranch);
    }

}
