package com.devicemanagement.module.device.entity.pulldata;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 地水设备对象 device_submission_surface_water
 * 
 * @author chenfan
 * @date 2025-10-25
 */
@Data
@Schema(description = "地水设备对象")
public class DeviceSubmissionSurfaceWater {
    private static final long serialVersionUID = 1L;

    /** 主键 */
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
    @Schema(description = "拆分包及应答标志Flag")
    private String flag;

    /** 指令参数 CP */
    @Schema(description = "指令参数 CP")
    private String cp;

    /** 时间戳 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "时间戳")
    private Date dataTime;

    /** pH 值*/
    @Schema(description = "pH 值")
    private String w01001;

    private String w01003;

    /** 溶解氧*/
    @Schema(description = "溶解氧")
    private String w01009;

    /** 水温*/
    @Schema(description = "水温")
    private String w01010;

    /** 电导率*/
    @Schema(description = "电导率")
    private String w01014;

    /** 高锰酸盐指数*/
    @Schema(description = "高锰酸盐指数")
    private String w01019;

    /** 总氮*/
    @Schema(description = "总氮")
    private String w21001;

    /** 氨氮 */
    @Schema(description = "氨氮")
    private String w21003;

    /** 总磷*/
    @Schema(description = "总磷")
    private String w21011;

}
