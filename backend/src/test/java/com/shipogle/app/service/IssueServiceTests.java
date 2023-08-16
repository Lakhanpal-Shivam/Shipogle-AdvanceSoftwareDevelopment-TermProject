package com.shipogle.app.service;

import com.shipogle.app.model.Issue;
import com.shipogle.app.model.PackageOrder;
import com.shipogle.app.model.User;
import com.shipogle.app.repository.IssueRepository;
import com.shipogle.app.repository.PackageOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.management.InvalidAttributeValueException;
import java.util.InvalidPropertiesFormatException;
import java.util.concurrent.ExecutionException;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IssueServiceTests {
    @InjectMocks
    IssueServiceImpl issueService;
    @Mock
    Issue issue;
    @Mock
    IssueRepository issueRepo;
    @Mock
    User user;
    @Mock
    UserServiceImpl userService;
    @Mock
    PackageOrder packageOrder;
    private final int TEST_ORDER_ID = 2;

    @Test
    public void postIssueTestAlreadyRaisedIssue(){
        when(userService.getLoggedInUser()).thenReturn(user);
        when(issueRepo.getIssueByUser(user)).thenReturn(issue);
        when(issue.getPackageOrder()).thenReturn(packageOrder);
        when(packageOrder.getId()).thenReturn(1);
        assertEquals("Issue Already registered",issueService.postIssue(1,"issue description"));
    }

    @Test
    public void postIssueTestException(){
        when(userService.getLoggedInUser()).thenThrow(UsernameNotFoundException.class);
        assertNull(issueService.postIssue(1, "issue description"));
    }

    @Test
    public void getAllIssuesTestSuccess(){
        issueService.getAllIssues();
        Mockito.verify(issueRepo,Mockito.times(1)).findAll();
    }
}
