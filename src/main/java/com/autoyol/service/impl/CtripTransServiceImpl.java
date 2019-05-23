package com.autoyol.service.impl;

import com.autoyol.entity.Address;
import com.autoyol.entity.CtripStore;
import com.autoyol.entity.CtripUser;
import com.autoyol.entity.Result;
import com.autoyol.service.CtripTransService;
import com.autoyol.util.ApiUtils;
import com.autoyol.util.HttpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.autoyol.util.JsonFormatUtil.formatJson;

@Service
public class CtripTransServiceImpl implements CtripTransService{

    @Override
    public Result createTrans(String pickupDate, String returnDate, String uri, String cityCode) {

        Result result = new Result();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();


        map.put("orderId", "32112827206112");
        map.put("vehicleCode", "499");
        map.put("pickupDate", pickupDate.substring(0,16));
        map.put("returnDate", returnDate.substring(0,16)); //还车时间
        map.put("pickupStoreCode", cityCode);
        map.put("returnStoreCode", cityCode);
        map.put("pickupCityCode", cityCode);
        map.put("returnCityCode", cityCode);
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

    @Override
    public Result selectCtripInventory(String pickupDate, String returnDate, String uri, String cityCode) {


        Result result = new Result();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        map.put("pickupDate", pickupDate.substring(0,16));
        map.put("returnDate", returnDate.substring(0,16));  //还车时间
        map.put("isPickupOndoor", true);   //取车开关
        map.put("isPickoffOndoor", true);  //还车开关


        CtripStore store = new CtripStore();
        store.setPickupCityCode(cityCode);   //取车城市code
        store.setReturnCityCode(cityCode);   //还车城市code
        store.setPickupStoreCode(cityCode);
        store.setReturnStoreCode(cityCode);

        List<CtripStore> list = new ArrayList<>();
        list.add(store);
        map.put("storeList", list);


        StringBuffer sbff = new StringBuffer();
        TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
        treeMap.putAll(map);
        for (Map.Entry<String, Object> m : treeMap.entrySet()) {
            if (m.getValue() instanceof String) {
                sbff.append(m.getKey()).append(m.getValue());
            }
        }
        String key = sbff.toString().toUpperCase() + "qGOSWGrd7wAXZhZHhP6fgR4kOUwr8Drz8crxqct1OgkBM1S7LeBPC0ukCX6v8S/dFpuJF7hpH73+cO3/9uiHbg==";
        System.out.println(key);
        try {


            String md5str = ApiUtils.md5Encode(key);
            map.put("sign", md5str);
            System.out.println("map：   " + map);
            String jsonStr = mapper.writeValueAsString(map);
            System.out.println("jsonStr=" + jsonStr);
            long start = System.currentTimeMillis();


            String str = HttpRequest.sendPostTestServer(uri + "/api/searchVehicle", jsonStr, "Autoyol-Ctrip");


            System.out.print("=========================================" + (System.currentTimeMillis() - start));

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
