package com.shipogle.app.service;

import com.shipogle.app.model.Issue;
import com.shipogle.app.model.PackageOrder;
import com.shipogle.app.model.User;
import com.shipogle.app.repository.IssueRepository;
import com.shipogle.app.repository.PackageOrderRepository;
import com.shipogle.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueServiceImpl implements IssueService {
    @Autowired
    IssueRepository issueRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    UserService userService;

    @Autowired
    PackageOrderRepository packageOrderRepo;

    /**
     * Post issue
     *
     * @author Nandkumar Kadivar
     * @param package_order_id package order id
     * @param description description
     * @return String
     */
    @Override
    public String postIssue(Integer package_order_id, String description){
        try {
            Issue issue = new Issue();

            User user = userService.getLoggedInUser();

            Issue currentIssue = issueRepo.getIssueByUser(user);
            boolean isOrderIdMatching = false;
            if(currentIssue!=null)
                isOrderIdMatching = currentIssue.getPackageOrder().getId().equals(package_order_id);
            if(currentIssue!=null && isOrderIdMatching)
                return "Issue Already registered";

            PackageOrder packageOrder = packageOrderRepo.getPackageOrderById(package_order_id);

            issue.setUser(user);
            issue.setPackageOrder(packageOrder);
            issue.setDescription(description);

            issueRepo.save(issue);

        }catch (Exception e){
            return e.getMessage();
        }
        return "Issue registered";
    }

    /**
     * Get all issues
     *
     * @author Nandkumar Kadivar
     * @return List<Issue>
     */
    @Override
    public List<Issue> getAllIssues(){
            return issueRepo.findAll();
    }
}
