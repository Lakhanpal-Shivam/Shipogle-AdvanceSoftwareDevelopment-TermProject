package com.shipogle.app.service;

import com.shipogle.app.model.Issue;

import java.util.List;

public interface IssueService {
    public String postIssue(Integer package_order_id, String description);

    public List<Issue> getAllIssues();
}
