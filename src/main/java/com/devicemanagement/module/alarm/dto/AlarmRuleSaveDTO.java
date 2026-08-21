package com.devicemanagement.module.alarm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "告警规则保存DTO")
public class AlarmRuleSaveDTO {

    @Schema(description = "规则ID（编辑时传）")
    private Long id;

    @Schema(description = "关联告警指标ID")
    private Long alarmIndicatorId;

    @NotNull(message = "告警通知频率不能为空")
    @Min(value = 1, message = "告警通知频率必须大于0")
    @Schema(description = "告警通知频率（小时/次）")
    private Integer notifyInterval;

    @NotBlank(message = "顶层条件关系不能为空")
    @Pattern(regexp = "^(AND|OR)$", message = "顶层条件关系只能是 AND 或 OR")
    @Schema(description = "顶层条件关系：OR=或 AND=与", example = "OR")
    private String topConditionRelation;

    @NotBlank(message = "规则关联关系不能为空")
    @Pattern(regexp = "^(AND|OR)$", message = "规则关联关系只能是 AND 或 OR")
    @Schema(description = "规则关联关系：OR=或 AND=与", example = "OR")
    private String ruleRelation;
    @Valid
    @NotEmpty(message = "预警规则条件不能为空")
    @Size(max = 20, message = "最多支持20条条件")
    @Schema(description = "规则条件列表")
    private List<ConditionItem> conditions;

    @Data
    @Schema(description = "规则条件项")
    public static class ConditionItem {

        @Schema(description = "条件ID（编辑时传）")
        private Long id;

        @Schema(description = "父条件ID（用于分组）")
        private Long parentId;

        @NotBlank(message = "告警属性不能为空")
        @Schema(description = "告警属性")
        private String alarmAttribute;

        @NotBlank(message = "比较条件不能为空")
        @Pattern(regexp = "^(>|<|<=|>=|=)$", message = "比较条件只能是 > < <= >= =")
        @Schema(description = "比较条件",example = ">")
        private String operator;

        @NotNull(message = "告警阈值不能为空")
        @Schema(description = "告警阈值")
        private BigDecimal threshold;

        @Schema(description = "条件排序")
        private Integer sort;
    }
}