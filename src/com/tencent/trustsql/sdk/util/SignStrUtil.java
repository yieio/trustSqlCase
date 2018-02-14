package com.tencent.trustsql.sdk.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.beanutils.PropertyUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class SignStrUtil {

	public static String mapToKeyValueStr(Map<String, Object> map) throws Exception {
		StringBuffer content = new StringBuffer();
		boolean first = true;
		Set<String> keySet = map.keySet();
		Iterator<String> iter = keySet.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			if (map.get(key) == null) {
				continue;
			}
			Object v = map.get(key);
			if (map.get(key) instanceof String) {
				String s = (String) map.get(key);
				if (s.length() <= 0) {
					continue;
				}
			} else if (!isBaseDataType(map.get(key).getClass())) {
				if(map.get(key) instanceof JSONArray) {
					JSONArray js = JSONArray.parseArray(map.get(key).toString());
					String json = js.toString();
					v = json;
				} else {
					JSONObject js = JSONObject.parseObject(map.get(key).toString());
					String json = js.toString();
					v = json;
				}
			}

			if (!first) {
				content.append("&").append(key).append("=").append(v);
			} else {
				content.append(key).append("=").append(v);
			}
			first = false;
		}
		return content.toString();
	}

	public static String objToKeyValueStr(Object object) throws Exception {
		Map<String, Object> map = new TreeMap<String, Object>();
		java.beans.PropertyDescriptor pd[] = PropertyUtils.getPropertyDescriptors(object);
		for (int i = 0; i < pd.length; i++) {
			if (pd[i].getReadMethod() != null && pd[i].getWriteMethod() != null) {
				Object value = PropertyUtils.getProperty(object, pd[i].getName());
				if (value != null) {
					map.put(pd[i].getName(), value);
				}

			}
		}
		return mapToKeyValueStr(map);
	}
	
	public static Map<String, Object> jsonToMap(JSONObject obj) throws Exception {
		Map<String, Object> map = new TreeMap<String, Object>();
	    Iterator<String> keysItr = obj.keySet().iterator();
	    while(keysItr.hasNext()) {
	        String key = keysItr.next();
	        Object value = obj.get(key);
	        if(value instanceof JSONArray) {
	            value = jsonArrayToKeyPairStr((JSONArray) value);
	        } else if(value instanceof JSONObject) {
	            value = mapToKeyValueStr(jsonToMap((JSONObject) value));
	        }
	        map.put(key, value);
	    }
	    return map;
	}
	
	public static String jsonArrayToKeyPairStr(JSONArray array) throws Exception {
		List<Object> list = new ArrayList<Object>();
	    for(int i = 0; i < array.size(); i++) {
	        Object value = array.get(i);
	        if(value instanceof JSONArray) {
	            value = jsonArrayToKeyPairStr((JSONArray) value);
	        } else if(value instanceof JSONObject) {
	            value = mapToKeyValueStr(jsonToMap((JSONObject)value));
	        }
	        list.add(value);
	    }
	    StringBuffer sb = new StringBuffer();
	    for(int i = 0; i < list.size() - 1; i++) {
	    	sb.append(list.get(i).toString()).append(",");
	    }
	    sb.append(list.get(list.size() - 1));
	    return sb.toString();
	}

	/**
	 * 判断一个类是否为基本数据类型。
	 * 
	 * @param clazz
	 *            要判断的类。
	 * @return true 表示为基本数据类型。
	 */
	private static boolean isBaseDataType(Class clazz) throws Exception {
		return (clazz.equals(String.class) || clazz.equals(Integer.class) || clazz.equals(Byte.class)
				|| clazz.equals(Long.class) || clazz.equals(Double.class) || clazz.equals(Float.class)
				|| clazz.equals(Character.class) || clazz.equals(Short.class) || clazz.equals(BigDecimal.class)
				|| clazz.equals(BigInteger.class) || clazz.equals(Boolean.class) || clazz.equals(Date.class) || clazz
					.isPrimitive());
	}

}
