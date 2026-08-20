package com.devicemanagement.module.device.entity.pulldata;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * TVOC对象 device_submission_tvoc
 * 
 * @author chenfan
 * @date 2025-10-25
 */
@Data
@Schema(description = "TVOC对象")
public class DeviceSubmissionTvoc {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 请求编码 QN*/
    @Schema(description = "请求编码 QN")
    private String qn;

    /** 系统编码 ST*/
    @Schema(description = "系统编码 ST")
    private String st;

    /** 命令编码 CN*/
    @Schema(description = "命令编码 CN")
    private String cn;

    private String pw;

    /** 设备唯一标识 MN */
    @Schema(description = "设备唯一标识 MN")
    private String mn;

    /** 拆分包及应答标志Flag */
    @Schema(description = "拆分包及应答标志 Flag")
    private String flag;

    /** 指令参数 CP */
    @Schema(description = "指令参数 CP")
    private String cp;

    /** 时间戳 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "时间戳")
    private Date dataTime;

    private String a99054;

    /** 温度*/
    @Schema(description = "温度")
    private String a01001;

    /** 湿度*/
    @Schema(description = "湿度")
    private String a01002;

    /** 气压*/
    @Schema(description = "气压")
    private String a01006;

    /** 风速*/
    @Schema(description = "风速")
    private String a01007;

    /** 风向 */
    @Schema(description = "风向")
    private String a01008;

}
