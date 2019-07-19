package com.autoyol.service.impl;

import com.autoyol.dao.MemberMapper;
import com.autoyol.entity.Member;
import com.autoyol.entity.Result;
import com.autoyol.dao.CouponMapper;
import com.autoyol.service.InsertCouponService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class InsertCouponServiceImpl implements InsertCouponService {

    @Resource
    private MemberMapper memberMapper;
    @Resource
    private CouponMapper couponMapper;


    public Result insertCoupon (String mobile) {

        List<Member> members = memberMapper.selectMemberInfoByMobile(mobile);

        String memNo = members.get(0).getReg_no();

        String random = "A";
        for (int i=0;i<10; i++) {
            int j = (int) (Math.random()*100);
            random = random + j;
        }

        String couponCode = String.valueOf(random);
        couponMapper.insertCoupon(memNo,couponCode);

        return null;
    }

}
