package com.autoyol.service.impl;

import com.autoyol.entity.Result;
import com.autoyol.http.HttpResponse;
import com.autoyol.http.HttpUtils;
import com.autoyol.service.MapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MapServiceImpl implements MapService {
    private static final Logger logger = LoggerFactory.getLogger(MapServiceImpl.class);

    /**
     * 通过高德地图API搜到地址列表
     * @param place
     * @return
     */
    public Result searchPlace(String place) {
        Result result = new Result();
        /*
        key：道德账号key
        keywords：搜索关键字
        offset：结果列表长度
         */
        String gdURL = "https://restapi.amap.com/v3/place/text?key=e730c6e23d78d01c1654c2bc327667d6&keywords=searchPlace&types=&city=&children=1&offset=30&page=1&extensions=all";
        gdURL = gdURL.replace("searchPlace",place);

        Map<String,String> headerMap = new HashMap<String, String>();
        HttpResponse httpResult = HttpUtils.get(headerMap,gdURL);

        if (httpResult.getHttpRespCode() == 200) {
            Map<String,Object> responseBodyObject = httpResult.getResponseBodyObject();
//            logger.info(JSON.toJSON(responseBodyObject));

            if ("1".equals(responseBodyObject.get("status"))) {
                Map<String,Object> suggestionMap = (Map<String,Object>)responseBodyObject.get("suggestion");    //城市信息所在对象list

                List<Object> poisList = (List<Object>)responseBodyObject.get("pois");   //具体地址List：输入有效地址时才有数据

                if (poisList.size() == 0) {
                    logger.info("没有地址信息，有城市信息");
                    List<Object> citiesList = (List<Object>)suggestionMap.get("cities");    //城市信息list

                    result.setStatus(1);
                    result.setMsg("success");
                    result.setData("请输入详细地址信息");
                } else {    //有效数据
                    logger.info("有地址信息");

                    Map<String,String> resultMap = new HashMap<String,String>();
                    for (int i = 0; i < poisList.size(); i++) {
                        Map<String,Object> poisMap = (Map<String,Object>)poisList.get(i);

                        resultMap.put((String) poisMap.get("name"),(String)poisMap.get("location"));
                        List<Object> childrenList = (List<Object>)poisMap.get("children");
                        if (childrenList.size() > 0) {      //待处理子地址

//                            logger.info((String) poisMap.get("name") + "—childrenList：" + JSON.toJSONString(childrenList));
                        }
                    }

                    result.setStatus(0);
                    result.setMsg("success");
                    result.setData(resultMap);
                }
            }
            if ("0".equals(responseBodyObject.get("status"))) {
                result.setStatus(1);
                result.setMsg("success");
                result.setData("高德地图接口请求失败");
            }
        } else {
            result.setStatus(1);
            result.setMsg("success");
            result.setData("调用接口失败");
        }

        return result;
    }
}