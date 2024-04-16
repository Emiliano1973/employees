package com.employees.demo.controllers;

import com.employees.demo.dtos.DownloadResponse;
import com.employees.demo.services.DepartmentService;
import com.employees.demo.services.DownloadManagerService;
import com.employees.demo.utils.ContentType;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.employees.demo.utils.ReportParamNames.*;

@RestController
@RequestMapping("/api/services/departments")
@CacheConfig(cacheNames = "departments")
public class DepartmentController {

    private static final String REPORT_DEP_WITH_PARAM_NAME="departmentsWithParams";

    private final DepartmentService departmentService;

    private final DownloadManagerService jasperDownloadManagerService;


    public DepartmentController(final DepartmentService departmentService,
                               final DownloadManagerService jasperDownloadManagerService) {
        this.departmentService = departmentService;
        this.jasperDownloadManagerService = jasperDownloadManagerService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Cacheable(value = "departments", keyGenerator = "customKeyGenerator")
    public ResponseEntity<?> getAllDepartments() {
        return ResponseEntity.ok(this.departmentService.getAllDepartments());
    }

    @GetMapping(value = "/{deptNo}/report/{type}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getDepartmentReport(
            @PathVariable("deptNo") final String departmentNumber
            ,@PathVariable("type") final ContentType contentType) {
        Map<String, Object> params=new HashMap<>();
        params.put(REPORT_NAME_KEY, REPORT_DEP_WITH_PARAM_NAME);
        Map<String, Object> internalParams=new HashMap<>();
        internalParams.put(DEPT_NUM_KEY,departmentNumber);
        params.put(REPORT_PARAMS_KEY,internalParams);
        DownloadResponse downloadResponse=this.jasperDownloadManagerService.getDownloadResponse(params, contentType);
        return ResponseEntity.ok()
                .contentLength(downloadResponse.getResponseLength())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + downloadResponse.getFileName()+"." +contentType.getExtension()+ "")
                .contentType(downloadResponse.getContentType())
                .body(new InputStreamResource(downloadResponse.getResponse()));
    }

    @GetMapping(value = "/pie", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllDepartmentsPie() {
        return ResponseEntity.ok(this.departmentService.getEmployeesDeptGroups());
    }


}
