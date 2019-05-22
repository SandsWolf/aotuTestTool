package com.autoyol.service.impl;

import com.autoyol.entity.Address;
import com.autoyol.entity.CtripUser;
import com.autoyol.entity.Result;
import com.autoyol.service.CtripTransService;
import com.autoyol.util.ApiUtils;
import com.autoyol.util.HttpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.autoyol.util.JsonFormatUtil.formatJson;

@Service
public class CreateCtripTransImpl implements CtripTransService{


    //设置用车时间
//    String pickupDate = "2019-05-15 14:00";
//    String returnDate = "2019-05-17 14:00";




    public Result createTrans(String pickupDate, String returnDate, String uri) {

        Result result = new Result();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();

        //设置取还车城市及门店
        String setPickupCityCode = "310100";
        String setReturnCityCode = "310100";
        String setPickupStoreCode = "310100";
        String setReturnStoreCode = "310100";

        map.put("orderId", "32112827206112");
        map.put("vehicleCode", "499");
        map.put("pickupDate", pickupDate);
        map.put("returnDate", returnDate); //还车时间
        map.put("pickupStoreCode", setPickupStoreCode);
        map.put("returnStoreCode", setReturnStoreCode);
        map.put("pickupCityCode", setPickupCityCode);
        map.put("returnCityCode", setReturnCityCode);
        map.put("couponCode", "");

        map.put("freeDepositDegree", "10");




        map.put("isPickupOndoor", true);
        Address pickupOndoorAddrDTO = new Address();
        pickupOndoorAddrDTO.setAddress("上海市徐汇区广元西路303号");
        //121.431405,31.196009
        pickupOndoorAddrDTO.setLongitude(121.431405);
        pickupOndoorAddrDTO.setLatitude(31.196009);
        map.put("pickupOndoorAddr", pickupOndoorAddrDTO);

        map.put("isPickoffOndoor", true);

        Address pickoffOndoorAddrDTO = new Address();
        pickoffOndoorAddrDTO.setAddress("上海市徐汇区广元西路303号");
        pickoffOndoorAddrDTO.setLongitude(121.431405);
        pickoffOndoorAddrDTO.setLatitude(31.196009);
        map.put("pickoffOndoorAddr", pickoffOndoorAddrDTO);


        map.put("addedServices", new String[]{"abatementAmt"});
        map.put("payAmount", "411");
        map.put("priceType", "2");
        map.put("payMode", "2");
        CtripUser userInfo = new CtripUser();
        userInfo.setIdNo("622726199105222890");
        userInfo.setIdType("1");
        userInfo.setMobile("15601797971");
        userInfo.setName("马奇");

        map.put("userInfo", userInfo);



        StringBuffer sbff = new StringBuffer();
        TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
        treeMap.putAll(map);
        for(Map.Entry<String, Object> m: treeMap.entrySet()){
            if (m.getValue() instanceof String) {
                sbff.append(m.getKey()).append(m.getValue());
            }
        }
        System.out.println("============="+sbff);
        String key = sbff.toString().toUpperCase() + "qGOSWGrd7wAXZhZHhP6fgR4kOUwr8Drz8crxqct1OgkBM1S7LeBPC0ukCX6v8S/dFpuJF7hpH73+cO3/9uiHbg==";

        try {

            String md5str = ApiUtils.md5Encode(key);
            map.put("sign", md5str);

            String jsonStr = mapper.writeValueAsString(map);
            System.out.println("jsonStr="+jsonStr);

            String str = HttpRequest.sendPostTestServer(uri+"/api/createOrder",jsonStr,"Autoyol-Ctrip");

            System.out.println(str);

            str = formatJson(str);
            result.setStatus(0);
            result.setMsg("success");
            result.setData(str);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
