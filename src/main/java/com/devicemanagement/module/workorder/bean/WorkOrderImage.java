package com.devicemanagement.module.workorder.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/** 工单处理图片：处理人上传的处理完成照片，一个工单可有多张。 */
@Data
@TableName("work_order_image")
@Schema(description = "工单处理图片")
public class WorkOrderImage {

    @TableId(value = "image_id", type = IdType.AUTO)
    @Schema(description = "图片ID", example = "1")
    private Integer imageId;

    @Schema(description = "工单ID", example = "1")
    private Integer orderId;

    /** 访问路径，如 /upload/workorder/xxx.jpg，前端拼 host 使用 */
    @Schema(description = "图片访问路径", example = "/upload/workorder/abc.jpg")
    private String imagePath;

    @Schema(description = "上传时间", example = "2026-08-20 10:00:00")
    private LocalDateTime createTime;
}
