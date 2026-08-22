package com.devicemanagement.module.workorder.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/** 工单处理图片：每次提交处理记录时上传的照片，对应 process_images 表。 */
@Data
@TableName("process_images")
@Schema(description = "工单处理图片")
public class ProcessImage {

    @TableId(value = "image_id", type = IdType.AUTO)
    @Schema(description = "图片ID", example = "1")
    private Integer imageId;

    /** 处理记录ID，对应 process_record.process_id */
    @Schema(description = "处理记录ID", example = "1")
    private Integer processId;

    /** 访问路径，如 /upload/workorder/xxx.jpg，前端拼 host 使用 */
    @Schema(description = "图片访问路径", example = "/upload/workorder/abc.jpg")
    private String imageUrl;

    @Schema(description = "上传时间", example = "2026-08-20 10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}