package test;

import com.alibaba.fastjson.JSON;
import com.autoyol.dao.V43Mapper;
import com.autoyol.entity.OrderInfo;
import com.autoyol.entity.PathIP;
import com.autoyol.entity.Result;
import com.autoyol.http.HttpResponse;
import com.autoyol.http.HttpUtils;
import com.autoyol.service.CostService;
import com.autoyol.util.SetDateSourceUtil;
import com.autoyol.util.ToolUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class TestStringUtils {
    @Resource
    private V43Mapper v43Mapper;
    @Resource
    private CostService costService;

    private static final Logger logger = LoggerFactory.getLogger(TestStringUtils.class);


    /**
     * 【自助找车】添加索引
     */
    @Test
    public void getCarNo () {
        String environment = "test_1";
        SetDateSourceUtil.setDataSourceName(environment);
        PathIP pathIP = ToolUtil.getIP(environment);

        String paraStr = "{\"OsVersion\":\"24\",\"requestId\":\"C40BCB8649861547034798573\",\"androidID\":\"6458a139085d34df\",\"token\":\"81ed4d947fb4464d8f560f35256b86e8\",\"pageSize\":10,\"mac\":\"C40BCB864986\",\"filterCondition\":{\"seq\":\"4\",\"rentTimeVo\":{\"endTime\":\"20190126210000\",\"startTime\":\"20190125090000\"},\"rentAddressVo\":{\"returnCarLon\":\"121.412698\",\"getCarLon\":\"121.412698\",\"returnCarFlag\":\"0\",\"returnCarLat\":\"31.173704\",\"getCarAddress\":\"上海市徐汇区宜山路710-23号\",\"returnCarAddress\":\"上海市徐汇区宜山路710-23号\",\"getCarLat\":\"31.173704\",\"getCarFlag\":\"0\"},\"carPriceVo\":{\"minPrice\":\"0\"}},\"OS\":\"ANDROID\",\"mem_no\":\"383688774\",\"AppChannelId\":\"testmarket\",\"appName\":\"atzucheApp\",\"publicCityCode\":\"021\",\"cityCode\":\"310100\",\"IMEI\":\"864454031102472\",\"sceneCode\":\"U003\",\"AndroidId\":\"6458a139085d34df\",\"publicToken\":\"81ed4d947fb4464d8f560f35256b86e8\",\"pageNum\":1,\"AppVersion\":90,\"PublicLongitude\":\"121.412695\",\"deviceName\":\"MI5s\",\"PublicLatitude\":\"31.173704\"}";
        int type = 1;       // 1：自助找车  2：搜车
        int flag = 1;       //索引服务开关


        String carNos = "";
        if (type == 1) {
            Map<String,Integer> costMap = new HashMap<String,Integer>();
            String uri = "v46/car/list";

            Map<String,String> headerMap = new HashMap<String, String>();
            headerMap.put("Accept", "application/json;version=3.0;compress=false");
            headerMap.put("Content-Type", "application/json");
            headerMap.put("User-Agent", "AutoyolEs_web");

            Map<String, Object> paraMap = new HashMap<String, Object>();
//        paraMap.put("requestId","336");
            paraMap = (Map)JSON.parse(paraStr);

            HttpResponse httpResult = HttpUtils.post(headerMap, (pathIP.getServerIP() + uri), paraMap, false, false);
            if (httpResult.getHttpRespCode() == 200) {
                Map<String,Object> responseBodyObject = httpResult.getResponseBodyObject();
                if ("000000".equals(responseBodyObject.get("resCode"))) {
                    Map<String,Object> dataMap = (Map<String,Object>)responseBodyObject.get("data");
                    List<Map> carList = (List<Map>)dataMap.get("carList");
//                System.out.println("carListSize:" + carList.size());
                    for (int i = 0; i < carList.size(); i++) {
//                    System.out.println("carNo:" + carList.get(i).get("carNo"));
                        carNos += carList.get(i).get("carNo") + ",";
                    }
                }
            }

            System.out.println("自助找车carNos");
        } else {
            Map<String,Integer> costMap = new HashMap<String,Integer>();
            String uri = "v50/search/car/list";

            Map<String,String> headerMap = new HashMap<String, String>();
            headerMap.put("Accept", "application/json;version=3.0;compress=false");
            headerMap.put("Content-Type", "application/json");
            headerMap.put("User-Agent", "AutoyolEs_web");

            Map<String, Object> paraMap = new HashMap<String, Object>();
//        paraMap.put("requestId","336");
            paraMap = (Map)JSON.parse(paraStr);

            HttpResponse httpResult = HttpUtils.post(headerMap, (pathIP.getServerIP() + uri), paraMap, false, false);
            if (httpResult.getHttpRespCode() == 200) {
                Map<String,Object> responseBodyObject = httpResult.getResponseBodyObject();
                if ("000000".equals(responseBodyObject.get("resCode"))) {
                    Map<String,Object> dataMap = (Map<String,Object>)responseBodyObject.get("data");
                    List<Map> carList = (List<Map>)dataMap.get("carList");
//                System.out.println("carListSize:" + carList.size());
                    for (int i = 0; i < carList.size(); i++) {
//                    System.out.println("carNo:" + carList.get(i).get("carNo"));
                        carNos += carList.get(i).get("carNo") + ",";
                    }
                }
            }

            System.out.println("搜车carNos");
        }

        carNos = carNos.substring(0,carNos.lastIndexOf(","));
        System.out.println("carNos:" + carNos);
        if (flag == 1) {
            this.postSearchindex(carNos);
        }
    }

    /**
     * 索引服务调用
     * @param carNos
     */
    public void postSearchindex (String carNos) {
        String searchindexURL = "http://10.0.3.234:6018/searchindex/car/" + carNos;


        Map<String,String> headerMap = new HashMap<String, String>();
        headerMap.put("Accept", "application/json;version=3.0;compress=false");
        headerMap.put("Content-Type", "application/json");
        headerMap.put("User-Agent", "AutoyolEs_web");
        headerMap.put("connection", "Keep-Alive");

        Map<String, Object> paraMap = new HashMap<String, Object>();

        System.out.println("searchindexURL:" + searchindexURL);
        HttpResponse httpResult = HttpUtils.post(headerMap, searchindexURL, paraMap, false, false);
        System.out.println("索引调用结果：" + JSON.toJSONString(httpResult));
    }

    @Test
    public void testStringUtils () {
        String environment = "test_1";
        String orderNo = "364896031181";

        SetDateSourceUtil.setDataSourceName(environment);
        PathIP pathIP = ToolUtil.getIP(environment);

        System.out.println(orderNo);
        System.out.println(JSON.toJSONString(pathIP));

        OrderInfo orderInfo = v43Mapper.selectOrderMsg(orderNo);     //计算费用信息对象
        System.out.println("orderInfo:" + JSON.toJSONString(orderInfo));


        Result result = costService.getMileageOilCost(pathIP,orderInfo);   //,Integer.parseInt(testTotalAmt),Integer.parseInt(testCodeFlag)
        if (result.getStatus() == 0) {
            if (orderInfo.getRenter_order_no() == null) {    //普通订单或代步车券
                logger.info("----订单（" + orderNo + "）是普通订单----");
            } else if (orderInfo.getRenter_order_no() != null) {    //是套餐订单
                logger.info("----订单（" + orderNo + "）是套餐订单----");
            }
        }

        if (result.getStatus() == 0) {
            if (StringUtils.isEmpty(orderInfo.getRenter_order_no())) {    //普通订单或代步车券
                logger.info("----订单（" + orderNo + "）是普通订单----");
            } else if (!StringUtils.isEmpty(orderInfo.getRenter_order_no())) {    //是套餐订单
                logger.info("----订单（" + orderNo + "）是套餐订单----");
            }
        }
    }

    public static void main(String[] args) {
        /**
         * 1. public static boolean isEmpty(String str)
         *    判断某字符串是否为空，为空的标准是 str==null 或 str.length()==0
         *    下面是 StringUtils 判断是否为空的示例：
         */
//        System.out.println("StringUtils.isEmpty(null):" + StringUtils.isEmpty(null));
//        System.out.println("StringUtils.isEmpty(\"\"):" + StringUtils.isEmpty(""));
//        System.out.println("StringUtils.isEmpty(\" \"):" + StringUtils.isEmpty(" "));       //注意在 StringUtils 中空格作非空处理
//        System.out.println("StringUtils.isEmpty(\"   \"):" + StringUtils.isEmpty("   "));
//        System.out.println("StringUtils.isEmpty(\"bob\"):" + StringUtils.isEmpty("bob"));
//        System.out.println("StringUtils.isEmpty(\" bob \"):" + StringUtils.isEmpty(" bob "));


        String str = "{\"OsVersion\":\"24\",\"requestId\":\"C40BCB8649861545791476130\",\"androidID\":\"6458a139085d34df\",\"token\":\"67c4ac0b0ff14ec88df6b1d8b74aea57\",\"pageSize\":10,\"mac\":\"C40BCB864986\",\"filterCondition\":{\"seq\":\"4\",\"rentTimeVo\":{\"endTime\":\"20181228144500\",\"startTime\":\"20181226144500\"},\"rentAddressVo\":{\"returnCarLon\":\"121.431824\",\"getCarLon\":\"121.431824\",\"returnCarFlag\":\"0\",\"returnCarLat\":\"31.182401\",\"getCarAddress\":\"凯华公寓\",\"returnCarAddress\":\"凯华公寓\",\"getCarLat\":\"31.182401\",\"getCarFlag\":\"0\"},\"carPriceVo\":{\"minPrice\":\"0\"}},\"OS\":\"ANDROID\",\"mem_no\":\"383688774\",\"AppChannelId\":\"testmarket\",\"appName\":\"atzucheApp\",\"publicCityCode\":\"021\",\"cityCode\":\"310100\",\"IMEI\":\"864454031102472\",\"sceneCode\":\"U003\",\"AndroidId\":\"6458a139085d34df\",\"publicToken\":\"67c4ac0b0ff14ec88df6b1d8b74aea57\",\"pageNum\":1,\"AppVersion\":90,\"PublicLongitude\":\"121.409662\",\"deviceName\":\"MI5s\",\"PublicLatitude\":\"31.17234\"}";
        Map maps = (Map)JSON.parse(str);
        System.out.println("这个是用JSON类来解析JSON字符串!!!");
        for (Object map : maps.entrySet()){
            System.out.println(((Map.Entry)map).getKey()+"     " + ((Map.Entry)map).getValue());
        }
        System.out.println("------------------");
        System.out.println(JSON.toJSONString(maps));

    }
}
