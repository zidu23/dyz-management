package org.jeecg.modules.reimbursement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.reimbursement.entity.Reimbursement;

import java.util.List;

public interface ReimbursementMapper extends BaseMapper<Reimbursement> {

    public List<Reimbursement> findcardReimbursement(String Username);

    Integer getMaxById();

    void insetReimbursement(Reimbursement reimbursement);

    void settime(String reimbursementDate, String reimbursementTime, String approvalDate, String approvalTime,Integer idid);

    void deleteReimbursement(String id);

    void updateReimbursement(String reimbursementType,String reimbursementDate,String reimbursementTime,
                             String approvalDate,String approvalTime,String reasons,Integer id);

    List<Reimbursement> listCheck(String card, String type);

    Reimbursement getOneById(Integer id);

    void approvalReimbursement(Integer id, String approvalDate, String approvalTime, String comment,String status);
}
