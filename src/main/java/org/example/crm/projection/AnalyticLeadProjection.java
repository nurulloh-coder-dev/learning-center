package org.example.crm.projection;

public interface AnalyticLeadProjection {
    Long getLeadCount();
    Long getLeadCountInMonth();
    Long getLeadCountInPrevMonth();
}
