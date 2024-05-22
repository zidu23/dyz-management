package org.jeecg.modules.reimbursement.controller;


import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.cardReplacement.service.cardReplacementJobService;
import org.jeecg.modules.reimbursement.entity.Reimbursement;
import org.jeecg.modules.reimbursement.service.ReimbursementService;
import org.jeecg.modules.reimbursement.util.MinioUtil;
import org.jeecg.modules.system.entity.LeaveInfo;
import org.jeecg.modules.system.service.ILeaveInfoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/sys/reimbursement")
@Slf4j
@Api(tags = "报销申请接口")
public class ReimbursementController {

    @Resource
    private ReimbursementService reimbursementService;

    @Resource
    private ILeaveInfoService leaveInfoService;

    @Resource
    private cardReplacementJobService cardReplacementJobService;

    @Resource
    private MinioUtil minioUtil;

    Integer IDID;

    /**
     * get = '/sys/reimbursement/get',
     * set = '/sys/reimbursement/set',
     * getusername = '/sys/reimbursement/getusername',
     * getuserdepartment = '/sys/reimbursement/getuserdepartment',
     * getuserpost = '/sys/reimbursement/getuserpost',
     * getuserid = '/sys/reimbursement/getuserid',
     * settime = '/sys/reimbursement/settime',
     * update = '/sys/reimbursement/update',
     * delete = '/sys/reimbursement/delete',
     * open = '/sys/reimbursement/open',
     */

    @GetMapping("/get")
    public Result<List<Reimbursement>> result1(String Username) {
        Result<List<Reimbursement>> result = new Result<List<Reimbursement>>();
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<Reimbursement> reimbursementList = reimbursementService.findcardReimbursement(user.getRealname());
        result.setResult(reimbursementList);
        return result;
    }

    @GetMapping("/set")
    public Result<Reimbursement> result2(Reimbursement reimbursement) {
        Result<Reimbursement> result = new Result<Reimbursement>();
        if (ObjectUtil.isEmpty(reimbursement)) {
            return result;
        }
        System.out.println(reimbursement.getTime1() + "+" + reimbursement.getTime2());
        reimbursement.setReimbursementDate(reimbursement.getTime1());
        reimbursement.setReimbursementTime(reimbursement.getTime2());
        IDID = reimbursementService.getMaxById();
        if (IDID == null) {
            IDID = 1;
        } else {
            IDID = IDID + 1;
        }
        reimbursement.setID(IDID);
        reimbursement.setApprovalDate("  ——");
        reimbursement.setApprovalTime("  ——");
        reimbursement.setStart("1");
        reimbursementService.insertReimbursement(reimbursement);

        return result.ok("报销申请提交成功");
    }

    @GetMapping("/getusername")
    public Result<String> result3() {
        Result<String> result = new Result<>();
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = user.getUsername();
        String realname = cardReplacementJobService.finduser(username).getRealname();
        result.setResult(realname);
        return result;
    }

    @GetMapping("/getuserid")
    public Result<Reimbursement> result6() {
        Result<Reimbursement> result = new Result<>();
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        LeaveInfo leaveInfo = leaveInfoService.queryInfoByJnId(user.getUsername());
        Reimbursement reimbursement = new Reimbursement();
        reimbursement.setUsername(leaveInfo.getRealname());
        reimbursement.setDepartment(leaveInfo.getDpName());
        reimbursement.setPost(leaveInfo.getPost());
        reimbursement.setCard(user.getUsername());
        IDID = reimbursementService.getMaxById();
        if (IDID == null) {
            IDID = 1;
        } else {
            IDID = IDID + 1;
        }
        ;
        reimbursement.setID(IDID);
        result.setResult(reimbursement);
        return result;
    }

    @GetMapping("/settime")
    public Result<Void> result7(String reimbursementDate, String reimbursementTime, String approvalDate, String approvalTime) {
        Result<Void> result = new Result<Void>();
        if (ObjectUtil.isAllEmpty(reimbursementDate, reimbursementTime, approvalDate, approvalTime)) {
            return result;
        }
        reimbursementService.settime(reimbursementDate, reimbursementTime, approvalDate, approvalTime, IDID);
        //System.out.println(setEndDate + "+" + setEndTime);
        return Result.ok();
    }

    @GetMapping("/update")
    public Result<Reimbursement> result8(Reimbursement reimbursement) {
        Result<Reimbursement> result = new Result<Reimbursement>();
        if (ObjectUtil.isEmpty(reimbursement)) {
            return result;
        }
        reimbursementService.updateReimbursement(reimbursement);
        IDID = reimbursement.getID();
        System.out.println("接收数据");
        log.info(String.valueOf(reimbursement));
        return result.ok("修改成功");
    }

    @GetMapping("/delete")
    public Result<Reimbursement> result9(String ID) {
        Result<Reimbursement> result = new Result<Reimbursement>();
        if ("".equals(ID)) {
            return result;
        }
        reimbursementService.deleteReimbursement(ID);
        return result.success("删除成功");
    }

    @GetMapping("/approvalGet")
    public Result<List<Reimbursement>> approvalGet(@RequestParam(name = "Card", required = false) String Card,
                                                   @RequestParam(name = "ReimbursementType", required = false) String ReimbursementType) {
        log.info("Card: " + Card + ", ReimbursementType: " + ReimbursementType);
        Result<List<Reimbursement>> result = new Result<>();
        List<Reimbursement> list = reimbursementService.listCheck(Card, ReimbursementType);
        if (!ObjectUtil.isEmpty(list)) {
            result.setResult(list);
        }
        log.info(list.toString());
        return result;
    }

    @GetMapping("/approvalGetId")
    public Result<Reimbursement> approvalGetId(@RequestParam(name = "ID", required = true) Integer id) {
        if (id < 0) {
            throw new JeecgBootException("请求异常");
        }
        Result<Reimbursement> result = new Result<Reimbursement>();
        Reimbursement byId = reimbursementService.getOneById(id);
        if (ObjectUtil.isEmpty(byId)) {
            return result;
        }
        String card = byId.getCard();
        LeaveInfo leaveInfo = leaveInfoService.queryInfoByJnId(card);
        byId.setDepartment(leaveInfo.getDpName());
        byId.setPost(leaveInfo.getPost());
        result.setResult(byId);
        return result;
    }

    @GetMapping("/approval")
    public Result<?> approval(@RequestParam(name = "flag", required = true) boolean flag
            , @RequestParam(name = "ID", required = true) Integer id
            , @RequestParam(name = "comment", required = true) String comment) {
        if (ObjectUtil.isEmpty(id)) {
            throw new JeecgBootException("请求异常");
        }
        if (ObjectUtil.isEmpty(comment)) {
            throw new JeecgBootException("审批意见不能为空");
        }
        log.info("flag: " + flag);
        Reimbursement byId = reimbursementService.getOneById(id);
        if (flag) {
            byId.setStatus("3");
        } else {
            byId.setStatus("-1");
        }
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        byId.setComment(comment);
        byId.setApprovalDate(localDate.toString());
        byId.setApprovalTime(localTime.toString().substring(0, 8));
        byId.setID(id);
        reimbursementService.approvalReimbursement(byId);
        return new Result<>().success("审批完成");
    }

    @PostMapping("/uploadFile")
    public Result<?> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        Result<?> result = new Result<String>();
        if (ObjectUtil.isEmpty(file)) {
            return result.error500("file为空");
        }
        String bucketName = "dyz";
        try {
            String originalFilename = file.getOriginalFilename();
            String fileType = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String md5Hex = DigestUtils.md5Hex(file.getBytes());
//            String objectName = UUID.randomUUID().toString() + fileType;
            String objectName = md5Hex + fileType;
            minioUtil.getMinioClient();
            boolean fileExist = minioUtil.getBucketFileExist(objectName, bucketName);
            if (!fileExist) {
                Boolean uploadFile = minioUtil.uploadFile(file, bucketName, objectName);
                if (uploadFile) {
                    return result.success(objectName);
                }
            } else {
                return result.error500("文件已存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.error500("上传失败");
        }
        return result;
    }


}
