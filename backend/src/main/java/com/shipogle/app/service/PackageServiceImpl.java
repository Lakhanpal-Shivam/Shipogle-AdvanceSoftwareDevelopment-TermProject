package com.shipogle.app.service;

import com.shipogle.app.model.Package;
import com.shipogle.app.model.User;
import com.shipogle.app.repository.PackageRepository;
import com.shipogle.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PackageServiceImpl implements PackageService{

    @Autowired
    PackageRepository packageRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    UserService userService;

    /**
     * Store package details
     *
     * @author Nandkumar Kadivar
     * @param courier package
     * @return integer stored package id
     */
    public Integer storePackage(Package courier){
        try {
            User user = userService.getLoggedInUser();

            courier.setSender(user);
            Integer package_id = packageRepo.save(courier).getId();

            return package_id;
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to save package");
        }
    }

    /**
     * Fetch all the packages of user
     *
     * @author Nandkumar Kadivar
     * @return list of packages
     */
    public List<Package> getPackages(){
        try {
            User user = userService.getLoggedInUser();

            List<Package> packages = packageRepo.getAllBySender(user);

            return packages;
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Fetch all the packages of user
     *
     * @author Nandkumar Kadivar
     * @param courier package
     * @return list of packages
     */
    public String updatePackage(Package courier){
        try{
            Package p = packageRepo.getPackageById(courier.getId());
            float length = courier.getLength();
            float width = courier.getWidth();
            float height = courier.getHeigth();

            p.setTitle(courier.getTitle());
            p.setDescription(courier.getDescription());
            p.setPackageDimension(length,width,height);
            p.setPickup_address(courier.getPickup_address());
            p.setDrop_address(courier.getDrop_address());
            packageRepo.save(p);

            return "Package updated";
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
