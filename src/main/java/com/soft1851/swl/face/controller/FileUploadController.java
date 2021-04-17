package com.soft1851.swl.face.controller;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import com.soft1851.swl.face.annocation.ControllerWebLog;
import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.common.ResultCode;
import com.soft1851.swl.face.service.UploadService;
import com.soft1851.swl.face.util.AliImageReviewUtil;
import com.soft1851.swl.face.util.FileResource;
import com.soft1851.swl.face.util.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/15
 */

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "文件相关上传Controller",tags = {"文件相关上传Controller"})
@RequestMapping("fs")
public class FileUploadController {


    private final UploadService uploadService;
    private final FileResource fileResource;
    private final AliImageReviewUtil aliImageReviewUtil;
    private final GridFsTemplate gridFsTemplate;
    private final GridFSBucket gridFSBucket;

    /**
     * 上传用户头像
     * @param userId 用户id
     * @param file 文件对象
     * @return 封装结果
     * @throws Exception 异常
     */
    @ApiOperation(value = "上传用户头像",notes = "上传用户头像",httpMethod = "POST")
    @PostMapping("/uploadFace")
    @ControllerWebLog
    public  ResponseResult uploadFace(@RequestParam String userId, MultipartFile file) throws Exception {
        String path;
        if(file != null) {
            //获得文件上传名称
            String fileName = file.getOriginalFilename();
            //判断文件名不能为空
            if (StringUtils.isNotBlank(fileName)) {
                // 分割命名
                String[] fileNameArr = fileName.split("\\.");
                //获得后端
                String suffix = fileNameArr[fileNameArr.length - 1];
                //判断后缀是否符合我们的预定义范围
                if (!"png".equalsIgnoreCase(suffix) && !"jpg".equalsIgnoreCase(suffix) &&
                        !"jpeg".equalsIgnoreCase(suffix)) {
                    return ResponseResult.failure(ResultCode.FILE_UPLOAD_NULL_ERROR);
                }
                //执行上传服务，得到回调路径
                path = uploadService.uploadFdfs(file,userId, suffix);
            } else {
                return ResponseResult.failure(ResultCode.FILE_UPLOAD_NULL_ERROR);
            }
        }else {
            return ResponseResult.failure(ResultCode.FILE_UPLOAD_NULL_ERROR);
        }
        log.info("path = "+path);
        String finalPath;
        if(StringUtils.isNotBlank(path)){
            finalPath = fileResource.getHost()+path;
        }else {
            return ResponseResult.failure(ResultCode.FILE_UPLOAD_NULL_ERROR);
        }

        return ResponseResult.success(doAliImageReview(finalPath));
    }


    @ApiOperation(value = "人脸入库",notes = "人脸入库",httpMethod = "POST")
    @PostMapping("/uploadToGridFs")
    @ControllerWebLog
    public ResponseResult uploadToGridFs(@RequestParam(value = "file") MultipartFile multipartFile) throws Exception{

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
     * 从gridFS读取文件
     *
     * @param faceId 人脸id
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    @ApiOperation(value = "从gridFS读取文件",notes = "从gridFS读取文件",httpMethod = "POST")
    @PostMapping("/readInGridFs")
    @ControllerWebLog
    public ResponseResult readInGridFs(@RequestParam String faceId, HttpServletRequest request,
                             HttpServletResponse response) throws Exception{
        //根据id查询文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(faceId)));
        if (gridFSFile == null) {
            throw new RuntimeException("No file with id: " + faceId);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseResult.success(new String(bytes));
    }

    /**
     * 从gridfs中读取读取图片内容，并且返回base64
     *
     * @param faceId 人脸照片
     * @param request 请求
     * @param response 响应
     * @return 返回
     * @throws Exception 异常
     */
    @ApiOperation(value = "从gridfs中读取读取图片内容，并且返回base64",notes = "从gridfs中读取读取图片内容，并且返回base64",httpMethod = "GET")
    @GetMapping("/readFace64")
    @ControllerWebLog
    public  ResponseResult readFace64(@RequestParam String faceId,HttpServletRequest request,HttpServletResponse response) throws  Exception{

        // 0.获得gridfs中人脸文件
        File myFace = readFileFromGridFs(faceId);
        // 1.转换人脸为base64
        String base64Face = FileUtil.fileToBase64(myFace);
        return ResponseResult.success(base64Face);
    }

    private File readFileFromGridFs(String faceId) throws Exception {
        GridFSFindIterable files = gridFSBucket.find(Filters.eq("_id", new ObjectId(faceId)));
        GridFSFile gridFsFile = files.first();
        //GridFSFile gridFsFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(faceId)));
        if (gridFsFile == null) {
            ResponseResult.failure(ResultCode.FILE_NOT_EXIST_ERROR);
        }
        String fileName = gridFsFile.getFilename();
        System.out.println(fileName);
        // 获取文件流，保存文件到本地或者服务器的临时目录
        File fileTemp = new File("C:/Users/HP/Desktop/毕业设计/项目/faces");
        if (!fileTemp.exists()) {
            fileTemp.mkdirs();
        }
        File myFile = new File("C:/Users/HP/Desktop/毕业设计/项目/faces/" + fileName);
        // 创建文件输出流
        OutputStream os = new FileOutputStream(myFile);
        // 下载到服务器或者本地
        gridFSBucket.downloadToStream(new ObjectId(faceId),os);
        return myFile;
    }

    /**
     *
     * 检测不通过的默认图片
     */
    public static final String FAILED_IMAGE_URL = "https://swl-kuzma.oss-cn-beijing.aliyuncs.com/markdown/20201120182215.png";

    private String doAliImageReview(String pendingImageUrl){
        log.info(pendingImageUrl);
        boolean result = false;
        try {
            result = aliImageReviewUtil.reviewImage(pendingImageUrl);
        }catch (Exception e){
            System.err.println("图片识别出错");
        }
        if(!result){
            return FAILED_IMAGE_URL;
        }
        return pendingImageUrl;
    }
}
