package com.autoyol.controller;

import com.autoyol.entity.Result;
import com.autoyol.service.MapService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/map")
public class MapController {
    @Resource
    private MapService mapService;

    /**
     * 搜到地址 & 经纬度
     * @param address
     * @return
     */
    @RequestMapping("/searchAddress")
    @ResponseBody
    public Result searchAddress(String address) {
        Result result = null;
        result = mapService.searchPlace(address);

        if (result.getStatus() == 0) {
            Map<String,String> resultMap = (Map<String,String>)result.getData();
//            String option = "<br><select id=\"select_addressList\">";
            String option = "";
            Set<Map.Entry<String,String>> entrySet = resultMap.entrySet();
            for(Map.Entry<String,String> e : entrySet){
                option += "<option value=\"" + e.getValue() + "\">" + e.getKey() + "</option>";
            }
//            option += "</select>";
//            System.out.println(option);

            result.setStatus(0);
            result.setMsg("success");
            result.setData(option);
        } else if (result.getStatus() == 1) {
            result.setStatus(1);
            result.setMsg("success");
            result.setData("<br>" + result.getData());
        }

        return result;
    }
}
