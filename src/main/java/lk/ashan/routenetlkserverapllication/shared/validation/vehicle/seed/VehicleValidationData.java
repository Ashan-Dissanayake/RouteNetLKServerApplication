package lk.ashan.routenetlkserverapllication.shared.validation.vehicle.seed;

import java.util.List;
import java.util.Map;

public class VehicleValidationData {

    public static final Map<String, String> CHASSIS_REGEX = Map.ofEntries(
            Map.entry("Ashok Leyland Viking 193", "^[A-HJ-NPR-Z0-9]{17}$"),
            Map.entry("Ashok Leyland Viking 210 Turbo", "^[A-HJ-NPR-Z0-9]{17}$"),
            Map.entry("Ashok Leyland Viking 222", "^[A-HJ-NPR-Z0-9]{17}$"),
            Map.entry("Ashok Leyland Lynx", "^[A-HJ-NPR-Z0-9]{17}$"),
            Map.entry("Tata LP 12.10/42", "^[A-HJ-NPR-Z0-9]{17}$"),
            Map.entry("Tata LP 15.10/52", "^[A-HJ-NPR-Z0-9]{17}$"),
            Map.entry("Isuzu BF50", "^[A-HJ-NPR-Z0-9]{17}$"),
            Map.entry("Isuzu MT 111L", "^[A-HJ-NPR-Z0-9]{17}$"),
            Map.entry("Isuzu ELR500", "^[A-HJ-NPR-Z0-9]{17}$"),
            Map.entry("Hino", "^[A-HJ-NPR-Z0-9]{17}$"),
            Map.entry("Mitsubishi UMP", "^[A-HJ-NPR-Z0-9]{17}$"),
            Map.entry("Leyland Tiger TL 11", "^[A-HJ-NPR-Z0-9]{17}$"),
            Map.entry("Leyland MCW double decker", "^[A-HJ-NPR-Z0-9]{17}$"),
            Map.entry("Volvo B7RLE", "^[A-HJ-NPR-Z0-9]{17}$"),
            Map.entry("Fiat 642", "^[A-HJ-NPR-Z0-9]{17}$")
    );

    public static final Map<String, String> ENGINE_REGEX = Map.ofEntries(
            Map.entry("Ashok Leyland Viking 193", "^[A-Z0-9]{12}$"),
            Map.entry("Ashok Leyland Viking 210 Turbo", "^[A-Z0-9]{3}[0-9]{3}[A-Z0-9]{5}$"),
            Map.entry("Ashok Leyland Viking 222", "^[A-Z]{2}[0-9]{3}[A-Z]{2}\\.[0-9]{6}$"),
            Map.entry("Ashok Leyland Lynx", "^[A-Z0-9]{12}$"),
            Map.entry("Tata LP 12.10/42", "^[A-Z0-9]{12}$"),
            Map.entry("Tata LP 15.10/52", "^[A-Z0-9]{12}$"),
            Map.entry("Isuzu BF50", "(^[A-Z0-9]{12}$)|(^[A-Z0-9]{3}[0-9]{3}[A-Z0-9]{5}$)"),
            Map.entry("Isuzu MT 111L", "(^[A-Z0-9]{12}$)|(^[A-Z0-9]{3}[0-9]{3}[A-Z0-9]{5}$)"),
            Map.entry("Isuzu ELR500", "^[A-Z0-9]{12}$"),
            Map.entry("Hino", "(^[A-Z0-9]{12}$)|(^[A-Z0-9]{3}[0-9]{3}[A-Z0-9]{5}$)"),
            Map.entry("Mitsubishi UMP", "^[A-Z0-9]{12}$"),
            Map.entry("Leyland Tiger TL 11", "^[A-Z]{2}[0-9]{3}[A-Z]{2}\\.[0-9]{6}$"),
            Map.entry("Leyland MCW double decker", "(^[A-Z]{2}[0-9]{3}[A-Z]{2}\\.[0-9]{6}$)|(^[0-9][A-Z]{2}-[A-Z0-9]{4}-[A-Z]-[0-9]{5}$)"),
            Map.entry("Volvo B7RLE", "^[A-Z0-9]{12}$"),
            Map.entry("Fiat 642", "^[A-Z0-9]{12}$")
    );
}
