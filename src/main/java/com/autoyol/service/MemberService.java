package com.autoyol.service;

import com.autoyol.entity.PathIP;
import com.autoyol.entity.Result;

public interface MemberService {
	public Result updateMemberInfo(String mobile);
	public Result createMember(String mobile, PathIP pathIP);
	public Result getCommUseDrivers (String mobile, String memNo);
}
