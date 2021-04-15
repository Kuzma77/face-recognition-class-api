package com.soft1851.swl.face.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/15
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "owner_face")
@Builder
public class OwnerFace {
    @Id
    @GeneratedValue(generator = "JDBC")
    @NotNull(message = "id不能为空")
    private String id;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "face_id")
    private String faceId;
}
