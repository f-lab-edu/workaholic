package com.project.workaholic.config.exception.type;

import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import lombok.Getter;

@Getter
public class NotFoundOAuthTokenException extends RuntimeException{
    private final VCSVendor vendor;

    public NotFoundOAuthTokenException(VCSVendor vendor) {
        this.vendor = vendor;
    }
}
