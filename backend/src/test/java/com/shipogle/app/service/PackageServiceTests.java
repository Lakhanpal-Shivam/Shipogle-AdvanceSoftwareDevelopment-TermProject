package com.shipogle.app.service;

import com.shipogle.app.repository.PackageRepository;
import com.shipogle.app.model.User;
import com.shipogle.app.model.Package;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PackageServiceTests {
    @InjectMocks
    PackageServiceImpl packageService;
    @Mock
    Package courier;
    @Mock
    PackageRepository packageRepo;
    @Mock
    User user;

    @Mock
    UserService userService;
    private final int TEST_COURIER_ID = 40;

    @Test
    public void storePackageTestSuccess(){
        Mockito.when(userService.getLoggedInUser()).thenReturn(user);
        Mockito.doNothing().when(courier).setSender(user);

        Mockito.when(packageRepo.save(courier)).thenReturn(courier);
        Mockito.when(courier.getId()).thenReturn(TEST_COURIER_ID);

        assertEquals(TEST_COURIER_ID,packageService.storePackage(courier));
    }

    @Test
    public void storePackageTestException(){
        Mockito.when(userService.getLoggedInUser()).thenReturn(user);

        Mockito.doNothing().when(courier).setSender(user);
        Mockito.when(packageRepo.save(courier)).thenThrow(IllegalArgumentException.class);

        assertThrows(ResponseStatusException.class,()->packageService.storePackage(courier));
    }

    @Test
    public void getPackagesTestSuccess(){
        List<Package> packages = new ArrayList<>();
        packages.add(courier);

        Mockito.when(userService.getLoggedInUser()).thenReturn(user);
        Mockito.when(packageRepo.getAllBySender(user)).thenReturn(packages);

        assertEquals(packages,packageService.getPackages());
    }

    @Test
    public void getPackagesTestException(){
        List<Package> packages = new ArrayList<>();
        packages.add(courier);

        Mockito.when(userService.getLoggedInUser()).thenThrow(UsernameNotFoundException.class);
        assertThrows(ResponseStatusException.class,()->packageService.getPackages());
    }

    @Test
    public void updatePackageTestSuccess(){
        Mockito.when(courier.getId()).thenReturn(TEST_COURIER_ID);
        Mockito.when(packageRepo.getPackageById(courier.getId())).thenReturn(courier);
        Mockito.when(packageRepo.save(courier)).thenReturn(courier);

        assertEquals("Package updated",packageService.updatePackage(courier));
    }

    @Test
    public void updatePackageTestException(){
        Mockito.when(courier.getId()).thenReturn(TEST_COURIER_ID);
        Mockito.when(packageRepo.getPackageById(courier.getId())).thenThrow(IllegalArgumentException.class);

        assertThrows(ResponseStatusException.class,()->packageService.updatePackage(courier));
    }
}
