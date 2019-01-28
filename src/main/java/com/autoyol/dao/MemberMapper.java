package com.autoyol.dao;

import com.autoyol.entity.CommUseDriver;
import com.autoyol.entity.Member;

import java.util.List;

public interface MemberMapper {
	public void updateMemberInfo(String mobile);
	public List<Member> selectMemberInfoByMobile(String mobile);
	public List<CommUseDriver> selectCommUseDrivers(String memNo);
}
