package com.tencent.trustsql.sdk.demo.command;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.trustsql.sdk.Constants;
import com.tencent.trustsql.sdk.ErrorNum;
import com.tencent.trustsql.sdk.TrustSDK;
import com.tencent.trustsql.sdk.exception.TrustSDKException;
import com.tencent.trustsql.sdk.util.HttpClientUtil;
import com.tencent.trustsql.sdk.util.SignStrUtil;

public class IssQueryCommand implements Command {

	@Override
	public String execute(String... args) throws Exception {
		if(args == null || args.length < 4 || StringUtils.isEmpty(args[1]) || StringUtils.isEmpty(args[2]) || StringUtils.isEmpty(args[3])) {
			throw new TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.getRetCode(), ErrorNum.INVALID_PARAM_ERROR.getRetMsg());
		}
		String mchId = args[1];
		String prvKey = args[2];
		JSONArray jsonArr = JSONArray.parseArray(args[3]);
		JSONArray resultJsonArr = new JSONArray();
		for(int i = 0; i < jsonArr.size(); i++) {
			JSONObject jsonObj = jsonArr.getJSONObject(i);
			Map<String, Object> paramMap = new TreeMap<String, Object>();
			paramMap.put("mch_id", mchId);
			paramMap.put("sign_type", "ECDSA");
			paramMap.put("timestamp", System.currentTimeMillis() / 1000);
			for(Entry<String, Object> entry : jsonObj.entrySet()) {
				paramMap.put(entry.getKey(), entry.getValue());
			}
			//System.out.println("========ori:" + SignStrUtil.mapToKeyValueStr(paramMap));
			paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes()));
			//System.out.println("========sign:" + paramMap.get("mch_sign"));
			JSONObject postJson = new JSONObject();
			for(String key : paramMap.keySet()) {
				postJson.put(key, paramMap.get(key));
			}
			System.out.println("Search information from TrustSQL: ");
			String url = "https://baas.trustsql.qq.com/cgi-bin/v1.0/trustsql_iss_query.cgi";
			String result = HttpClientUtil.sendHttpPost(url, postJson.toJSONString());
			//分析http请求结果
			JSONObject resultJson = JSONObject.parseObject(result);
			if("0".equals(resultJson.getString("retcode")) && "OK".equals(resultJson.getString("retmsg"))) {
				//验证返回数据的mch_sign
				paramMap.clear();
				for(Entry<String, Object> entry : resultJson.entrySet()) {
					if(!"mch_sign".equals(entry.getKey())) {
						paramMap.put(entry.getKey(), entry.getValue());
					}
				}
				boolean mchSignValid = TrustSDK.verifyString(Constants.INFO_SHARE_PUBKEY, SignStrUtil.mapToKeyValueStr(paramMap), resultJson.getString("mch_sign"));
				resultJson.put("mch_sign_verify", mchSignValid);
				if(mchSignValid) {
					// 验证返回的每个共享信息的sign
					JSONArray resultJsonInfos = resultJson.getJSONArray("infos");
					for(int j = 0; j < resultJsonInfos.size(); j++) {
						JSONObject info = resultJsonInfos.getJSONObject(i);
						boolean infoSignValid = TrustSDK.issVerifySign(info.getString("info_key"), info.getString("info_version"),
														info.getIntValue("state"), info.getString("content"), info.getString("notes"), 
														info.getString("commit_time"), info.getString("public_key"), info.getString("sign"));
						info.put("sign_verify", infoSignValid);
					}
				}
			}
			resultJsonArr.add(i, resultJson);
		}
		return resultJsonArr.toJSONString();
	}

}
