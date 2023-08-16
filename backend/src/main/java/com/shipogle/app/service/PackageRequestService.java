package com.shipogle.app.service;

import com.shipogle.app.model.PackageRequest;

import java.util.List;
import java.util.Map;

public interface PackageRequestService {
    public String sendRequest(Map<String,String> req);

    public String acceptRequest(Integer package_request_id);

    public String rejectRequest(Integer package_request_id);

    public String unsendRequest(Integer package_request_id);

    public List<PackageRequest> getRequest();
}
