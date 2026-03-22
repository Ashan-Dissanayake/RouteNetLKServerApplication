package lk.ashan.routenetlkserverapllication.shared.numbergenerator;

import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Component
public class DocumentNumberFormatter {


    public String branch(long seq,String branchName) {
        String prefix = branchName.replaceAll("[^a-zA-Z]", "").toUpperCase();
        prefix = prefix.substring(0, 3);
        return prefix + String.format("%04d", seq);
    }

    public String employee(long seq) {
        return "EMP-" + String.format("%04d", seq);
    }

    public String driver(long seq) {
        return "DRV-" + String.format("%06d", seq);
    }

    public String conductor(long seq) {
        return "CON-" + String.format("%06d", seq);
    }

    public String partRequest(String branchCode, YearMonth ym, long seq) {
        return "PR-" + branchCode + "-" + ym.format(DateTimeFormatter.ofPattern("yyyyMM"))
                + "-" + String.format("%04d", seq);
    }

    public String grn(String branchCode, YearMonth ym, long seq) {
        return "GRN-" + branchCode + "-" + ym.format(DateTimeFormatter.ofPattern("yyyyMM"))
                + "-" + String.format("%04d", seq);
    }

    public String vehicleService(String branchCode, YearMonth ym, long seq) {
        return "VS-" + branchCode + "-" + ym.format(DateTimeFormatter.ofPattern("yyyyMM"))
                + "-" + String.format("%04d", seq);
    }
}
