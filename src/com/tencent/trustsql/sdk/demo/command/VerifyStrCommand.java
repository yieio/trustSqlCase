package com.tencent.trustsql.sdk.demo.command;

import org.apache.commons.lang3.StringUtils;

import com.tencent.trustsql.sdk.ErrorNum;
import com.tencent.trustsql.sdk.TrustSDK;
import com.tencent.trustsql.sdk.exception.TrustSDKException;

public class VerifyStrCommand implements Command {

	@Override
	public String execute(String... args) throws Exception {
		if(args == null || args.length < 4 || StringUtils.isEmpty(args[1]) || StringUtils.isEmpty(args[2]) || StringUtils.isEmpty(args[3])) {
			throw new TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.getRetCode(), ErrorNum.INVALID_PARAM_ERROR.getRetMsg());
		}
		String pubKey = args[1];
		String srcStr = args[2];
		String sign = args[3];
		System.out.println(String.format("Verify a sign [%s] with pubkey key [%s] :", srcStr, pubKey));
		boolean isCorrect = TrustSDK.verifyString(pubKey, srcStr, sign);
		return Boolean.toString(isCorrect);
	}

}
