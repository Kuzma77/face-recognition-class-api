package com.soft1851.swl.face.mapper;

import com.soft1851.swl.face.entity.OwnerFace;
import org.apache.ibatis.annotations.Insert;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/15
 */
public interface OwnerFaceMapper {


    /**
     * 添加人脸关系
     *
     * @param ownerFace
     */
    @Insert("INSERT INTO owner_face VALUES(#{id},#{ownerId},#{faceId})")
    void addOne(OwnerFace ownerFace);
}
