package com.project.workaholic.vcs.api;

import com.project.workaholic.response.model.ApiResponse;
import com.project.workaholic.response.model.enumeration.StatusCode;
import com.project.workaholic.vcs.service.OAuthGithubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Tag(name = "VSC API", description = "Workaholic VSC= API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/vsc")
public class VersionControlSystemApi  {

    @Operation(summary = "", description = "")
    @GetMapping("/repo")
    public ResponseEntity<ApiResponse<List<String>>> getRepositoriesFromVersionControlSystem() {
        return ApiResponse.success(StatusCode.SUCCESS_IMPORT_REPO, List.of("feature"));
    }

    @Operation(summary = "", description = "")
    @GetMapping("/commit")
    public ResponseEntity<ApiResponse<StatusCode>> getCommitsFromRepository() {
        return ApiResponse.success(StatusCode.SUCCESS_READ_COMMIT_LIST);
    }

    @Operation(summary = "", description = "")
    @GetMapping("/branch")
    public ResponseEntity<ApiResponse<StatusCode>> getBranchesFromRepository() {
        return ApiResponse.success(StatusCode.SUCCESS_READ_BRANCHES);
    }

}
