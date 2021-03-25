package com.srx.transaction.Util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.AnonymousCOSCredentials;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.srx.transaction.Exception.PathException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URL;

/**
 * 使用腾讯云的cos服务搭建类似于图床的存储，并在数据库中保存图片路径
 */
public class PictureUtil {

    public static String secretId = "AKIDWo1H9UK0sVwPgUIi6E5fR0b0RYSxEose";
    public static String secretKey = "e3XlVIp68uQP8bxTEj8G9nTvpysaaLs9";

    // 指定文件将要存放的存储桶
    private static String bucketName = "transaction-1304038944";
    private static String regionName = "ap-beijing";
    //存储根路径
    private static String rootPath = "root";

    public final static int LICENSE = 1;
    public final static int IDENTITY_FRONT = 2;
    public final static int IDENTITY_BACK = 3;


    private static COSClient initClient() {
        // 1 初始化用户身份信息（secretId, secretKey）。
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
// clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region(regionName);
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);
        return cosClient;
    }

    public static Boolean uploadPicture(MultipartFile multipartFile, String shopUUID, String goodsUUID, Integer mode) throws Exception {
        COSClient cosClient = initClient();
        // 指定要上传的文件
        /**
         * 这里使用file工具类中的方法将multipartfile转为了file
         */
        File file = FileUtil.multipartFileToFile(multipartFile);

        String key=getKey(shopUUID,goodsUUID,mode);
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        cosClient.shutdown();
        return true;
    }

    public static String getUrl(String shopUUID, String goodsUUID, Integer mode) {
        ListObjectsRequest request=new ListObjectsRequest();
        request.setBucketName(bucketName);
        request.setPrefix(shopUUID+"/");
        request.setMaxKeys(20);
        // 生成匿名的请求签名，需要重新初始化一个匿名的 cosClient
        // 初始化用户身份信息, 匿名身份不用传入 SecretId、SecretKey 等密钥信息
        COSCredentials cred = new AnonymousCOSCredentials();
        // 设置 bucket 的区域，COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(regionName));
        // 生成 cos 客户端
        COSClient cosClient = new COSClient(cred, clientConfig);
        String key = getKey(shopUUID,goodsUUID,mode);
        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);
        URL url = cosClient.generatePresignedUrl(req);
        System.out.println(url.toString());
        cosClient.shutdown();
        return url.toString();
    }

    private static String getKey(String shopUUID, String goodsUUID, Integer mode) {
        String key;
        if (goodsUUID != null && !goodsUUID.equals("")) {
            key = rootPath + "/" + shopUUID + "/" + goodsUUID + "/" + CodeUtil.get_uuid()+ ".png";
        } else {
            if (mode == LICENSE) {
                key = rootPath + "/" + shopUUID + "/license.png";
            } else if (mode == IDENTITY_FRONT) {
                key = rootPath + "/" + shopUUID + "/identity_front.png";
            } else if (mode == IDENTITY_BACK) {
                key = rootPath + "/" + shopUUID + "/identity_back.png";
            } else {
                throw new PathException("图片保存路径错误");
            }
        }
        return key;
    }

}
