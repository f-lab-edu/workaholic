package com.project.workaholic.vcs.api;

import com.project.workaholic.response.model.ApiResponse;
import com.project.workaholic.response.model.enumeration.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VersionControlSystemApi  {

    public ResponseEntity<ApiResponse<StatusCode>> authVersionControlSystem(String repoName, String url) {
        return ApiResponse.success(StatusCode.SUCCESS);
    }

    public ResponseEntity<ApiResponse<StatusCode>> getCommitsFromRepository() {
        return ApiResponse.success(StatusCode.SUCCESS);
    }

    public ResponseEntity<ApiResponse<StatusCode>> getBranchesFromRepository() {
        return ApiResponse.success(StatusCode.SUCCESS);
    }

    public ResponseEntity<ApiResponse<StatusCode>> checkOutBranch(String branchName) {
        return ApiResponse.success(StatusCode.SUCCESS);
    }

    public ResponseEntity<ApiResponse<StatusCode>> getFileContentBy() {
        return ApiResponse.success(StatusCode.SUCCESS);
    }
}
