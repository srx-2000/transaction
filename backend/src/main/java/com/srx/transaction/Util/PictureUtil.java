package com.srx.transaction.Util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.AnonymousCOSCredentials;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.srx.transaction.Exception.PathException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用腾讯云的cos服务搭建类似于图床的存储，并在数据库中保存图片路径
 */
public class PictureUtil {

    public static String secretId = "your secretId";
    public static String secretKey = "your secretKey";

    // 指定文件将要存放的存储桶
    private static String bucketName = "bucketName";
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

        String key = getKey(shopUUID, goodsUUID, true, mode);
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        cosClient.shutdown();
        return true;
    }

    public static String getShopUrl(String shopUUID, Integer mode) {
        ListObjectsRequest request = new ListObjectsRequest();
        request.setBucketName(bucketName);
        request.setPrefix(shopUUID + "/");
        request.setMaxKeys(20);
        // 生成匿名的请求签名，需要重新初始化一个匿名的 cosClient
        // 初始化用户身份信息, 匿名身份不用传入 SecretId、SecretKey 等密钥信息
        COSCredentials cred = new AnonymousCOSCredentials();
        // 设置 bucket 的区域，COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(regionName));
        // 生成 cos 客户端
        COSClient cosClient = new COSClient(cred, clientConfig);
        String key = getKey(shopUUID, null, false, mode);
        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);
        URL url = cosClient.generatePresignedUrl(req);
        System.out.println(url.toString());
        cosClient.shutdown();
        return url.toString();
    }

    private static String getKey(String shopUUID, String goodsUUID, Boolean upload, Integer mode) {
        String key;
        if (upload) {
            if (goodsUUID != null && !goodsUUID.equals("")) {
                key = rootPath + "/" + shopUUID + "/" + goodsUUID + "/" + CodeUtil.get_uuid() + ".png";
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

    private static List<String> getGoodsKey(String shopUUID, String goodsUUID) {
        COSClient cosClient = initClient();
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        // 设置bucket名称
        listObjectsRequest.setBucketName(bucketName);
        // prefix表示列出的object的key以prefix开始
        List<String> keyList = new ArrayList<>();
        if (goodsUUID != null && !goodsUUID.equals("")) {
            listObjectsRequest.setPrefix(rootPath + "/" + shopUUID + "/" + goodsUUID + "/");
            // deliter表示分隔符, 设置为/表示列出当前目录下的object, 设置为空表示列出所有的object
            listObjectsRequest.setDelimiter("/");
            // 设置最大遍历出多少个对象, 一次listobject最大支持1000
            listObjectsRequest.setMaxKeys(1000);
            ObjectListing objectListing = null;

            do {
                try {
                    objectListing = cosClient.listObjects(listObjectsRequest);
                    List<COSObjectSummary> objectSummaries = objectListing.getObjectSummaries();
                    for (COSObjectSummary c : objectSummaries) {
                        keyList.add(c.getKey());
                    }
                } catch (CosServiceException e) {
                    e.printStackTrace();
                    return null;
                } catch (CosClientException e) {
                    e.printStackTrace();
                    return null;
                }
            } while (objectListing.isTruncated());
        } else {
            throw new PathException("请输入商品UUID以获取商品地址");
        }
        return keyList;
    }

    public static List<String> getGoodsUrl(String shopUUID, String goodsUUID) {
        ListObjectsRequest request = new ListObjectsRequest();
        request.setBucketName(bucketName);
        request.setPrefix(shopUUID + "/");
        request.setMaxKeys(1000);
        // 生成匿名的请求签名，需要重新初始化一个匿名的 cosClient
        // 初始化用户身份信息, 匿名身份不用传入 SecretId、SecretKey 等密钥信息
        COSCredentials cred = new AnonymousCOSCredentials();
        // 设置 bucket 的区域，COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(regionName));
        // 生成 cos 客户端
        COSClient cosClient = new COSClient(cred, clientConfig);
        List<String> goodsKey = getGoodsKey(shopUUID, goodsUUID);
        List<String> urlList = new ArrayList<>();
        for (String key : goodsKey) {
            GeneratePresignedUrlRequest req =
                    new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);
            URL url = cosClient.generatePresignedUrl(req);
            System.out.println(url.toString());
            urlList.add(url.toString());
        }
        cosClient.shutdown();
        return urlList;
    }

}
