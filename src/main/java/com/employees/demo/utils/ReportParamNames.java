package com.employees.demo.utils;

public final class ReportParamNames {


    private ReportParamNames(){
        throw new IllegalArgumentException("Error, object instancing is forbidden");
    };


    public static final String REPORT_NAME_KEY ="reportName";

    public static final String REPORT_PARAMS_KEY ="params";

    public static final String DEPT_NUM_KEY ="deptNum";
}
