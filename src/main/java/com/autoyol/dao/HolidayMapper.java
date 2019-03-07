package com.autoyol.dao;

import java.util.List;
import java.util.Map;

public interface HolidayMapper {
	public List<String> selectHolidayList ();
	public List<Map<String,Long>> selectHolidaySettingList ();
	public List<Map<String,Long>> selectSpringHolidaySettingList ();
}
