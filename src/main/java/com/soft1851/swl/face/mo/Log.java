package com.soft1851.swl.face.mo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/18
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("log")
public class Log {
    @Id
    private String id;
    @Field("operator_id")
    private String operatorId;
    @Field("object_id")
    private String objectId;
    @Field("type")
    private String type;
    @Field("content")
    private String content;
    @Field("create_time")
    private Date createTime;
}
