package com.example.QuanLyCongCuBE.service;

import java.util.List;

import java.util.Optional;
import org.springframework.stereotype.Service;

import com.example.QuanLyCongCuBE.model.DeviceCategory;
import com.example.QuanLyCongCuBE.reponsitory.DeviceCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceCategoryService {
    @Autowired
    private DeviceCategoryRepository deviceCategoryRepository;

    public List<DeviceCategory> getAllDeviceCategory (){
        List<DeviceCategory> listDeviceCategory = deviceCategoryRepository.findAll();
            return listDeviceCategory;
    }

    public void deleteDeviceCategory (Long id){
        deviceCategoryRepository.deleteBycategoryId(id);
    }

    public DeviceCategory updateCategory (DeviceCategory deviceCategory){
        Optional<DeviceCategory> optionalCategory = deviceCategoryRepository.findById(deviceCategory.getCategoryId());

        if (optionalCategory.isPresent()) {
            DeviceCategory existingCategory = optionalCategory.get();
        
            existingCategory.setCategoryName(deviceCategory.getCategoryName());
            existingCategory.setDescription(deviceCategory.getDescription());

                return deviceCategoryRepository.save(existingCategory);
            } else {
                throw new RuntimeException("Không tìm thấy loại thiết bị có ID: " + deviceCategory.getCategoryId());
            }
        }

        public DeviceCategory addDeviceCategory(DeviceCategory deviceCategory){
            if(deviceCategoryRepository.findByCategoryName(deviceCategory.getCategoryName()).isPresent()){
                throw new RuntimeException("Tên danh mục đã tồn tại");
            }
            DeviceCategory addNewDeviceCategory = deviceCategoryRepository.save(deviceCategory);
                return addNewDeviceCategory;
        }
}
