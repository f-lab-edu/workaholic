package com.project.workaholic.issue.api;

import com.project.workaholic.response.model.ApiResponse;
import com.project.workaholic.response.model.enumeration.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IssueTrackerApi {

    public ResponseEntity<ApiResponse<StatusCode>> setTrackerByIssueNumber(String issueNumber) {

        return ApiResponse.success(StatusCode.SUCCESS);
    }

    public ResponseEntity<ApiResponse<StatusCode>> closedTrackerByIssueNumber(String issueNumber) {

        return ApiResponse.success(StatusCode.SUCCESS);
    }
}
