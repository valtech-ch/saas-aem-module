package com.valtech.aem.saas.api.query;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test to verify that Java 17 compilation issues have been resolved.
 * This test ensures that Lombok annotations work correctly and 
 * enum/switch statements compile without issues.
 */
public class CompilationTest {

    @Test
    public void testFilterJoinOperatorEnum() {
        // Test that enum compilation works
        FilterJoinOperator andOperator = FilterJoinOperator.AND;
        FilterJoinOperator orOperator = FilterJoinOperator.OR;
        
        assertEquals(" AND ", andOperator.getText());
        assertEquals(" OR ", orOperator.getText());
    }

    @Test
    public void testCompositeFilterBuilder() {
        // Test that Lombok @Builder works correctly
        CompositeFilter filter = CompositeFilter.builder()
            .joinOperator(FilterJoinOperator.AND)
            .build();
        
        assertNotNull(filter);
        assertEquals("", filter.getQueryString()); // Should be empty for no filters
    }

    @Test
    public void testFilterFactory() {
        // Test that the factory methods work correctly
        Filter simpleFilter = FilterFactory.createSimpleFilter("key", "value");
        assertNotNull(simpleFilter);
        
        Filter notFilter = FilterFactory.createNotFilter("key", "value");
        assertNotNull(notFilter);
    }
}