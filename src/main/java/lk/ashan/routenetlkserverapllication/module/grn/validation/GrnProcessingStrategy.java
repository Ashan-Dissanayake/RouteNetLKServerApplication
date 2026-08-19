package lk.ashan.routenetlkserverapllication.module.grn.validation;

import java.math.BigDecimal;

/**
 * Strategy interface for processing GRN (Goods Received Note) operations.
 * Implementations define specific processing logic based on the received and expected quantities.
 */
public interface GrnProcessingStrategy {

    /**
     * Determines if the strategy is applicable based on the received and expected quantities.
     *
     * @param receivedQty the quantity of goods received
     * @param expectedQty the quantity of goods expected
     * @return true if the strategy is applicable, false otherwise
     */
    boolean isApplicable(BigDecimal receivedQty, BigDecimal expectedQty);

    /**
     * Processes the GRN context using the specific strategy implementation.
     *
     * @param context the GRN context containing relevant data for processing
     * @throws IllegalArgumentException if the context is invalid or processing fails
     */
    void process(GrnContext context);
}
