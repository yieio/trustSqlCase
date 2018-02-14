package com.tencent.trustsql.sdk.demo.command;

import com.alibaba.fastjson.JSONObject;
import com.tencent.trustsql.sdk.TrustSDK;
import com.tencent.trustsql.sdk.bean.PairKey;

public class GeneratePairKeyCommand implements Command {

	@Override
	public String execute(String... args) throws Exception {
		System.out.println("Generate a pair of public key and private key:");
		PairKey pairKey = TrustSDK.generatePairKey();
		return JSONObject.toJSONString(pairKey);
	}

}
