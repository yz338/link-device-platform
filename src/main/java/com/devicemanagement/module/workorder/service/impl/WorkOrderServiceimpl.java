package com.devicemanagement.module.workorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.devicemanagement.common.exception.BusinessException;
import com.devicemanagement.module.device.entity.Alarm;
import com.devicemanagement.module.device.entity.Device;
import com.devicemanagement.module.device.mapper.AlarmMapper;
import com.devicemanagement.module.device.mapper.DeviceMapper;
import com.devicemanagement.module.system.entity.SysUser;
import com.devicemanagement.module.system.mapper.SysUserMapper;
import com.devicemanagement.module.workorder.bean.ProcessRecord;
import com.devicemanagement.module.workorder.bean.ProcessImage;
import com.devicemanagement.module.workorder.bean.VerificationRecords;
import com.devicemanagement.module.workorder.bean.WorkOrder;
import com.devicemanagement.module.workorder.dto.CreateWorkOrderRequest;
import com.devicemanagement.module.workorder.dto.VerifyWorkOrderRequest;
import com.devicemanagement.module.workorder.dto.WorkOrderQueryDTO;
import com.devicemanagement.module.workorder.enums.WorkOrderStatus;
import com.devicemanagement.module.workorder.mapper.ProcessImageMapper;
import com.devicemanagement.module.workorder.mapper.ProcessRecordMapper;
import com.devicemanagement.module.workorder.mapper.VerificationRecordsMapper;
import com.devicemanagement.module.workorder.mapper.WorkOrderMapper;
import com.devicemanagement.module.workorder.service.WorkOrderService;
import com.devicemanagement.module.workorder.vo.WorkOrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;


@Service
public class WorkOrderServiceimpl extends ServiceImpl<WorkOrderMapper, WorkOrder> implements WorkOrderService {

    @Autowired
    private WorkOrderMapper workOrderMapper;
    @Autowired
    private AlarmMapper alarmMapper;
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private ProcessRecordMapper processRecordMapper;
    @Autowired
    private VerificationRecordsMapper verificationRecordsMapper;
    @Autowired
    private ProcessImageMapper processImageMapper;

    /** 上传文件保存根目录，默认项目运行目录下的 upload 文件夹 */
    @Value("${app.upload-dir:./upload}")
    private String uploadDir;

    /** 允许的图片扩展名 */
    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of("jpg", "jpeg", "png", "gif", "webp", "bmp");

    /** 提交处理时最多上传 9 张图片 */
    private static final int MAX_IMAGE_COUNT = 9;

    @Override
    public void createWorkOrder(CreateWorkOrderRequest request) {
        Alarm alarm = alarmMapper.selectById(request.getAlarmId());
        if (alarm == null) {
            throw new BusinessException("告警不存在");
        }
        if (alarm.getStatus() != null && alarm.getStatus() != 0) {
            throw new BusinessException("该告警已有工单处理中或已处理，不能下发工单");
        }
        WorkOrder workOrder = new WorkOrder();
        workOrder.setAlarmId(request.getAlarmId());
        workOrder.setOrderTitle(request.getOrderTitle());
        workOrder.setOrderDescription(request.getOrderDescription());
        workOrder.setUserId(request.getUserId());
        workOrder.setOrderStatus(WorkOrderStatus.UNPROCESSED.getCode());
        workOrder.setCreateTime(LocalDateTime.now());
        workOrder.setUpdateTime(LocalDateTime.now());
        workOrder.setDeadline(request.getDeadline());
        workOrderMapper.insert(workOrder);
    }

    @Override
    public void updateHandlerUser(Long userId, Long orderId) {
        WorkOrder workOrder = workOrderMapper.selectById(orderId);
        if (workOrder == null) {
            throw new BusinessException("工单不存在");
        }
        if (workOrder.getOrderStatus() == null
                || workOrder.getOrderStatus() != WorkOrderStatus.UNPROCESSED.getCode()) {
            throw new BusinessException("未处理的工单才能修改处理人");
        }
        if (userId != null && userId.equals(workOrder.getUserId())) {
            throw new BusinessException("处理人不能与原处理人相同");
        }
        workOrder.setUserId(userId);
        workOrder.setUpdateTime(LocalDateTime.now());
        workOrderMapper.updateById(workOrder);
    }

    @Override
    @Transactional
    public void startProcess(Long orderId) {
        WorkOrder wo = workOrderMapper.selectById(orderId);
        if (wo == null) {
            throw new BusinessException("工单不存在");
        }
        Integer status = wo.getOrderStatus();
        if (status == null || (status != WorkOrderStatus.UNPROCESSED.getCode()
                && status != WorkOrderStatus.VERIFY_REJECTED.getCode())) {
            throw new BusinessException("只有未处理或核验未通过的工单才能开始处理");
        }
        // 工单状态流转：未处理/核验未通过 -> 处理中
        wo.setOrderStatus(WorkOrderStatus.PROCESSING.getCode());
        wo.setUpdateTime(LocalDateTime.now());
        workOrderMapper.updateById(wo);
        // 联动告警置为处理中
        if (wo.getAlarmId() != null) {
            Alarm alarm = alarmMapper.selectById(wo.getAlarmId());
            if (alarm != null) {
                alarm.setStatus(2);
                alarmMapper.updateById(alarm);
            }
        }
    }

    @Override
    @Transactional
    public void processWorkOrder(Long orderId, String method, String processDescription, MultipartFile[] images) {
        WorkOrder wo = workOrderMapper.selectById(orderId);
        if (wo == null) {
            throw new BusinessException("工单不存在");
        }
        Integer status = wo.getOrderStatus();
        if (status == null || status != WorkOrderStatus.PROCESSING.getCode()) {
            throw new BusinessException("只有处理中的工单才能提交处理结果");
        }
        // 处理图片非必填，但最多 9 张
        if (images != null && images.length > MAX_IMAGE_COUNT) {
            throw new BusinessException("最多上传 " + MAX_IMAGE_COUNT + " 张图片");
        }

        // 写入处理记录
        ProcessRecord pr = new ProcessRecord();
        pr.setOrderId(wo.getOrderId().intValue());
        if (wo.getUserId() != null) {
            pr.setUserId(wo.getUserId().intValue());
        }
        pr.setMethod(method);
        pr.setProcessDescription(processDescription);
        pr.setFinishTime(LocalDateTime.now());
        processRecordMapper.insert(pr);

        // 保存图片到本地 + 写入 process_images，关联到本次处理记录
        if (images != null && images.length > 0) {
            LocalDateTime now = LocalDateTime.now();
            for (MultipartFile image : images) {
                if (image == null || image.isEmpty()) {
                    continue;
                }
                String ext = resolveExtension(image.getOriginalFilename());
                if (!ALLOWED_EXTENSIONS.contains(ext)) {
                    throw new BusinessException("不支持的图片格式：" + image.getOriginalFilename()
                            + "，仅支持 jpg/jpeg/png/gif/webp/bmp");
                }
                String filename = UUID.randomUUID().toString().replace("-", "") + "." + ext;
                String path;
                try {
                    Path dir = Paths.get(uploadDir, "workorder");
                    Files.createDirectories(dir);
                    image.transferTo(dir.resolve(filename).toFile());
                    path = "/upload/workorder/" + filename;
                } catch (IOException e) {
                    throw new BusinessException("图片保存失败：" + image.getOriginalFilename());
                }
                ProcessImage record = new ProcessImage();
                record.setProcessId(pr.getProcessId());
                record.setImageUrl(path);
                record.setCreateTime(now);
                processImageMapper.insert(record);
            }
        }

        // 工单状态流转：处理中 -> 待核验
        wo.setOrderStatus(WorkOrderStatus.PENDING_VERIFY.getCode());
        wo.setUpdateTime(LocalDateTime.now());
        workOrderMapper.updateById(wo);
        // 联动更新告警为已处理，并记录处理人
        if (wo.getAlarmId() != null) {
            Alarm alarm = alarmMapper.selectById(wo.getAlarmId());
            if (alarm != null) {
                alarm.setStatus(1);
                if (wo.getUserId() != null) {
                    SysUser handler = sysUserMapper.selectById(wo.getUserId());
                    if (handler != null) {
                        alarm.setHandler(handler.getNickname());
                    }
                }
                alarmMapper.updateById(alarm);
            }
        }
    }

    @Override
    @Transactional
    public void verifyWorkOrder(VerifyWorkOrderRequest request) {
        WorkOrder wo = workOrderMapper.selectById(request.getOrderId());
        if (wo == null) {
            throw new BusinessException("工单不存在");
        }
        if (wo.getOrderStatus() == null
                || wo.getOrderStatus() != WorkOrderStatus.PENDING_VERIFY.getCode()) {
            throw new BusinessException("只有待核验的工单才能核验");
        }
        // 写入核验记录
        VerificationRecords vr = new VerificationRecords();
        vr.setOrderId(wo.getOrderId().intValue());
        vr.setVerificationResult(request.getVerificationResult());
        vr.setVerificationDescription(request.getVerificationDescription());
        vr.setVerificationTime(LocalDateTime.now());
        wo.setUpdateTime(LocalDateTime.now());
        workOrderMapper.updateById(wo);
    }

    @Override
    public List<String> uploadImages(Long orderId, MultipartFile[] images) {
        // 与提交处理分离的图片上传入口，按 orderId 存入 process_images（processId 留空）
        WorkOrder wo = workOrderMapper.selectById(orderId);
        if (wo == null) {
            throw new BusinessException("工单不存在");
        }
        if (images == null || images.length == 0) {
            throw new BusinessException("请至少上传一张图片");
        }
        if (images.length > MAX_IMAGE_COUNT) {
            throw new BusinessException("最多上传 " + MAX_IMAGE_COUNT + " 张图片");
        }
        List<String> paths = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (MultipartFile image : images) {
            if (image == null || image.isEmpty()) {
                continue;
            }
            String ext = resolveExtension(image.getOriginalFilename());
            if (!ALLOWED_EXTENSIONS.contains(ext)) {
                throw new BusinessException("不支持的图片格式：" + image.getOriginalFilename()
                        + "，仅支持 jpg/jpeg/png/gif/webp/bmp");
            }
            String filename = UUID.randomUUID().toString().replace("-", "") + "." + ext;
            String path;
            try {
                Path dir = Paths.get(uploadDir, "workorder");
                Files.createDirectories(dir);
                image.transferTo(dir.resolve(filename).toFile());
                path = "/upload/workorder/" + filename;
            } catch (IOException e) {
                throw new BusinessException("图片保存失败：" + image.getOriginalFilename());
            }
            ProcessImage record = new ProcessImage();
            record.setImageUrl(path);
            record.setCreateTime(now);
            processImageMapper.insert(record);
            paths.add(path);
        }
        if (paths.isEmpty()) {
            throw new BusinessException("请至少上传一张图片");
        }
        return paths;
    }

    /** 从原始文件名提取小写扩展名，没有扩展名返回空串。 */
    private String resolveExtension(String filename) {
        if (filename == null) {
            return "";
        }
        int dot = filename.lastIndexOf('.');
        if (dot < 0 || dot == filename.length() - 1) {
            return "";
        }
        return filename.substring(dot + 1).toLowerCase(Locale.ROOT);
    }

    @Override
    public Page<WorkOrder> pageList(WorkOrderQueryDTO dto) {
        Page<WorkOrder> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<WorkOrder> wrapper = Wrappers.<WorkOrder>lambdaQuery()
                .eq(dto.getOrderId() != null, WorkOrder::getOrderId, dto.getOrderId())
                .like(dto.getOrderTitle() != null && !dto.getOrderTitle().isEmpty(),
                        WorkOrder::getOrderTitle, dto.getOrderTitle())
                .eq(dto.getOrderStatus() != null, WorkOrder::getOrderStatus, dto.getOrderStatus())
                .eq(dto.getUserId() != null, WorkOrder::getUserId, dto.getUserId())
                .orderByDesc(WorkOrder::getCreateTime);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public WorkOrderVO getDetail(Long orderId) {
        WorkOrder wo = baseMapper.selectById(orderId);
        if (wo == null) {
            throw new BusinessException("工单不存在");
        }

        WorkOrderVO vo = new WorkOrderVO();
        vo.setOrderId(String.valueOf(wo.getOrderId()));
        vo.setOrderTitle(wo.getOrderTitle());
        vo.setOrderDescription(wo.getOrderDescription());
        vo.setOrderStatus(wo.getOrderStatus());
        vo.setDeadline(wo.getDeadline());

        // 告警 + 设备信息
        if (wo.getAlarmId() != null) {
            Alarm alarm = alarmMapper.selectById(wo.getAlarmId());
            if (alarm != null) {
                vo.setAlarmContent(alarm.getAlarmContent());
                if (alarm.getDeviceId() != null) {
                    Device device = deviceMapper.selectById(alarm.getDeviceId());
                    if (device != null) {
                        vo.setDeviceName(device.getDeviceName());
                    }
                }
            }
        }

        // 处理人昵称
        if (wo.getUserId() != null) {
            SysUser user = sysUserMapper.selectById(wo.getUserId());
            if (user != null) {
                vo.setUserName(user.getNickname());
            }
        }

        // 处理记录（按 id 倒序取最新一条，兼容打回后多次处理的场景）
        ProcessRecord pr = processRecordMapper.selectOne(Wrappers.<ProcessRecord>lambdaQuery()
                .eq(ProcessRecord::getOrderId, wo.getOrderId())
                .orderByDesc(ProcessRecord::getProcessId)
                .last("limit 1"));
        if (pr != null) {
            vo.setMethod(pr.getMethod());
            vo.setProcessDescription(pr.getProcessDescription());
        }

        // 核验记录（按 id 倒序取最新一条，兼容多次核验的场景）
        VerificationRecords vr = verificationRecordsMapper.selectOne(
                Wrappers.<VerificationRecords>lambdaQuery()
                        .eq(VerificationRecords::getOrderId, wo.getOrderId())
                        .orderByDesc(VerificationRecords::getVerificationId)
                        .last("limit 1"));
        if (vr != null) {
            vo.setVerificationResult(vr.getVerificationResult());
            vo.setVerificationDescription(vr.getVerificationDescription());
        }

        // 处理图片列表：按最新处理记录 ID 关联的 process_images，按上传顺序返回
        List<String> images = new ArrayList<>();
        if (pr != null) {
            List<ProcessImage> imageList = processImageMapper.selectList(
                    Wrappers.<ProcessImage>lambdaQuery()
                            .eq(ProcessImage::getProcessId, pr.getProcessId())
                            .orderByAsc(ProcessImage::getImageId));
            images = imageList.stream().map(ProcessImage::getImageUrl).toList();
        }
        vo.setImages(images);

        return vo;
    }

}
