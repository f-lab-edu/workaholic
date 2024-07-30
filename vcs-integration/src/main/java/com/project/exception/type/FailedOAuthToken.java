package com.project.exception.type;

import com.project.oauth.model.enumeration.VCSVendor;
import lombok.Getter;

@Getter
public class FailedOAuthToken extends RuntimeException{
    private final VCSVendor vendor;

    public FailedOAuthToken(VCSVendor vendor) {
        this.vendor = vendor;
    }
}
