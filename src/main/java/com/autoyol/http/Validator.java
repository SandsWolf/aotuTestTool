package com.autoyol.http;

import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.util.*;


public class Validator {
	
	public static final org.slf4j.Logger logger = LoggerFactory.getLogger(Validator.class);
	
	
	/**
	 * 判断是否为ArrayList
	 * @param paramsMap
	 */
	public void isList(Map<String,Object> paramsMap){
		Object resultData = paramsMap.get("resultData");
		logger.info("\n");
		logger.info("=========验证Json字符串中的data是否是List集合=========");
		logger.info("data:>>>{}",resultData.toString());
		Assert.assertTrue(resultData instanceof ArrayList,"  =========该结果不是一个List集合。");
		logger.info(" =========验证成功！data是List集合======\n");
	}
	
	/**
	 * 判断是否为HashMap
	 * @param paramsMap
	 */
	public void isMap(Map<String,Object> paramsMap){
		Object resultData = paramsMap.get("resultData");
		logger.info("\n");
		logger.info("=========验证Json字符串中的data是否是Map集合=========");
		logger.info("data:>>>{}",resultData.toString());
		Assert.assertTrue(resultData instanceof HashMap,"  =========该结果不是一个Map集合。");
		logger.info(" =========验证成功！data是Map集合======\n");
	}
	
	/**
	 * 验证resultData 的size 适用于ArrayList HashMap
	 * @param paramsMap
	 */
	@SuppressWarnings("rawtypes")
	public void validateSize(Map<String,Object> paramsMap){
		Object resultObj = paramsMap.get("resultData");
		
		Assert.assertNotNull(paramsMap.get("propert"),"propertName不能为空");
		String propert = (String)paramsMap.get("propert");
		int expected = (Integer)paramsMap.get("size");
		
		logger.info("=========验证Json字符串中{}的大小为：{}=========",propert,expected);
		List<Object> dataList = new ArrayList<Object>();
		this.forResultByProperty(resultObj, propert, dataList);
		Assert.assertTrue(dataList.size()>0,"结果中没有"+propert+"字段。");
		boolean result = false;
		for(Object object : dataList){
			int size =0;
			if(object instanceof ArrayList)
				size = ((ArrayList)object).size();
			else if(object instanceof HashMap)
				size = ((HashMap)object).size();
			//logger.info("========={}的大小为{}，\t值为:{}",propert,size,object);
			result = size ==expected;
			if(result){
				Assert.assertEquals(size, expected);
				logger.info("=========验证成功！=========");
				break;
			}
		}
		if(!result){
			Assert.fail(propert+"的size不为："+expected);
		}
	}
	
	public void valiatePropertValue(Map<String,Object> paramsMap){
		Object resultObj = paramsMap.get("resultData");
		
		Assert.assertNotNull(paramsMap.get("propert"),"propertName不能为空");
		String propert = String.valueOf(paramsMap.get("propert"));
		
		Object expectObj = paramsMap.get("expectObj");

		logger.info("=========验证json字符串中{}的值为：{}=========",propert,expectObj);
		//1、从resultData中找到属性名为propert的字段
		//2、其次判断propert字段的类型是否是集合，是则进入集合比较判断
		//3、判断propert字段的值和expectObj是否相等
		List<Object> dataList = new ArrayList<Object>();
		this.forResultByProperty(resultObj, propert, dataList);
		Assert.assertTrue(dataList.size()>0,"结果中没有"+propert+"字段。");
		boolean result = false;
		for(Object object : dataList){
			logger.info("========={}的值为:{}",propert,object);
			result = expectObj.equals(object);
			if(result){
				Assert.assertEquals(object, expectObj);
				logger.info("=========验证成功！=========");
				break;
			}
		}
		if(!result){
			Assert.fail("在返回结果中没有找到字段为"+propert+"值为："+expectObj+"的数据");
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void valiateCollectionsIndexOfValue(Map<String,Object> paramsMap){
		
		Object resultData = paramsMap.get("resultData");
		Assert.assertNotNull(paramsMap.get("propert"));
		String propert = String.valueOf(paramsMap.get("propert"));
		int index = (Integer)paramsMap.get("index")-1;
		Object expectObj = paramsMap.get("expectObj");
		
		List<Object> dataList = new ArrayList<Object>();
		this.forResultByProperty(resultData, propert, dataList);
		
		for(Object object:dataList){
			Object result = null;
			
			if(object instanceof ArrayList){
				ArrayList resultList = (ArrayList)object;
				result = resultList.get(index);
				Assert.assertNotNull(result);
			}else if (object instanceof HashMap){
				HashMap resultMap = (HashMap)object;
				Object key = resultMap.keySet().toArray()[index];
				result = resultMap.get(key);
				Assert.assertNotNull(result);
			}
			if(result instanceof HashMap)
				this.valiateMap((HashMap)result, (HashMap)expectObj);
			else if(result instanceof ArrayList)
				this.valiateList((ArrayList)result, (ArrayList)expectObj);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void valiateResult(Map<String,Object> paramsMap){
		Object resultData = paramsMap.get("resultData");
		Object expectObj = paramsMap.get("expectObj");
		if(expectObj instanceof HashMap){
			HashMap resultMap = (HashMap)resultData;
			HashMap expectMap = (HashMap)expectObj;
			this.valiateMap(resultMap, expectMap);
		}else if(expectObj instanceof ArrayList){
			ArrayList resultList = (ArrayList)resultData;
			ArrayList expectList = (ArrayList)expectObj;
			this.valiateList(resultList, expectList);
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	private void valiateList(ArrayList resultList, ArrayList expectList) {
		
		Assert.assertNotNull(resultList);
		Assert.assertEquals(resultList.size(), expectList.size());
		for(int index =0 ;index < expectList.size();index ++){
			Object expectObj = expectList.get(index);
			Object resultObj = resultList.get(index);
			if(expectObj instanceof ArrayList){
				ArrayList resultList1 = (ArrayList)resultObj;
				ArrayList expectList1 = (ArrayList)expectObj;
				this.valiateList(resultList1, expectList1);
			}else if(expectObj instanceof HashMap){
				HashMap resultMap = (HashMap)resultObj;
				HashMap expectMap = (HashMap)expectObj;
				this.valiateMap(resultMap, expectMap);
			}else{
				Assert.assertEquals(resultObj, expectObj);
			}
		}
	}

	/**
     * 验证返回的 json 对象是否满足期望
     * @param resultMap
     * @param expectMap
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void valiateMap(HashMap resultMap, HashMap expectMap){
        Set<String> keySet = expectMap.keySet();
        for(String key:keySet){
            Object object = resultMap.get(key);
            Assert.assertNotNull(object);
            if(object instanceof HashMap){
                Object expectResult = expectMap.get(key);
                Assert.assertTrue(expectResult instanceof Map);
                valiateMap((HashMap)object,(HashMap)expectMap);
            }else if(object instanceof ArrayList){
            	ArrayList resultList = (ArrayList)object;
            	ArrayList expectList = (ArrayList)expectMap.get(key);
            	this.valiateList(resultList, expectList);
            }else{
                Assert.assertEquals(object,expectMap.get(key));
            }
        }
    }
	
	
    /**
     * 根据属性名递归查找结果
     * @param resultObj
     * @param propert
     * @param dataList 
     * @return
     */
    @SuppressWarnings("rawtypes")
	private void forResultByProperty(Object resultObj, String propert, List<Object> dataList){
    	if(resultObj instanceof HashMap){
    		HashMap resultMap = (HashMap)resultObj;
    		if(resultMap.get(propert) !=null){
    			dataList.add(resultMap.get(propert));
    		}
			for(Object object : resultMap.keySet()){
				this.forResultByProperty(resultMap.get(object), propert, dataList);
			}
    	}else if(resultObj instanceof ArrayList){
    		ArrayList resultList = (ArrayList)resultObj;
    		for(Object object : resultList){
    			this.forResultByProperty(object, propert, dataList);
    		}
    	}
    }
    
    /**
     * 
     * @param paramsMap
     */
    public void valiatedJson(Map<String,Object> paramsMap){
    	logger.info("\n");
    	logger.info("=========验证JSON字符串是否相等======");
		Object resultData = paramsMap.get("result");
		logger.info("resultJson:>>>"+resultData);
		Object expectJson = paramsMap.get("json");
		logger.info("expectJson:>>>"+expectJson);
		Assert.assertEquals(String.valueOf(resultData), String.valueOf(expectJson),"验证不通过，两个JSON字符串不相等。\n");
		logger.info("=========验证成功！JSON字符串相等======\n");
	}
}