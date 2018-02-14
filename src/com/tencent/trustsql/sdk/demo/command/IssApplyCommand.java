package com.tencent.trustsql.sdk.demo.command;

import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.trustsql.sdk.Constants;
import com.tencent.trustsql.sdk.ErrorNum;
import com.tencent.trustsql.sdk.TrustSDK;
import com.tencent.trustsql.sdk.exception.TrustSDKException;
import com.tencent.trustsql.sdk.util.HttpClientUtil;
import com.tencent.trustsql.sdk.util.SignStrUtil;

public class IssApplyCommand implements Command {

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
			paramMap.put("version", jsonObj.getString("version"));
			paramMap.put("sign_type", "ECDSA");
			paramMap.put("mch_id", mchId);
	        if(StringUtils.isNotEmpty(jsonObj.getString("node_id"))) {
	        	paramMap.put("node_id", jsonObj.getString("node_id"));
	        }
	        paramMap.put("chain_id", jsonObj.getString("chain_id"));
	        paramMap.put("ledger_id", jsonObj.getString("ledger_id"));
	        paramMap.put("channel_id", jsonObj.getString("channel_id"));
			paramMap.put("source_id", jsonObj.getString("source_id"));
			paramMap.put("owner_account", jsonObj.getString("owner_account")); 
			paramMap.put("asset_type", jsonObj.getInteger("asset_type"));
			paramMap.put("amount", jsonObj.getInteger("amount")); 
			paramMap.put("unit", jsonObj.getString("unit")); 
			paramMap.put("content", jsonObj.getJSONObject("content")); 
			paramMap.put("timestamp", System.currentTimeMillis() / 1000);
			
			paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8")));
			//generate post data
			JSONObject postJson = new JSONObject();
			for(String key : paramMap.keySet()) {
				postJson.put(key, paramMap.get(key));
			}
			String url = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_issue_apply.cgi";
			System.out.println("Apply information into TrustSQL:");
 
			String result = HttpClientUtil.sendHttpPost(url, postJson.toJSONString());
			System.out.println(postJson.toJSONString());
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
			}
			resultJsonArr.add(i, resultJson);
		}
		return resultJsonArr.toJSONString();
	}

}
