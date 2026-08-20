package com.devicemanagement.module.device.entity.pulldata;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 空气微站对象 device_submission_air_station
 * 
 * @author chenfan
 * @date 2025-10-25
 */
@Data
@Schema(description = "空气微站对象")
public class DeviceSubmissionAirStation {
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "时间戳")
    private Date dataTime;

    /** 二氧化硫累计值 */
    @Schema(description = "二氧化硫累计值")
    private String a21026Cou;

    /** 二氧化硫最小值 */
    @Schema(description = "二氧化硫最小值")
    private String a21026Min;

    /** 二氧化硫平均值 */
    @Schema(description = "二氧化硫平均值")
    private String a21026Avg;

    /** 二氧化硫最大值 */
    @Schema(description = "二氧化硫最大值")
    private String a21026Max;

    /** 一氧化氮累计值 */
    @Schema(description = "一氧化氮累计值")
    private String a21003Cou;

    /** 一氧化氮最小值 */
    @Schema(description = "一氧化氮最小值")
    private String a21003Min;

    /** 一氧化氮平均值 */
    @Schema(description = "一氧化氮平均值")
    private String a21003Avg;

    /** 一氧化氮最大值 */
    @Schema(description = "一氧化氮最大值")
    private String a21003Max;

    /** 二氧化氮累计值 */
    @Schema(description = "二氧化氮累计值")
    private String a21004Cou;

    /** 二氧化氮最小值 */
    @Schema(description = "二氧化氮最小值")
    private String a21004Min;

    /** 二氧化氮平均值 */
    @Schema(description = "二氧化氮平均值")
    private String a21004Avg;

    /** 二氧化氮最大值 */
    @Schema(description = "二氧化氮最大值")
    private String a21004Max;

    /** 氮氧化物累计值 */
    @Schema(description = "氮氧化物累计值")
    private String a21002Cou;

    /** 氮氧化物最小值 */
    @Schema(description = "氮氧化物最小值")
    private String a21002Min;

    /** 氮氧化物平均值 */
    @Schema(description = "氮氧化物平均值")
    private String a21002Avg;

    /** 氮氧化物最大值 */
    @Schema(description = "氮氧化物最大值")
    private String a21002Max;

    /** 一氧化碳累计值 */
    @Schema(description = "一氧化碳累计值")
    private String a21005Cou;

    /** 一氧化碳最小值 */
    @Schema(description = "一氧化碳最小值")
    private String a21005Min;

    /** 一氧化碳平均值 */
    @Schema(description = "一氧化碳平均值")
    private String a21005Avg;

    /** 一氧化碳最大值 */
    @Schema(description = "一氧化碳最大值")
    private String a21005Max;


    private String a05024Cou;


    private String a05024Min;


    private String a05024Avg;


    private String a05024Max;

    /** 可吸入颗粒物PM10累计值 */
    @Schema(description = "可吸入颗粒物PM10累计值")
    private String a34002Cou;

    /** 可吸入颗粒物PM10最小值 */
    @Schema(description = "可吸入颗粒物PM10最小值")
    private String a34002Min;

    /** 可吸入颗粒物PM10平均值 */
    @Schema(description = "可吸入颗粒物PM10平均值")
    private String a34002Avg;

    /** 可吸入颗粒物PM10最大值 */
    @Schema(description = "可吸入颗粒物PM10最大值")
    private String a34002Max;

    /** 细微颗粒物 PM2.5累计值*/
    @Schema(description = "细微颗粒物 PM2.5累计值")
    private String a34004Cou;

    /** 细微颗粒物 PM2.5最小值 */
    @Schema(description = "细微颗粒物 PM2.5最小值")
    private String a34004Min;

    /** 细微颗粒物 PM2.5平均值 */
    @Schema(description = "细微颗粒物 PM2.5平均值")
    private String a34004Avg;

    /** 细微颗粒物 PM2.5最大值 */
    @Schema(description = "细微颗粒物 PM2.5最大值")
    private String a34004Max;
}
