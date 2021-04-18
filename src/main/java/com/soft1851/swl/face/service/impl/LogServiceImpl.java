package com.soft1851.swl.face.service.impl;


import com.soft1851.swl.face.mo.Log;

import com.soft1851.swl.face.repository.LogRepository;
import com.soft1851.swl.face.service.LogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/18
 */

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LogServiceImpl implements LogService {
    public final LogRepository logRepository;
    @Override
    public void saveOneLog(Log log) {
        logRepository.save(log);
    }
}
