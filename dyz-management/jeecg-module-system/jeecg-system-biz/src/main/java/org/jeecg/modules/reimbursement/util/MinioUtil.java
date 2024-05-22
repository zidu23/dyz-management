package org.jeecg.modules.reimbursement.util;


import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

/**
 * Minio 文件上传
 */
@Slf4j
@Component
public class MinioUtil {

//    public static void main(String[] args) {
//        uploadTest();
//    }

    private MinioClient minioClient;

    public static void uploadTest() {
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream("D:\\Temp/test1.txt");
            // 1.创建minio链接客户端
            MinioClient minioClient = MinioClient.builder()
                    .credentials("minio", "admin@wlb")
                    .endpoint("http://8.219.246.57:9000")
                    .build();
            // 2.上传
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    // 文件名
                    .object("test.txt")
                    // 文件类型
                    .contentType("text/html")
                    // 桶名称
                    .bucket("dyz")
                    // 文件流
                    .stream(fileInputStream, fileInputStream.available(), -1)
                    .build();
            minioClient.putObject(putObjectArgs);
            System.out.println("8.219.246.57:9000");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取一个MinioClient
     *
     * @return
     */
    public MinioClient getMinioClient() {

        try {
            MinioClient minioClient = MinioClient.builder()
                    .credentials("minio", "admin@wlb")
                    .endpoint("http://8.219.246.57:9000")
                    .build();
            this.minioClient = minioClient;
            return minioClient;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("创建minio客户端失败！", e);
            return null;
        }
    }

    /**
     * 检测某个桶内是否存在某个文件
     *
     * @param objectName 文件名称
     * @param bucketName 桶名称
     */
    public boolean getBucketFileExist(String objectName, String bucketName) throws Exception {
        if (!StringUtils.hasLength(objectName) || !StringUtils.hasLength(bucketName)) {
            throw new RuntimeException("检测文件的时候，文件名和桶名不能为空！");
        }
        try {
            // 判断文件是否存在
            return (minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build()) &&
                    minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build()) != null);

        } catch (ErrorResponseException e) {
            log.info("文件不存在 ! Object does not exist");
            return false;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * 删除文件夹
     *
     * @param bucketName 桶名
     * @param objectName 文件夹名
     * @param isDeep     是否递归删除
     * @return
     */
    public Boolean deleteBucketFolder(String bucketName, String objectName, Boolean isDeep) {
        if (!StringUtils.hasLength(bucketName) || !StringUtils.hasLength(objectName)) {
            throw new RuntimeException("删除文件夹的时候，桶名或文件名不能为空！");
        }
        try {
            ListObjectsArgs args = ListObjectsArgs.builder().bucket(bucketName).prefix(objectName + "/").recursive(isDeep).build();
            Iterable<Result<Item>> listObjects = minioClient.listObjects(args);
            listObjects.forEach(objectResult -> {
                try {
                    Item item = objectResult.get();
                    System.out.println(item.objectName());
                    minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(item.objectName()).build());
                } catch (Exception e) {
                    log.info("删除文件夹中的文件异常", e);
                }
            });
            return true;
        } catch (Exception e) {
            log.info("删除文件夹失败");
            return false;
        }
    }

    /**
     * 文件上传文件
     *
     * @param file       文件
     * @param bucketName 桶名
     * @param objectName 文件名,如果有文件夹则格式为 "文件夹名/文件名"
     * @return
     */
    public Boolean uploadFile(MultipartFile file, String bucketName, String objectName) {

        if (Objects.isNull(file) || Objects.isNull(bucketName)) {
            throw new RuntimeException("文件或者桶名参数不全！");
        }

        try {
            //资源的媒体类型
            String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;//默认未知二进制流
            InputStream inputStream = file.getInputStream();
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucketName).object(objectName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            ObjectWriteResponse response = minioClient.putObject(args);
            inputStream.close();
            return response.etag() != null;
        } catch (Exception e) {
            log.info("单文件上传失败！", e);
            return false;
        }
    }

    public static String generateUniqueId(int length) {
        UUID uuid = UUID.randomUUID();
        String hash = uuid.toString().replaceAll("-", "");
        return hash.substring(0, Math.min(length, hash.length()));
    }


}
