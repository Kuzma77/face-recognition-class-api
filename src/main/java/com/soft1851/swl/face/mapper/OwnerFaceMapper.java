package com.soft1851.swl.face.mapper;

import com.soft1851.swl.face.entity.OwnerFace;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/15
 */
public interface OwnerFaceMapper extends Mapper<OwnerFace> {


    /**
     * 添加人脸关系
     *
     * @param ownerFace
     */
    @Insert("INSERT INTO owner_face VALUES(#{id},#{ownerId},#{faceId})")
    void addOne(OwnerFace ownerFace);

    /**
     * 通过用户id查询faceId
     *
     * @param ownerId
     * @return
     */
    @Select("SELECT * FROM owner_face WHERE owner_id = #{ownerId}")
    OwnerFace queryFaceIdByOwnerId(String ownerId);
}
