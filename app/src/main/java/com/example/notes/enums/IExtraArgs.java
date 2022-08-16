package com.example.notes.enums;

/**
 * @author yogitad
 * @since 16-08-2022
 * This enum will be used for bundle extra constants in the overall application.
 **/

public enum IExtraArgs {
    /**
     * Specify navigate to which fragment
     */
    ARG_NAVIGATE_TO_DASHBOARD_FRAGMENT("DashboardFragment");

    String value;

    IExtraArgs(String value) {
        this.value = value;
    }

    public String value() {
        return this.name();
    }

    public String getShortValue() {
        return value;
    }
}
