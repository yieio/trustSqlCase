package com.tencent.trustsql.sdk.demo.command;

import org.apache.commons.lang3.StringUtils;

import com.tencent.trustsql.sdk.ErrorNum;
import com.tencent.trustsql.sdk.TrustSDK;
import com.tencent.trustsql.sdk.exception.TrustSDKException;

public class SignStrCommand implements Command {

	@Override
	public String execute(String... args) throws Exception {
		if(args == null || args.length < 3 || StringUtils.isEmpty(args[1]) || StringUtils.isEmpty(args[2])) {
			throw new TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.getRetCode(), ErrorNum.INVALID_PARAM_ERROR.getRetMsg());
		}
		String prvKey = args[1];
		String srcStr = args[2];
		try {
			System.out.println(String.format("Sign a string [%s] with private key [%s]: ", srcStr, prvKey));
			String sign = TrustSDK.signString(prvKey, srcStr.getBytes("UTF-8"));
			return sign;
		} catch(Exception e) {
			throw new TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.getRetCode(), ErrorNum.INVALID_PARAM_ERROR.getRetMsg());
		}
	}

}
