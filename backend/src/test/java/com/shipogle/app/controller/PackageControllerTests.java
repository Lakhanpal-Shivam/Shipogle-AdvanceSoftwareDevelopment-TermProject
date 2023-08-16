package com.shipogle.app.controller;

import com.shipogle.app.model.Package;
import com.shipogle.app.service.PackageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
public class PackageControllerTests {
    @InjectMocks
    PackageController packageController;
    @Mock
    PackageServiceImpl packageService;
    @Mock
    Package _package;

    @Test
    public void createPackageTest() {

        packageController.createPackage(_package);

        verify(packageService,times(1)).storePackage(_package);
    }

    @Test
    public void getAllPackageTest() {

        packageController.getAllPackages();

        verify(packageService,times(1)).getPackages();
    }

    @Test
    public void updatePackageTest() {

        packageController.updatePackage(_package);

        verify(packageService,times(1)).updatePackage(_package);
    }
}
