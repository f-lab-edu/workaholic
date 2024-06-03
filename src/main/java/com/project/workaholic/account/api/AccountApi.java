package com.project.workaholic.account.api;

import com.project.workaholic.response.model.ApiResponse;
import com.project.workaholic.response.model.enumeration.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountApi {

    public ResponseEntity<ApiResponse<StatusCode>> login() {
        return ApiResponse.success(StatusCode.LOGIN_SUCCESS);
    }
}
