package test;

import com.autoyol.dao.CarMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class TestLog {
//    Log log = LogFactory.getLog(Test1.class);
    private static final Logger logger = LoggerFactory.getLogger(TestLog.class);

    @Test
    public void testInteger() {
        String msg = "调试完毕";
        Integer a = 123;
        Integer b = 123;
        String str = "123";
        int num = 123;

        float f = 2.5f;

        logger.debug("a == b：{}",a == b);
        logger.debug("a.intValue() == Integer.parseInt(str)：{}。{}",a.intValue() == Integer.parseInt(str),msg);
        logger.debug("a.intValue() == num：{}",a.intValue() == num);

        Random r = new Random();
        System.out.println(r.nextInt());

        String environment = "1";
        String carNo = "2";
        String rentTime = "3";
        String revertTime = "4";
        String srvFlag = "5";
        String[] strs = {environment,carNo,rentTime,revertTime,srvFlag};
        String[] strss = new String[]{environment,carNo,rentTime,revertTime,srvFlag};
        logger.info("=======================");
        logger.info("------>开始校验时间轴：environment=" + environment + ",carNo=" + carNo + ",rentTime=" + rentTime + ",revertTime=" + revertTime + ",srvFlag=" + srvFlag);
        logger.info("------>开始校验时间轴：environment={},carNo={},rentTime={},revertTime={},srvFlag={}",strs);
        logger.info("------>开始校验时间轴：environment={},carNo={},rentTime={},revertTime={},srvFlag={}",new String[]{environment,carNo,rentTime,revertTime,srvFlag});

        try {
            logger.debug("debug");
            logger.info("保存： 开始进入保存方法");
            logger.info("保存： 开始进入保存方法");

            int i = 1/0;

            logger.info("保存： 执行保存结束，成功");
        } catch (Exception e) {
            logger.error("执行App类Save()方法出现异常！");  // 异常
            logger.error("计算异常：",e);
//            e.printStackTrace();
        }
    }

    /*
     * 思考： 日志的输出级别作用？
     * 	 ----> 控制日志输出的内容。
     */
    @Test
    public void testLog() throws Exception {
        // 输出不同级别的提示
        logger.debug("调试信息");
        logger.info("信息提示");
        logger.warn("警告");
        logger.error("异常");

    }


    public static void main(String[] args) {
//        System.out.println(Math.ceil(1.01));
//        System.out.println(Math.ceil(-1.01));
//        System.out.println(Math.ceil(1.5));
//        System.out.println(Math.ceil(-1.5));
//
//        System.out.println(Math.floor(1.01));
//        System.out.println(Math.floor(-1.01));
//
//        System.out.println(Math.round(10.2731));



    }
}
