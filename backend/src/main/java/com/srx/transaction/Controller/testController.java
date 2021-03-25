//package com.srx.transaction.Controller;
//
//import com.srx.transaction.Util.CodeUtil;
//import com.srx.transaction.Util.PictureUtil;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
//@RestController
//public class testController {
//    @PostMapping("/upload")
//    public Boolean upload(@RequestPart MultipartFile file) throws Exception {
//        Boolean aBoolean = PictureUtil.uploadPicture(file, CodeUtil.get_uuid(), null, PictureUtil.LICENSE);
//        return aBoolean;
//    }
//
//    @PostMapping("/uploadFiles")
//    public Boolean upload(@RequestPart MultipartFile[] file) throws Exception {
//        int count = 0;
//        for (int i = 0; i < file.length; i++) {
//            Boolean aBoolean = PictureUtil.uploadPicture(file[i], "f653b418-8970-47df-9462-e43d1dfb1a78", "ce79d103-f2ce-4b8d-9478-edc7eab399be", PictureUtil.LICENSE);
//            if (aBoolean) {
//                count++;
//            }
//        }
//        if (count == file.length) {
//            return true;
//        }
//        return false;
//    }
//
//    @GetMapping("/getUrl")
//    public String getUrl(@RequestParam String shopUUID, @RequestParam(required = false) String goodsUUID) {
//        if (goodsUUID == null) {
//            return PictureUtil.getUrl(shopUUID, null, PictureUtil.LICENSE);
//        } else {
//            return PictureUtil.getUrl(shopUUID,goodsUUID,null);
//        }
//    }
//}
