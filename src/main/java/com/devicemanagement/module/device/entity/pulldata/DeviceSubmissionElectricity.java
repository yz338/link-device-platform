package com.devicemanagement.module.device.entity.pulldata;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 用电量监控报送信息对象 device_submission_electricity
 * 
 * @author chenfan
 * @date 2025-10-11
 */
@Data
@Schema(description = "用电量监控报送信息对象")
public class DeviceSubmissionElectricity {
    private static final long serialVersionUId = 1L;
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "请求编码 QN")
    @Excel(name = "请求编码 QN")
    private String qn;

    @Schema(description = "系统编码 ST")
    @Excel(name = "系统编码 ST")
    private String st;

    @Schema(description = "命令编码 CN")
    @Excel(name = "命令编码 CN")
    private String cn;

    @Schema(description = "设备唯一标识 MN")
    @Excel(name = "设备唯一标识 MN")
    private String mn;

    @Schema(description = "拆分包及应答标志Flag")
    @Excel(name = "拆分包及应答标志Flag")
    private String flag;

    @Schema(description = "指令参数 CP")
    @Excel(name = "指令参数 CP")
    private String cp;

    @Schema(description = "时间戳")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dataTime;

    @Schema(description = "电流互感器倍率 IrAt")
    @Excel(name = "电流互感器倍率 IrAt")
    private String d7;

    @Schema(description = "电压互感器倍率 UrAt")
    @Excel(name = "电压互感器倍率 UrAt")
    private String d8;

    @Schema(description = "正向有功电能 ImpEp")
    @Excel(name = "正向有功电能 ImpEp")
    private String d4127;

    @Schema(description = "反向有功电能 ExpEp")
    @Excel(name = "反向有功电能 ExpEp")
    private String d4137;

    @Schema(description = "第一象限无功电能 Q1Eq")
    @Excel(name = "第一象限无功电能 Q1Eq")
    private String d4147;

    @Schema(description = "第二象限无功电能 Q2Eq")
    @Excel(name = "第二象限无功电能 Q2Eq")
    private String d4157;

    @Schema(description = "第三象限无功电能 Q3Eq")
    @Excel(name = "第三象限无功电能 Q3Eq")
    private String d4167;

    @Schema(description = "第四象限无功电能 Q4Eq")
    @Excel(name = "第四象限无功电能 Q4Eq")
    private String d4177;

    @Schema(description = "三相线电压 Uab")
    @Excel(name = "三相线电压 Uab")
    private String d8193;

    @Schema(description = "三相线电压 Ubc")
    @Excel(name = "三相线电压 Ubc")
    private String d8195;

    @Schema(description = "三相线电压 Uca")
    @Excel(name = "三相线电压 Uca")
    private String d8197;

    @Schema(description = "三相相电压 Ua")
    @Excel(name = "三相相电压 Ua")
    private String d8199;

    @Schema(description = "三相相电压 Ub")
    @Excel(name = "三相相电压 Ub")
    private String d8201;

    @Schema(description = "三相相电压 Uc")
    @Excel(name = "三相相电压 Uc")
    private String d8203;

    @Schema(description = "三相电流 Ia")
    @Excel(name = "三相电流 Ia")
    private String d8205;

    @Schema(description = "三相电流 Ib")
    @Excel(name = "三相电流 Ib")
    private String d8207;

    @Schema(description = "三相电流 Ic")
    @Excel(name = "三相电流 Ic")
    private String d8209;

    @Schema(description = "合相有功功率 Pt")
    @Excel(name = "合相有功功率 Pt")
    private String d8211;

    @Schema(description = "A 相有功功率 Pa")
    @Excel(name = "A 相有功功率 Pa")
    private String d8213;

    @Schema(description = "B 相有功功率 Pb")
    @Excel(name = "B 相有功功率 Pb")
    private String d8215;

    @Schema(description = "C 相有功功率 Pc")
    @Excel(name = "C 相有功功率 Pc")
    private String d8217;

    @Schema(description = "合相无功功率 Qt")
    @Excel(name = "合相无功功率 Qt")
    private String d8219;

    @Schema(description = "A 相无功功率 Qa")
    @Excel(name = "A 相无功功率 Qa")
    private String d8221;

    @Schema(description = "B 相无功功率Qb")
    @Excel(name = "B 相无功功率Qb")
    private String d8223;

    @Schema(description = "C 相无功功率 Qc")
    @Excel(name = "C 相无功功率 Qc")
    private String d8225;

    @Schema(description = "合相功率因数 PFt")
    @Excel(name = "合相功率因数 PFt")
    private String d8235;

    @Schema(description = "A 相功率因数 PFa ")
    @Excel(name = "A 相功率因数 PFa")
    private String d8237;

    @Schema(description = "B 相功率因数 PFb")
    @Excel(name = "B 相功率因数 PFb")
    private String d8239;

    @Schema(description = "C 相功率因数 PFc")
    @Excel(name = "C 相功率因数 PFc")
    private String d8241;

    @Schema(description = "频率 Freq")
    @Excel(name = "频率 Freq")
    private String d8261;

}
