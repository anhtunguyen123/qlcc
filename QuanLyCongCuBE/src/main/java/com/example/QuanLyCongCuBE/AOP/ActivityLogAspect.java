package com.example.QuanLyCongCuBE.AOP;

import com.example.QuanLyCongCuBE.model.ActivityLog;
import com.example.QuanLyCongCuBE.model.Device;
import com.example.QuanLyCongCuBE.model.User;
import com.example.QuanLyCongCuBE.reponsitory.ActivityLogRepository;
import com.example.QuanLyCongCuBE.reponsitory.UserReponsitory;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class ActivityLogAspect {

    private final ActivityLogRepository activityLogRepository;
    private final UserReponsitory userRepository;
    private final HttpServletRequest request;

    @AfterReturning(value = "@annotation(logAction)", returning = "result")
    public void logAction(JoinPoint joinPoint, LogAction logAction, Object result) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userEntity = null;

        if (principal instanceof User) {
            userEntity = (User) principal;
        } else if (principal instanceof org.springframework.security.core.userdetails.User) {
            String username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
            userEntity = userRepository.findByEmail(username).orElse(null);
        }

        if (userEntity == null) return;

        Object[] args = joinPoint.getArgs();
        String deviceId = args.length > 0 ? String.valueOf(args[0]) : "unknown";
        
        ActivityLog log = new ActivityLog();
        log.setUser(userEntity);
        log.setAction(logAction.action().replace("{deviceId}", deviceId));
        log.setActionType(logAction.actionType());
        log.setIpAddress(request.getRemoteAddr());
        log.setUserAgent(request.getHeader("User-Agent"));
        log.setCreatedAt(LocalDateTime.now());

        if (result instanceof Device) {
            log.setDevice((Device) result);
        }

        activityLogRepository.save(log);
    }
}
