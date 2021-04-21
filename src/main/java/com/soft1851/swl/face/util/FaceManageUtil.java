package com.soft1851.swl.face.util;



import com.aliyun.facebody20191230.Client;
import com.aliyun.tea.*;
import com.aliyun.facebody20191230.*;
import com.aliyun.facebody20191230.models.*;
import com.aliyun.teaopenapi.*;
import com.aliyun.teaopenapi.models.*;
import com.google.gson.Gson;
import com.soft1851.swl.face.dto.AddFaceDto;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author wl_sun
 * @description 人脸库管理工具类
 * @Data 2021/4/21
 */

@Component
public class FaceManageUtil {

    public final com.aliyun.facebody20191230.Client client = FaceManageUtil.createClient("LTAI4G3RbEnYyXpd2ez6jbo4", "6vVUWNdOhUunRQCLyrjszgKwVZMjkv");

    public FaceManageUtil() throws Exception {
    }

    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.facebody20191230.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "facebody.cn-shanghai.aliyuncs.com";
        return new com.aliyun.facebody20191230.Client(config);
    }


    /**
     * 判断人脸样本是否存在
     * @param userId
     * @return
     * @throws Exception
     */
    public boolean getFaceEntity(String userId) throws Exception{
        //com.aliyun.facebody20191230.Client client = FaceManageUtil.createClient("LTAI4G3RbEnYyXpd2ez6jbo4", "6vVUWNdOhUunRQCLyrjszgKwVZMjkv");
        GetFaceEntityRequest getFaceEntityRequest = new GetFaceEntityRequest()
                .setDbName("face")
                .setEntityId(userId);
        GetFaceEntityResponse response = client.getFaceEntity(getFaceEntityRequest);
        Map<String,Object> map = JsonUtil.jsonToPojo(new Gson().toJson(response), Map.class);
        assert map != null;
        Map<String,Object> body = JsonUtil.jsonToPojo(new Gson().toJson(map.get("body")), Map.class);
        return body.containsKey("data");
    }




    /**
     * 添加人脸样本
     * @param userId
     * @param userName
     * @return
     * @throws Exception
     */
    public void addFaceEntity(String userId,String userName) throws Exception{
        //com.aliyun.facebody20191230.Client client = FaceManageUtil.createClient("LTAI4G3RbEnYyXpd2ez6jbo4", "6vVUWNdOhUunRQCLyrjszgKwVZMjkv");
        //添加人脸样本
        AddFaceEntityRequest addFaceEntityRequest = new AddFaceEntityRequest()
                .setDbName("face")
                .setEntityId(userId)
                .setLabels(userName);
        client.addFaceEntity(addFaceEntityRequest);
    }


    /**
     * 添加人脸数据
     * @param addFaceDto
     * @return
     * @throws Exception
     */
    public Map<String,Object> addFaceData(AddFaceDto addFaceDto) throws Exception{
        //com.aliyun.facebody20191230.Client client = FaceManageUtil.createClient("LTAI4G3RbEnYyXpd2ez6jbo4", "6vVUWNdOhUunRQCLyrjszgKwVZMjkv");
        //添加人脸数据
        AddFaceRequest addFaceRequest = new AddFaceRequest()
                .setImageUrl(addFaceDto.getImgUrl())
                                .setDbName("face")
                .setEntityId(addFaceDto.getUserId())
                .setExtraData(addFaceDto.getUserName());
        AddFaceResponse response = client.addFace(addFaceRequest);
        Map<String,Object> map = JsonUtil.jsonToPojo(new Gson().toJson(response), Map.class);
        assert map != null;
        return JsonUtil.jsonToPojo(new Gson().toJson(map.get("body")), Map.class);
    }


    public void SearchFace()

    public static void main(String[] args_) throws Exception {
        com.aliyun.facebody20191230.Client client = FaceManageUtil.createClient("LTAI4G3RbEnYyXpd2ez6jbo4", "6vVUWNdOhUunRQCLyrjszgKwVZMjkv");
//        CreateFaceDbRequest createFaceDbRequest = new CreateFaceDbRequest()
//                .setName("face");
        // 复制代码运行请自行打印 API 的返回值
        //创建数据库
        //client.createFaceDb(createFaceDbRequest);
        //查看数据库列表
        //client.listFaceDbs();


//        //添加人脸样本
//        AddFaceEntityRequest addFaceEntityRequest = new AddFaceEntityRequest()
//                .setDbName("face")
//                .setEntityId("1802343117")
//                .setLabels("林俊杰");
//        // 复制代码运行请自行打印 API 的返回值
//        client.addFaceEntity(addFaceEntityRequest);
//
//        //添加人脸数据
//        AddFaceRequest addFaceRequest = new AddFaceRequest()
//                .setImageUrl("https://face-manage-kuzma.oss-cn-shanghai.aliyuncs.com/faces1/a3b36aa1-15a0-4c1c-bb28-2e2aecbc93df.jpg?versionId=CAEQGhiBgMCt2OvOxxciIDU5NmUzMDY2NmZiNzRjNjY5MmQ4ZDE1YTJlYzQzMTE4")
//                                .setDbName("face")
//                                .setEntityId("1802343117")
//                                .setExtraData("林俊杰");
//        // 复制代码运行请自行打印 API 的返回值
//        client.addFace(addFaceRequest);


        //查询人脸样本
        GetFaceEntityRequest getFaceEntityRequest = new GetFaceEntityRequest()
                .setDbName("face")
                .setEntityId("180234311");
        // 复制代码运行请自行打印 API 的返回值
        GetFaceEntityResponse response = client.getFaceEntity(getFaceEntityRequest);
        Map<String,Object> map = JsonUtil.jsonToPojo(new Gson().toJson(response), Map.class);
        assert map != null;
        Map<String,Object> body = JsonUtil.jsonToPojo(new Gson().toJson(map.get("body")), Map.class);
        System.out.println(body.containsKey("data"));
    }
}
