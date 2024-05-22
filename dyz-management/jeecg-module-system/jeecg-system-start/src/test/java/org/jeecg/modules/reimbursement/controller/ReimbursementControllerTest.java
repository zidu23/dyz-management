package org.jeecg.modules.reimbursement.controller;

import org.jeecg.JeecgSystemApplication;
import org.jeecg.modules.reimbursement.entity.Reimbursement;
import org.jeecg.modules.reimbursement.service.ReimbursementService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = JeecgSystemApplication.class)
public class ReimbursementControllerTest {

    @Resource
    private ReimbursementService reimbursementService;

    @Test
    public void testGetUser(){
        List<Reimbursement> reimbursementList = reimbursementService.findcardReimbursement("王玉玺");
        System.out.println(reimbursementList);

    }
}