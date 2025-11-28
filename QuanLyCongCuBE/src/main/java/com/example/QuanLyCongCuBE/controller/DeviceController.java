package com.example.QuanLyCongCuBE.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.QuanLyCongCuBE.DTO.AvailabilityRequest;
import com.example.QuanLyCongCuBE.DTO.DeviceAvailabilityResponse;
import com.example.QuanLyCongCuBE.DTO.DeviceRequest;
import com.example.QuanLyCongCuBE.model.Device;
import com.example.QuanLyCongCuBE.service.DeviceService;

@RestController
@RequestMapping("/api/device")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DeviceController {
    
    private final DeviceService deviceService;

    @PostMapping("/create")
    public ResponseEntity<Device> createDevice (@RequestBody DeviceRequest device){
        Device newDevice = deviceService.createDevice(device);
        return ResponseEntity.ok(newDevice);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> DeleteDevice (@PathVariable("id") String id){

        deviceService.DeleteDevice(id);
        return ResponseEntity.ok(Map.of(
            "message", "Xóa thành công"
        ));
    }

    @GetMapping("/getdevice")
    public ResponseEntity<List<Device>> getDevice(){

        List<Device> devices = deviceService.getAllDevice();

        return ResponseEntity.ok(devices);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countdevice(){
        
       Long count = deviceService.countDevice();
        return ResponseEntity.ok(count);
    }

    @PostMapping("/search-available")
    public ResponseEntity<List<DeviceAvailabilityResponse>> searchAvailableDevices(@RequestBody AvailabilityRequest request) {
        return ResponseEntity.ok(deviceService.searchAvailable(request));
    }

    @GetMapping("/available-devices")
    public ResponseEntity<List<Device>> getAvailableDevices() {
        return ResponseEntity.ok(deviceService.getDeviceAvailable());
    }

    @PutMapping("/update/{deviceId}")
    public ResponseEntity<?> updateDevice(
            @PathVariable("deviceId") String deviceId,
            @RequestBody DeviceRequest req
    ) {
        return ResponseEntity.ok(deviceService.updateDevice(deviceId, req));
    }

    @GetMapping("/filterdevice")
    public ResponseEntity<List<Device>> filterDevices(
            @RequestParam(name = "status",required = false) String status,
            @RequestParam(name = "categoryId",required = false) Integer categoryId,
            @RequestParam(name = "roomId",required = false) String roomId
    ) {
        return ResponseEntity.ok(deviceService.filterDevice(status, categoryId, roomId));
    }
}

