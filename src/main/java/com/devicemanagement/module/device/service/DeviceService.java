package com.devicemanagement.module.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.devicemanagement.module.device.dto.DeviceAttrUpdateDTO;
import com.devicemanagement.module.device.dto.DeviceCategoryDTO;
import com.devicemanagement.module.device.dto.DeviceDTO;
import com.devicemanagement.module.device.entity.Device;
import com.devicemanagement.module.device.vo.DeviceDetailVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface DeviceService extends IService<Device> {
    List<Device> deviceList(DeviceDTO deviceDTO);
    void save(DeviceDTO deviceDTO);
    Device updateDevice(DeviceDTO deviceDTO);
    String deleteDeviceById(Long id);
    List<String> queryCategoryName(DeviceCategoryDTO deviceCategoryDTO);
    List<Device> queryByCategory(String categoryName);
    String importExcel(MultipartFile file) throws Exception;
    DeviceDetailVO getDeviceDetail(Long id);
    void updateDeviceAttr(DeviceAttrUpdateDTO dto);
    String exportExcel(List<Device> devices, HttpServletResponse response) throws IOException;
    String exportTemplate(HttpServletResponse response) throws IOException;
}
