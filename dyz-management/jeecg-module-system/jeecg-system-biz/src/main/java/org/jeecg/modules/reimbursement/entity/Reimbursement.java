package org.jeecg.modules.reimbursement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_reimbursement")
public class Reimbursement {

    private static final long serialVersionUID = 1L;
    @TableField("ID")
    private java.lang.Integer ID;
    /**申请人*/
    @TableField("Username")
    private java.lang.String Username;
    /**卡号*/
    @TableField("Card")
    private java.lang.String Card;
    /**报销类型*/
    @TableField("ReplacementType")
    private java.lang.String ReimbursementType;
    /**报销日期*/
    @TableField("ReimbursementDate")
    private java.lang.String ReimbursementDate;
    /**报销时间*/
    @TableField("ReimbursementTime")
    private java.lang.String ReimbursementTime;
    /**审批日期*/
    @TableField("ApprovalDate")
    private java.lang.String ApprovalDate;
    /**审批时间*/
    @TableField("ApprovalTime")
    private java.lang.String ApprovalTime;
    /**报销事由*/
    @TableField("Reasons")
    private java.lang.String Reasons;
    /**报销事由*/
    @TableField("ApprovalReasons")
    private java.lang.String Comment;
    /**状态*/
    @TableField("Status")
    private java.lang.String Status;

    private java.lang.String time1;
    private java.lang.String time2;
    private java.lang.String Department;
    private java.lang.String Post;
    private java.lang.String Start;
    private java.lang.String End;

}
