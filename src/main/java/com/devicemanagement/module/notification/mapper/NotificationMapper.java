package com.devicemanagement.module.notification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.devicemanagement.module.notification.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;

@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {

    @Update("UPDATE notification SET is_read = 1, read_time = #{readTime} WHERE id = #{id}")
    int markAsRead(@Param("id") Long id, @Param("readTime") Date readTime);

    @Update("UPDATE notification SET is_read = 1, read_time = #{readTime} WHERE user_id = #{userId} AND is_read = 0")
    int markAllAsRead(@Param("userId") Long userId, @Param("readTime") Date readTime);

    @Select("SELECT COUNT(*) FROM notification WHERE user_id = #{userId} AND is_read = 0")
    long countUnreadByUserId(@Param("userId") Long userId);
}
