package com.tencent.trustsql.sdk.demo.command;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.tencent.trustsql.sdk.ErrorNum;
import com.tencent.trustsql.sdk.TrustSDK;
import com.tencent.trustsql.sdk.exception.TrustSDKException;

public class GeneratePubKeyByPrvKeyCommand implements Command {

	@Override
	public String execute(String... args) throws Exception {
		if(args == null || args.length < 2 || StringUtils.isEmpty(args[1])) {
			throw new TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.getRetCode(), ErrorNum.INVALID_PARAM_ERROR.getRetMsg());
		}
		JSONArray jsonArr = JSONArray.parseArray(args[1]);
		String prvKey = jsonArr.getString(0);
		System.out.println(String.format("Convert the private key [%s] to public key:", prvKey));
		String pubKey = TrustSDK.generatePubkeyByPrvkey(prvKey);
		return pubKey;
	}

}
