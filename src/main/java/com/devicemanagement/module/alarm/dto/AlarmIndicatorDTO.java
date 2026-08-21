package com.devicemanagement.module.alarm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "告警指标DTO")
public class AlarmIndicatorDTO {

    @Schema(description = "告警指标ID（编辑时传）")
    private Long id;

    @Schema(description = "告警指标名称")
    @NotBlank(message = "指标名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String indicatorName;

    @Schema(description = "设备类别名称")
    @NotBlank(message = "设备类别不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String deviceCategoryName;

    @Schema(description = "作用设备ID列表（多设备生效），新增/编辑时必传")
    @NotEmpty(message = "作用设备不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private List<Long> deviceIds;

    @Schema(description = "指标描述")
    private String description;

    @Schema(description = "状态：1=启用 2=禁用")
    private Byte status;

    @Schema(description = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页条数", example = "10")
    private Integer pageSize = 10;

    // 校验分组
    public interface AddGroup {}
    public interface UpdateGroup {}
}