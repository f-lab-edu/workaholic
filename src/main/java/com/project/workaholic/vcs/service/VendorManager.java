package com.project.workaholic.vcs.service;

import com.project.workaholic.vcs.model.enumeration.VCSVendor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VendorManager {
    private final Map<VCSVendor, VendorApiService> serviceMap = new HashMap<>();

    public void registerService(VCSVendor vendor, VendorApiService service) {
        serviceMap.put(vendor, service);
    }

    public VendorApiService getService(VCSVendor vendor) {
        return serviceMap.get(vendor);
    }
}
