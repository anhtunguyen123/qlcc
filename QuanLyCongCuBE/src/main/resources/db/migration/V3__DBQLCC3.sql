ALTER TABLE activity_logs DROP CONSTRAINT fk_log_device;

ALTER TABLE activity_logs 
ADD CONSTRAINT fk_log_device 
FOREIGN KEY (device_id) 
REFERENCES devices(device_id) 
ON DELETE SET NULL;

CREATE INDEX idx_activity_logs_device ON activity_logs(device_id);
CREATE INDEX idx_activity_logs_user_created ON activity_logs(user_id, created_at);