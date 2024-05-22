package org.jeecg.modules.reimbursement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.reimbursement.entity.Reimbursement;

import java.util.List;


public interface ReimbursementService extends IService<Reimbursement> {

    List<Reimbursement> findcardReimbursement(String realname);

    Integer getMaxById();


    void insertReimbursement(Reimbursement reimbursement);

    Reimbursement finduser(String username);

    void settime(String reimbursementDate, String reimbursementTime, String approvalDate, String approvalTime,Integer ID);

    void deleteReimbursement(String id);

    Reimbursement updateReimbursement(Reimbursement reimbursement);

    List<Reimbursement> listCheck(String card, String type);

    Reimbursement getOneById(Integer id);

    void approvalReimbursement(Reimbursement reimbursement);
}
