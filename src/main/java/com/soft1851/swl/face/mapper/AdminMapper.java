package com.soft1851.swl.face.mapper;

import com.soft1851.swl.face.entity.Admin;
import com.soft1851.swl.face.entity.Student;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/17
 */
public interface AdminMapper extends Mapper<Admin> {

    /**
     * 通过id查询管理员
     *
     * @param adminId
     * @return
     */
    @Select("SELECT * FROM admin WHERE admin_id = #{adminId}")
    Admin getAdminById(String adminId);
}
