package com.soft1851.swl.face.controller;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.soft1851.swl.face.annocation.ControllerWebLog;
import com.soft1851.swl.face.common.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/13
 */

@RestController
@Slf4j
@RequestMapping("/gridfs")
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags = "gridfs相关接口",value = "提供gridfs接口相关的Rest API")
public class GridFsController {
    @Autowired
    private GridFsTemplate gridFsTemplate;
    /**
     * 上传文件
     *
     * @param multipartFile 文件
     * @return 上传成功文件id
     */
    @PostMapping("/upload")
    @ControllerWebLog
    @ApiOperation(value = "上传文件",notes = "上传文件")
    public ResponseResult uploadFile(@RequestParam(value = "file") MultipartFile multipartFile) {
        // 设置meta数据值
        Map<String, String> metaData = new HashMap<>(8);
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 获取文件的源名称
        String fileName = multipartFile.getOriginalFilename();
        // 进行文件存储
        assert inputStream != null;
        ObjectId objectId = gridFsTemplate.store(inputStream, fileName, metaData);
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseResult.success(objectId.toHexString());
    }
    /**
     * 获取文件信息
     *
     * @param fileId 文件id
     */
    @GetMapping("/get/{fileId}")
    @ControllerWebLog
    @ApiOperation(value = "获取文件信息",notes = "获取文件信息")
    public void getFile(@PathVariable("fileId") String fileId, HttpServletRequest request, HttpServletResponse response) {
        //根据id查询文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        if (gridFSFile == null) {
            throw new RuntimeException("No file with id: " + fileId);
        }
        System.out.println(gridFSFile.getFilename());
        //获取流对象
        GridFsResource resource = gridFsTemplate.getResource(gridFSFile);
        InputStream inputStream;
        String content = null;
        byte[] bytes = new byte[(int) gridFSFile.getLength()];
        try {
            inputStream = resource.getInputStream();
            int read = inputStream.read(bytes);
            inputStream.close();
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(bytes);
            outputStream.close();
            ////获取流中的数据
            //content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            ////获取byte[]信息
            //bytes = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 删除文件
     *
     * @param fileId 文件id
     */
    @DeleteMapping("/delete")
    @ControllerWebLog
    @ApiOperation(value = "删除文件",notes = "删除文件")
    public ResponseResult deleteFile(@RequestParam(value = "fileId") String fileId) {
        // 根据文件id删除fs.files和fs.chunks中的记录
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is(fileId)));
        return ResponseResult.success();
    }
}