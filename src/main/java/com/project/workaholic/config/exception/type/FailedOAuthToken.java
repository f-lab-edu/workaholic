package com.project.workaholic.config.exception.type;

import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import lombok.Getter;

@Getter
public class FailedOAuthToken extends RuntimeException{
    private final VCSVendor vendor;

    public FailedOAuthToken(VCSVendor vendor) {
        this.vendor = vendor;
    }
}
