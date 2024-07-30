package com.project.exception.type;

import com.project.oauth.model.enumeration.VCSVendor;
import lombok.Getter;

@Getter
public class NotFoundOAuthTokenException extends RuntimeException{
    private final VCSVendor vendor;

    public NotFoundOAuthTokenException(VCSVendor vendor) {
        this.vendor = vendor;
    }
}
