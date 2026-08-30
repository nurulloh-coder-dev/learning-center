package org.example.crm.projection;

public interface AnalyticLeadProjection {
    Integer getLeadCount();
    Integer getLeadCountInMonth();
    Integer getLeadCountInPrevMonth();
}
