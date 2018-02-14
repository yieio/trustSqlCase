/**
 * Project Name:trustsql_sdk_demo
 * File Name:Application.java
 * Package Name:com.tencent.trustsql.sdk.demo
 * Date:Jul 26, 20175:17:04 PM
 * Copyright (c) 2017, Tencent All Rights Reserved.
 *
 */
package com.tencent.trustsql.sdk.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.tencent.trustsql.sdk.TrustSDK;
import com.tencent.trustsql.sdk.bean.PairKey;
import com.tencent.trustsql.sdk.demo.command.Command;
import com.tencent.trustsql.sdk.demo.command.CommandFactory;

public class Application {

	private static final Log logger = LogFactory.getLog(Application.class);

	public static void main(String[] args) throws Exception {
		if (args == null || args.length == 0) {
			//args = new String[] {"tSignString","sbOIIUCFhCj9LfPkT93ZXJGPoanqL1XsyDm62BkNxCM=","123456"};
			
			//DAM_common 秘钥 lN9byovLrQGi+EvlVv3eBi5hZqadXWnORzddhXwG4mE=
			String channelPrev = "lN9byovLrQGi+EvlVv3eBi5hZqadXWnORzddhXwG4mE=";
			String mchId = "gb2a674789baa797b";
			String mchPrevKey = "sbOIIUCFhCj9LfPkT93ZXJGPoanqL1XsyDm62BkNxCM=";
			//args = new String[] {"tGenerateAddrByPrvkey","[\"lN9byovLrQGi+EvlVv3eBi5hZqadXWnORzddhXwG4mE=\"]"};
			

			//共享信息查询
			//args = new String[] {"tIssQuery","gb2a674789baa797b","sbOIIUCFhCj9LfPkT93ZXJGPoanqL1XsyDm62BkNxCM=","[{\"node_id\":\"nd_tencent_test1\",\"chain_id\":\"ch_tencent_test\",\"ledger_id\":\"ld_tencent_iss\",\"version\":\"1.0\",\"info_key\":\"yieio20180210\",\"page_no\":\"1\",\"page_limit\":\"5\"}]"};
			//共享信息添加
			//args = new String[] {"tIssAppend","gb2a674789baa797b","sbOIIUCFhCj9LfPkT93ZXJGPoanqL1XsyDm62BkNxCM=","[{\"node_id\":\"nd_tencent_test1\",\"chain_id\":\"ch_tencent_test\",\"ledger_id\":\"ld_tencent_iss\",\"version\":\"1.0\",\"info_key\":\"yieio20180210\",\"info_version\":1,\"state\":0,\"content\":{\"content\":\"test\"},\"notes\":{\"notes\":\"test\"},\"commit_time\":\"2018-02-09 15:15:00\",\"private_key\":\"sbOIIUCFhCj9LfPkT93ZXJGPoanqL1XsyDm62BkNxCM=\"}]"};

			
			//create user
			//PairKey pairKey = TrustSDK.generatePairKey(); 
	        //System.out.println(JSONObject.toJSONString(pairKey));
	        //String public_key = pairKey.getPublicKey();  
	        //String reqData = "{\"user_id\":\"1235\",\"public_key\":\""+public_key+"\",\"user_fullName\":\"yieio\"}";  
			//args = new String[] {"tUserRegister","+mchId+","+mchPrevKey+","[{\"product_code\":\"YieioTest\",\"req_data\":"+reqData+"}]"};
			
			// create user account
			//String reqData = "{\"user_id\":\"1235\",\"public_key\":\"BKW0Vk0dEZeYhkhiWkXg7ryknCEs/7JmV2EP22zHNbkV0LlJlW7BHZj2R+0nu8fdTjQYp2OAjI9FZLlWZ5JAVzY=\"}"; 
			//args = new String[] {"tCreateUserAccount","+mchId+","+mchPrevKey+","[{\"product_code\":\"YieioTest\",\"req_data\":"+reqData+"}]"};
			
			//资产发行申请
			//args = new String[] {"tIssApply","+mchId+","+mchPrevKey+","[{\"version\":\"1.0\",\"sign_type\":\"ECDSA\",\"node_id\":\"nd_tencent_test1\",\"chain_id\":\"ch_tencent_test\",\"ledger_id\":\"ld_tencent_dam\",\"channel_id\":\"DAM_common\",\"source_id\":\"\",\"owner_account\":\"1Cs3KomKZKj2ctBPDqXfg2jSExreeUwGYM\",\"asset_type\":5,\"amount\":100,\"unit\":\"ë\",\"content\": {\"content\": \"yieio test message\"},\"timestamp\":1518157395}]"};
			
			//资产发行提交
			String signStr = "Fassets=asset_type=5&channel_id=DAM_common&content=yieio test message&source_id=&unit=毛&Fattach=amount=100&expire_date=&state=0&trans_type=1&Fdst1=1Cs3KomKZKj2ctBPDqXfg2jSExreeUwGYM&Fdst1_amount=100&Fdst2=&Fdst2_amount=0&Fpubkey=BASEri2QZf6OYx2BUZR/096PP36afTsI6sWT7F9g2xLcVhGxIAMprspPm0XiWWCmuEeJGSWy2tGJxLC+hMtem80=&Fseqno=201802111440000252&Fsrc=1DZDc8tMFmrQzpMhUjpymAM7Nq3L1Ruq6V&Ftime=2018-02-11 17:42:01";			
			String sign = TrustSDK.signString(channelPrev, signStr.getBytes("UTF-8"));
			System.out.println(sign);			
			String signItem = "[{\"id\":\"1\",\"sign_str\":\""+signStr+"\",\"account\":\"1DZDc8tMFmrQzpMhUjpymAM7Nq3L1Ruq6V\",\"sign\":\""+sign+"\"}]";
			args = new String[] {"tIssSubmit","+mchId+","+mchPrevKey+","[{\"version\":\"1.0\",\"sign_type\":\"ECDSA\",\"node_id\":\"nd_tencent_test1\",\"chain_id\":\"ch_tencent_test\",\"ledger_id\":\"ld_tencent_dam\",\"asset_type\":5,\"timestamp\":1518157395,\"transaction_id\":\"201802111440000252\",\"sign_list\":"+signItem+"}]"};
			
		}
		Command command = CommandFactory.getCommand(args[0]);
		if (command != null) {
			String result = command.execute(args);
			logger.info(String.format("application get result: %s", result));
		} else {
			logger.info(String.format("application doesn't support this argument: [%s]", args[0]));
		}

	}

}
