package com.project.workaholic.issue.api;

import com.project.workaholic.issue.model.IssueInfoDto;
import com.project.workaholic.issue.model.IssueConfigDto;
import com.project.workaholic.response.model.ApiResponse;
import com.project.workaholic.response.model.enumeration.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/issue")
public class IssueTrackerApi {
    private long generateRandomIssueId() {
        Random random = new Random();
        long randomId = random.nextLong();
        return randomId == Long.MIN_VALUE ? 0 : Math.abs(randomId);

    }
    @Operation(summary = "이슈 목록 API", description = "생성된 모든 이슈 목록을 조회하는 API")
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<IssueInfoDto>>> getIssueList() {
        return ApiResponse.success(StatusCode.SUCCESS_READ_ALL_ISSUE_LIST,
                List.of());
    }

    @Operation(summary = "이슈 목록 API", description = "지정된 프로젝트에 해당되는 이슈 목록을 조회하는 API")
    @GetMapping("/project/{id}")
    public ResponseEntity<ApiResponse<List<IssueInfoDto>>> getIssueListByProjectId(
            final @Parameter(name = "아이디", description = "프로젝트 아이디")
            @PathVariable("id") Long projectId) {
        return ApiResponse.success(StatusCode.SUCCESS_READ_ISSUE_LIST_BY_PROJECT,
                List.of());
    }

    @Operation(summary = "이슈 조회 API", description = "지정된 ID에 해당되는 이슈를 조회하는 API")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<IssueInfoDto>> getIssueById(
            final @Parameter(name = "아이디", description = "이슈 아이디")
            @PathVariable("id") long issueId) {
        return ApiResponse.success(StatusCode.SUCCESS_READ_ISSUE,
                IssueInfoDto.builder()
                        .build());
    }

    @Operation(summary = "이슈 생성 API", description = "지정된 프로젝트에 해당되는 이슈를 생성 API")
    @PostMapping("")
    public ResponseEntity<ApiResponse<Long>> createIssueByProjectId(
            final @Parameter(name = "생섬 폼", description = "이슈 생성을 위한 폼")
            @Valid @RequestBody IssueConfigDto dto) {
        Random random = new Random();
        long randomId = random.nextLong();
        return ApiResponse.success(StatusCode.SUCCESS_CREATE_ISSUE,
                randomId == Long.MIN_VALUE ? null : Math.abs(randomId));
    }

    @Operation(summary = "이슈 삭제 API", description = "지정된 이슈를 삭제 API")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<StatusCode>> deleteIssueById(
            final @Parameter(name = "아이디", description = "이슈 아이디")
            @PathVariable long id) {

        return ApiResponse.success(StatusCode.SUCCESS_DELETE_ISSUE);
    }

    @Operation(summary = "이슈 수정 API", description = "지정된 이슈를 수정 API")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> updateIssueById(
            final @Parameter(name = "아이디", description = "이슈 아이디")
            @PathVariable long id,
            final @Parameter(name = "수정 폼", description = "이슈 수정을 위한 폼")
            @Valid @RequestBody IssueConfigDto dto) {
        Random random = new Random();
        long randomId = random.nextLong();
        return ApiResponse.success(StatusCode.SUCCESS_UPDATE_ISSUE,
                randomId == Long.MIN_VALUE ? null : Math.abs(randomId));
    }
}
