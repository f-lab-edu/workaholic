package com.project.workaholic.vcs.api;

import com.project.workaholic.config.interceptor.JsonWebTokenProvider;
import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import com.project.workaholic.vcs.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Tag(name = "VCS API", description = "Workaholic VSC= API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/vcs")
public class VersionControlSystemApi  {
    private final OAuthService oAuthService;
    private final JsonWebTokenProvider jsonWebTokenProvider;

    @Operation(summary = "", description = "")
    @GetMapping("/import")
    public RedirectView importVSCByOAuth(
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            final @RequestParam("vendor")VCSVendor vendor) {
//        String jwt = jsonWebTokenProvider.extractAccessToken(request);
        return oAuthService.importVCS(redirectAttributes, vendor);
    }
}
