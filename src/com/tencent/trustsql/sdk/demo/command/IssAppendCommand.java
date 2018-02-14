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

public class IssAppendCommand implements Command {

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
			paramMap.put("info_key", jsonObj.getString("info_key"));
			paramMap.put("info_version", jsonObj.getString("info_version"));
			paramMap.put("state", jsonObj.getString("state"));
			paramMap.put("content", jsonObj.getJSONObject("content"));
			paramMap.put("notes", jsonObj.getJSONObject("notes"));
			paramMap.put("commit_time", jsonObj.getString("commit_time"));
			paramMap.put("account", TrustSDK.generateAddrByPrvkey(prvKey));
			paramMap.put("public_key", TrustSDK.generatePubkeyByPrvkey(prvKey));
			paramMap.put("sign", TrustSDK.issSign(jsonObj.getString("info_key"), jsonObj.getString("info_version"), jsonObj.getIntValue("state"), jsonObj.getString("content"), jsonObj.getString("notes"), jsonObj.getString("commit_time"), prvKey));
			paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes()));
			//generate post data
			JSONObject postJson = new JSONObject();
			for(String key : paramMap.keySet()) {
				postJson.put(key, paramMap.get(key));
			}
			String url = "https://baas.trustsql.qq.com/cgi-bin/v1.0/trustsql_iss_append.cgi";
			System.out.println("Append information into TrustSQL:");
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
			}
			resultJsonArr.add(i, resultJson);
		}
		return resultJsonArr.toJSONString();
	}

}
