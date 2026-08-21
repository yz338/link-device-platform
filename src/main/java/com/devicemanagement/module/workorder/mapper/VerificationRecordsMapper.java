package com.devicemanagement.module.workorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.devicemanagement.module.workorder.bean.VerificationRecords;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface VerificationRecordsMapper extends BaseMapper<VerificationRecords> {

    /** 按核验结果统计记录数（0=未通过，1=通过）。 */
    @Select("SELECT COUNT(*) FROM verification_records WHERE verification_result = #{result}")
    long countByVerificationResult(@Param("result") int result);
}
