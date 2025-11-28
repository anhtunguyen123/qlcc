package com.example.QuanLyCongCuBE.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale.Category;

import org.springframework.stereotype.Service;

import com.example.QuanLyCongCuBE.AOP.LogAction;
import com.example.QuanLyCongCuBE.DTO.AvailabilityRequest;
import com.example.QuanLyCongCuBE.DTO.DeviceAvailabilityResponse;
import com.example.QuanLyCongCuBE.DTO.DeviceRequest;
import com.example.QuanLyCongCuBE.model.Classroom;
import com.example.QuanLyCongCuBE.model.Device;
import com.example.QuanLyCongCuBE.model.DeviceCategory;
import com.example.QuanLyCongCuBE.reponsitory.ClassroomRepository;
import com.example.QuanLyCongCuBE.reponsitory.DeviceCategoryRepository;
import com.example.QuanLyCongCuBE.reponsitory.DeviceReponsitory;
import com.example.QuanLyCongCuBE.reponsitory.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceService {

    @Autowired
    private final DeviceReponsitory deviceReponsitory;
    private final DeviceCategoryRepository deviceCategoryRepository;
    private final ClassroomRepository classroomRepository;
    private final ReservationRepository reservationRepository;

    @LogAction(action = "Thêm thiết bị", actionType = "CREATE")
    public Device createDevice(DeviceRequest deviceRequest) {
        if (deviceReponsitory.existsById(deviceRequest.getDeviceId())) {
            throw new RuntimeException("Mã thiết bị đã tồn tại!");
        }

        Device device = new Device();
        device.setDeviceId(deviceRequest.getDeviceId());
        device.setDeviceName(deviceRequest.getDeviceName());
        device.setDescription(deviceRequest.getDescription());
        device.setSpecifications(deviceRequest.getSpecifications());
        device.setIsPortable(deviceRequest.getIsPortable());
        device.setStatus(deviceRequest.getStatus());

        if (deviceRequest.getCategoryId() != null) {
            DeviceCategory category = deviceCategoryRepository.findById(deviceRequest.getCategoryId())
                    .orElse(null);
            device.setCategory(category);
        }

        if (deviceRequest.getRoomId() != null) {
            Classroom room = classroomRepository.findById(deviceRequest.getRoomId())
                    .orElse(null);
            device.setClassroom(room);
        }

        deviceReponsitory.save(device);

        System.out.println("Thêm thiết bị thành công");
        return device;
    }

    @LogAction(action = "Cập nhập thiết bị", actionType = "UPDATEDEVICE")
    public Device updateDevice(String deviceId, DeviceRequest request) {

        Device device = deviceReponsitory.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found: " + deviceId));

        device.setDeviceName(request.getDeviceName());
        device.setDescription(request.getDescription());
        device.setSpecifications(request.getSpecifications());
        device.setIsPortable(request.getIsPortable());
        device.setStatus(request.getStatus());

        if (request.getCategoryId() != null) {
            DeviceCategory category = deviceCategoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found: " + request.getCategoryId()));
            device.setCategory(category);
        }

        if (request.getRoomId() != null) {
            Classroom room = classroomRepository.findById(request.getRoomId())
                    .orElseThrow(() -> new RuntimeException("Room not found: " + request.getRoomId()));
            device.setClassroom(room);
        }

        return deviceReponsitory.save(device);
    }

    @LogAction(action = "Xóa thiết bị {deviceId}", actionType = "DELETEDEVICE")
    public void DeleteDevice(String deviceId) {

        deviceReponsitory.deleteById(deviceId);
        System.out.println("xoa thiet bi thanh cong");
    }

    public List<Device> getAllDevice() {
        List<Device> listDevice = deviceReponsitory.findAll();
        return listDevice;
    }

    public Long countDevice() {
        return deviceReponsitory.count();
    }

    public List<Device> getDeviceByStatus(String status) {
        List<Device> listDeviceByStatus = deviceReponsitory.findByStatus(status);
        return listDeviceByStatus;
    }

    public List<DeviceAvailabilityResponse> searchAvailable(AvailabilityRequest req) {

        System.out.println(req);
        LocalDateTime endTime = req.getStartTime().plusMinutes(req.getLessonCount() * 45);

        List<Device> devices = deviceReponsitory.searchDevices(req.getRoomId(), req.getCategoryId());
        System.out.println(devices);

        List<DeviceAvailabilityResponse> result = new ArrayList<>();

        for (Device d : devices) {
            boolean overlap = reservationRepository.existsOverlap(
                    d.getDeviceId(), req.getStartTime(), endTime);

            DeviceAvailabilityResponse dto = new DeviceAvailabilityResponse();
            dto.setDeviceId(d.getDeviceId());
            dto.setName(d.getDeviceName());
            dto.setCategory(d.getCategory().getCategoryName());
            dto.setLocation(d.getClassroom().getRoomName());
            if (!overlap) {
                dto.setStatus("AVAILABLE_NOW");
            } else {
                LocalDateTime nextAvailable = reservationRepository.findNextAvailableTime(d.getDeviceId());
                dto.setStatus("AVAILABLE_LATER");
                dto.setAvailableAfter(nextAvailable);
            }
            result.add(dto);
        }
        return result;
    }

    public List<Device> getDeviceAvailable() {
        return deviceReponsitory.findAvailableDevices();
    }

    public List<Device> filterDevice(String status, Integer categoryId, String roomId) {
        return deviceReponsitory.filterDevices(status, categoryId,roomId);
    }
  
}
