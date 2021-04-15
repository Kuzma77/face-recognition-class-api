package com.soft1851.swl.face.util;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/13
 */

import org.apache.commons.codec.binary.Base64;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;


/**
 * 文件读取工具类
 */
public class FileUtil {

    /**
     * 读取文件内容，作为字符串返回
     */
    public static String readFileAsString(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        }

        if (file.length() > 1024 * 1024 * 1024) {
            throw new IOException("File is too large");
        }

        StringBuilder sb = new StringBuilder((int) (file.length()));
        // 创建字节输入流
        FileInputStream fis = new FileInputStream(filePath);
        // 创建一个长度为10240的Buffer
        byte[] bbuf = new byte[10240];
        // 用于保存实际读取的字节数
        int hasRead = 0;
        while ( (hasRead = fis.read(bbuf)) > 0 ) {
            sb.append(new String(bbuf, 0, hasRead));
        }
        fis.close();
        return sb.toString();
    }

    /**
     * 根据文件路径读取byte[] 数组
     */
    public static byte[] readFileByBytes(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
            BufferedInputStream in = null;

            try {
                in = new BufferedInputStream(new FileInputStream(file));
                short bufSize = 1024;
                byte[] buffer = new byte[bufSize];
                int len1;
                while (-1 != (len1 = in.read(buffer, 0, bufSize))) {
                    bos.write(buffer, 0, len1);
                }

                byte[] var7 = bos.toByteArray();
                return var7;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException var14) {
                    var14.printStackTrace();
                }

                bos.close();
            }
        }
    }

    /**
     * 文件流下载，在浏览器展示
     *
     * @param response 响应
     * @param file     文件从盘符开始的完整路径
     */
    public static void downloadFileByStream(HttpServletResponse response, File file) {
        String filePath = file.getPath();
        System.out.println("filePath = " + filePath);
        // 对encode过的filePath处理
        if (filePath.contains("%")) {
            try {
                filePath = URLDecoder.decode(filePath, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        ServletOutputStream out = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            String[] dir = filePath.split("/");
            String fileName = dir[dir.length - 1];
            String[] array = fileName.split("[.]");
            String fileType = array[array.length - 1].toLowerCase();
            // 判断图片类型
            if ("jpg,jepg,gif,png".contains(fileType)) {
                response.setContentType("image/" + fileType);
                // 判断pdf类型
            } else if ("pdf".contains(fileType)) {
                response.setContentType("application/pdf");
            } else {
                // 设置multipart
                response.setContentType("multipart/form-data");
            }
            out = response.getOutputStream();
            // 读取文件流
            int len = 0;
            byte[] buffer = new byte[1024 * 10];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert out != null;
                out.close();
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 文件转换为base64
     *
     * @param file 入参
     * @return String
     */
    public static String fileToBase64(File file) {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in;
        byte[] fileData = null;
        // 读取文件字节数组
        try {
            in = new FileInputStream(file);
            fileData = new byte[in.available()];
            in.read(fileData);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 对字节数组Base64编码并且返回
//        BASE64Encoder encoder = new BASE64Encoder();
//        return encoder.encode(fileData);
        return Base64.encodeBase64String(fileData);
    }
}

