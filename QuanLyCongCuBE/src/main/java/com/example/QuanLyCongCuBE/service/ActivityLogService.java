package com.example.QuanLyCongCuBE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.QuanLyCongCuBE.model.ActivityLog;
import com.example.QuanLyCongCuBE.model.Device;
import com.example.QuanLyCongCuBE.model.User;
import com.example.QuanLyCongCuBE.reponsitory.ActivityLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityLogService {
    @Autowired
    private final ActivityLogRepository activityLogRepository;
    private final HttpServletRequest request;

    public void log(User user, Device device, String action, String actionType) {
        ActivityLog log = new ActivityLog();
        log.setUser(user);
        log.setDevice(device);
        log.setAction(action);
        log.setActionType(actionType);
        log.setIpAddress(request.getRemoteAddr());
        log.setUserAgent(request.getHeader("User-Agent"));

        activityLogRepository.save(log);
    }

}
