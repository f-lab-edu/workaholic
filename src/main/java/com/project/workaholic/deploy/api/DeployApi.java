package com.project.workaholic.deploy.api;

import com.project.workaholic.response.model.ApiResponse;
import com.project.workaholic.response.model.enumeration.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeployApi {

    public ResponseEntity<ApiResponse<StatusCode>> getPods() {
        return ApiResponse.success(StatusCode.SUCCESS);
    }

    public ResponseEntity<ApiResponse<StatusCode>> deployment() {
        return ApiResponse.success(StatusCode.SUCCESS);
    }

    public ResponseEntity<ApiResponse<StatusCode>> unDeployment() {
        return ApiResponse.success(StatusCode.SUCCESS);
    }

    public ResponseEntity<ApiResponse<StatusCode>> reDeployment() {
        return ApiResponse.success(StatusCode.SUCCESS);
    }
}
