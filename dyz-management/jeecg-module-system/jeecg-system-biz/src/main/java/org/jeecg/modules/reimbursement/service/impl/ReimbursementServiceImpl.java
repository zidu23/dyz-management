package org.jeecg.modules.reimbursement.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.reimbursement.entity.Reimbursement;
import org.jeecg.modules.reimbursement.mapper.ReimbursementMapper;
import org.jeecg.modules.reimbursement.service.ReimbursementService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ReimbursementServiceImpl extends ServiceImpl<ReimbursementMapper, Reimbursement> implements ReimbursementService {

    @Resource
    private ReimbursementMapper reimbursementMapper;

    @Override
    public List<Reimbursement> findcardReimbursement(String realname) {
        return reimbursementMapper.findcardReimbursement(realname);
    }

    @Override
    public Integer getMaxById() {
        return reimbursementMapper.getMaxById();
    }

    @Override
    public void insertReimbursement(Reimbursement reimbursement) {
        reimbursementMapper.insetReimbursement(reimbursement);
    }

    @Override
    public Reimbursement finduser(String username) {
        return null;
    }

    @Override
    public void settime(String reimbursementDate, String reimbursementTime, String approvalDate, String approvalTime, Integer idid) {
        reimbursementMapper.settime(reimbursementDate, reimbursementTime, approvalDate, approvalTime, idid);
    }

    @Override
    public void deleteReimbursement(String id) {
        reimbursementMapper.deleteById(id);
    }

    @Override
    public Reimbursement updateReimbursement(Reimbursement reimbursement) {
        String reimbursementType = reimbursement.getReimbursementType();
        String reimbursementDate = reimbursement.getReimbursementDate();
        String reimbursementTime = reimbursement.getReimbursementTime();
        String approvalDate = reimbursement.getApprovalDate();
        String approvalTime = reimbursement.getApprovalTime();
        String reasons = reimbursement.getReasons();
        Integer id = reimbursement.getID();
        System.out.println(reimbursement.toString());
        reimbursementMapper.updateReimbursement(reimbursementType, reimbursementDate,
                reimbursementTime, approvalDate, approvalTime, reasons, id);
        return null;
    }

    @Override
    public List<Reimbursement> listCheck(String card, String type) {
        return reimbursementMapper.listCheck(card, type);
    }

    @Override
    public Reimbursement getOneById(Integer id) {
        return reimbursementMapper.getOneById(id);
    }

    @Override
    public void approvalReimbursement(Reimbursement reimbursement) {
        Integer id = reimbursement.getID();
        String approvalDate = reimbursement.getApprovalDate();
        String approvalTime = reimbursement.getApprovalTime();
        String comment = reimbursement.getComment();
        String status = reimbursement.getStatus();
        reimbursementMapper.approvalReimbursement(id, approvalDate, approvalTime, comment, status);
    }
}
