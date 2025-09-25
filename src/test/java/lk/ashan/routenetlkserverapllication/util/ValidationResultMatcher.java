package lk.ashan.routenetlkserverapllication.util;

import org.springframework.test.web.servlet.ResultMatcher;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ValidationResultMatcher {

    public static ResultMatcher expectValidationErrors(String... expectedErrors) {
        return jsonPath("$.details", containsInAnyOrder(expectedErrors));
    }

    public static ResultMatcher expectValidationError(String expectedError) {
        return jsonPath("$.details", hasItem(expectedError));
    }

}
