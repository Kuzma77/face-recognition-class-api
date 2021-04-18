package com.soft1851.swl.face.repository;

import com.soft1851.swl.face.mo.Log;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/18
 */
public interface LogRepository extends MongoRepository<Log,String> {
}
