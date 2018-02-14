# 腾讯区块链TrustSql对接说明

标签： 区块链 腾讯 TrustSql TrustSql_SDK使用说明

---

## SDK接口调试说明
> 以下实例使用的是官方 java v1.0版的SDK 包 [下载地址][1], 使用 eclipse 进行调试调用。本文档为实际操作的一些问题的补充说明，阅读时请先熟悉 [官方文档][2]。

1.生成密钥对
```java
 //GeneratePairKeyCommand.java
 //也可直接调用:
 PairKey pairKey = TrustSDK.generatePairKey();
 //生成的 *密钥对* 在 账户中心 密钥设置中可以使用，后面会说
```

2.使用密钥对内容加密
```java
//SignStrCommand.java
//也可直接调用,prvKey 为你的密钥，srcStr 为你要加密的内容 
//账户中心 密钥设置中 需要使用该方法 生成一个签名， 签名对象是：Tencent TrustSQL
String sign = TrustSDK.signString(prvKey, srcStr.getBytes("UTF-8"));
```
3.共享信息新增/追加
> IssAppendCommand.java 文件中的方法
> 调用该方法之前需要先注册开通 [[Bass开放平台]][3]，目前只支持企业用户注册，需要上传企业营业执照。
> 注册审核通过以后，可以在 账户中心->账户设置 中查看自己的 机构id(mch_id) 和 使用 
> 前两步生成的密钥对完成 密钥设置
> 
测试 共享信息的新增的入参中会使用到 
node_id (节点id-nd_tencent_test1) 在测试链 的 [[基本信息]][4] 中 可以看到对应的 节点ID。
chain_id (联盟链ID-ch_tencent_test) 
ledger_id (账本Id-ld_tencent_iss) 共享信息需使用共享账本
另外还有自己的
mch_id (机构id)，使用自己的密钥签名生成 mch_sign

```js
//入参例子
[{"node_id":"nd_tencent_test1","chain_id":"ch_tencent_test","ledger_id":"ld_tencent_iss","version":"1.0","info_key":"yieio20180211","info_version":1,"state":0,"content":{"content":"test"},"notes":{"notes":"test"},"commit_time":"2018-02-11 15:15:00"}]

//IssAppendCommand.java 的方法中会通过传入的私钥计算出 
//account 和 public_key,通过 CommandFactory调用该方法的完整入参：
args = new String[] {"tIssAppend","你的机构ID","你的密钥","[{"node_id":"nd_tencent_test1","chain_id":"ch_tencent_test","ledger_id":"ld_tencent_iss","version":"1.0","info_key":"yieio20180210","info_version":1,"state":0,"content":{"content":"test"},"notes":{"notes":"test"},"commit_time":"2018-02-09 15:15:00",}]"}

```

### 数字资产的相关接口调用
> 调用数字资产接口的大致顺序：
1. 先注册用户
2. 注册用户账户
3. 发行数字资产 先申请再提交（这里的owner_account 需要用到前面注册用户账户,相当于发数字资产到那个对应的用户账户中）。

用户相关的接口调用跟上面共享信息和资产信息相关的接口调用有些差别，文档中有说明，详情查看 身份管理 中的报文规范 

> 所有场景默认采用UTF-8编码
HTTPS使用单向认证
证书由我方管理并发布
使用POST发送消息
消息请求基于HTTPS的POST方式
HTTP消息头要求
**HTTP请求消息中必须按照如下要求设置头部域：
‘Content-Length:’必须设置成消息体的长度 
‘Content-Type:’必须设置下面的值：
application/json;charset=UTF-8**

在 HttpClientUtil.java 中添加了如下http请求方法
```java
public static String sendHttpPostJson(String httpUrl, String params) {
		HttpPost httpPost = new HttpPost(httpUrl);// httpPost
		try {
			// 封装https 请求头
			BasicHttpEntity stringEntity = new BasicHttpEntity();
			stringEntity.setContentEncoding("UTF-8");
			ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(params.getBytes());
			stringEntity.setContent(tInputStringStream);
			stringEntity.setContentLength(params.length());
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendHttpPost(httpPost);
	}
```

### 重要说明
1.用户管理里面的接口，入参里面有个 seq_no
seq_no	请求流水号	C(32)	R	必须唯一，多次接口请求流水号不能相同
**他的位数必须是32位**

2.req_data	业务参数	JSON	R	参见各接口参数,这个参数文档中写的是 json 格式，其实提交的时候要按 字符串格式提交，如下：
```java
String reqData = "{\"user_id\":\"1235\",\"public_key\":\""+public_key+"\",\"user_fullName\":\"yieio\"}";  
String[] args = new String[] {"tUserRegister","gb2a674789baa797b","sbOIIUCFhCj9LfPkT93ZXJGPoanqL1XsyDm62BkNxCM=","[{\"product_code\":\"YieioTest\",\"req_data\":"+reqData+"}]"};

//在参数提交时使用 jsonObj.GetString 而不是 使用 jsonObj.GetJsonObject：
paramMap.put("req_data", jsonObj.getString("req_data"));
```
3.签名时带上UTF-8的编码，尤其是入参有中文时一定要带，不然会报商户签名无效，或者 系统异常 如下：
```java
paramMap.put("sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8")));
```

4.测试中资产发行申请 默认了一个渠道 DAM_common 秘钥是 lN9byovLrQGi+EvlVv3eBi5hZqadXWnORzddhXwG4mE= ,这里的渠道秘钥在发行提交的时候需要用来对申请返回的签名串进行签名。

附，上述调试 SDK 的github 地址 【[java源码地址][5]】


  [1]: https://baas.trustsql.qq.com/web/baas_blockchain/doc/v1.0/sdk/TrustSQL_SDK_java_v1.0.zip
  [2]: https://baas.trustsql.qq.com/web/baas_blockchain/doc/v1.0/index.shtml#_1
  [3]: https://baas.trustsql.qq.com/web/trust_blockchain/service/service.shtml
  [4]: https://baas.trustsql.qq.com/web/trust_blockchain/testchain/baseinfo.shtml?chain_id=ch_tencent_test&relate_id=testchain_ch_tencent_test_0
  [5]: https://github.com/yieio/trustSqlCase
