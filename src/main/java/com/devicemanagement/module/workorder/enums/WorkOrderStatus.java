package com.devicemanagement.module.workorder.enums;

import lombok.Getter;

/** 工单状态：1 未处理、2 处理中、3 待核验、4 核验完成、5 核验未通过。 */
@Getter
public enum WorkOrderStatus {

    UNPROCESSED(1, "未处理"),
    PROCESSING(2, "处理中"),
    PENDING_VERIFY(3, "待核验"),
    VERIFIED(4, "核验完成"),
    VERIFY_REJECTED(5, "核验未通过");

    private final int code;
    private final String desc;

    WorkOrderStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /** 根据 code 查找枚举，找不到返回 null。 */
    public static WorkOrderStatus of(Integer code) {
        if (code == null) {
            return null;
        }
        for (WorkOrderStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
}
