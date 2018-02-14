/**
 * Project Name:trustsql_sdk
 * File Name:TrustSDK.java
 * Package Name:com.tencent.trustsql.sdk
 * Date:Jul 26, 201710:30:31 AM
 * Copyright (c) 2017, Tencent All Rights Reserved.
 *
 */

package com.tencent.trustsql.sdk;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.tencent.trustsql.sdk.algorithm.AESAlgorithm;
import com.tencent.trustsql.sdk.algorithm.DESAlgorithm;
import com.tencent.trustsql.sdk.algorithm.ECDSAAlgorithm;
import com.tencent.trustsql.sdk.bean.PairKey;
import com.tencent.trustsql.sdk.exception.TrustSDKException;
import com.tencent.trustsql.sdk.util.SignStrUtil;

/**
 * ClassName:TrustSDK <br/>
 * Date: Jul 26, 2017 10:30:31 AM <br/>
 * 
 * @author Rony
 * @version
 * @since JDK 1.7
 * @see
 */
public class TrustSDK {

	/**
	 * generatePariKey:产生一对公私钥, 并返回. <br/>
	 * 
	 * @author Rony
	 * @return 返回公私钥对
	 * @throws TrustSDKException
	 * @since JDK 1.7
	 */
	public static PairKey generatePairKey() throws TrustSDKException {
		return generatePairKey(false);
	}
	
	/**
	 * generatePairKey:(这里用一句话描述这个方法的作用). <br/>
	 *
	 * @author ronyyang
	 * @param encodePubKey  是否压缩
	 * @return
	 * @throws TrustSDKException
	 * @since JDK 1.7
	 */
	public static PairKey generatePairKey(boolean encodePubKey) throws TrustSDKException {
		try {
			PairKey pair = new PairKey();
			String privateKey = ECDSAAlgorithm.generatePrivateKey();
			String pubKey = ECDSAAlgorithm.generatePublicKey(privateKey.trim(), encodePubKey);
			pair.setPrivateKey(privateKey);
			pair.setPublicKey(pubKey);
			return pair;
		} catch (Exception e) {
			throw new TrustSDKException(ErrorNum.ECDSA_ENCRYPT_ERROR.getRetCode(), ErrorNum.ECDSA_ENCRYPT_ERROR.getRetMsg(), e);
		}
	}
	
	/**
	 * checkPairKey:验证一对公私钥是否匹配. <br/>
	 *
	 * @author ronyyang
	 * @param prvKey 输入 存放私钥 长度必须为PRVKEY_DIGEST_LENGTH
	 * @param pubKey 输入 存放公钥 长度必须为PUBKEY_DIGEST_LENGTH
	 * @return true 公私钥匹配  false 公私钥不匹配
	 * @throws TrustSDKException
	 * @since JDK 1.7
	 */
	public static boolean checkPairKey(String prvKey, String pubKey) throws TrustSDKException {
		if (StringUtils.isEmpty(prvKey) || StringUtils.isEmpty(pubKey)) {
			throw new TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.getRetCode(), ErrorNum.INVALID_PARAM_ERROR.getRetMsg());
		}
		try {
			String correctPubKey = ECDSAAlgorithm.generatePublicKey(prvKey.trim());
			if(pubKey.trim().equals(correctPubKey)) {
				return true;
			}
			return false;
		} catch(Exception e) {
			throw new TrustSDKException(ErrorNum.ECDSA_ENCRYPT_ERROR.getRetCode(), ErrorNum.ECDSA_ENCRYPT_ERROR.getRetMsg(), e);
		}
	}
	
	/**
	 * generatePubkeyByPrvkey: 通过私钥计算相应公钥. <br/>
	 *
	 * @author Rony
	 * @param privateKey
	 *            私钥字符串
	 * @param encode
	 *            是否压缩公钥
	 * @return 返回公钥字符串
	 * @throws TrustSDKException
	 * @since JDK 1.7
	 */
	public static String generatePubkeyByPrvkey(String privateKey, boolean encode) throws TrustSDKException {
		if (StringUtils.isEmpty(privateKey)) {
			throw new TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.getRetCode(), ErrorNum.INVALID_PARAM_ERROR.getRetMsg());
		}
		try {
			String pubKey = ECDSAAlgorithm.generatePublicKey(privateKey, encode);
			return pubKey;
		} catch (Exception e) {
			throw new TrustSDKException(ErrorNum.ECDSA_ENCRYPT_ERROR.getRetCode(), ErrorNum.ECDSA_ENCRYPT_ERROR.getRetMsg(), e);
		}
	}

	/**
	 * generatePubkeyByPrvkey: 通过私钥计算相应公钥. <br/>
	 *
	 * @author Rony
	 * @param privateKey
	 *            私钥字符串
	 * @return 返回公钥字符串
	 * @throws TrustSDKException
	 * @since JDK 1.7
	 */
	public static String generatePubkeyByPrvkey(String privateKey) throws TrustSDKException {
		return generatePubkeyByPrvkey(privateKey, false);
	}
	
	public static String decodePubkey(String encodePubKey) throws TrustSDKException {
		if (StringUtils.isEmpty(encodePubKey)) {
			throw new TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.getRetCode(), ErrorNum.INVALID_PARAM_ERROR.getRetMsg());
		}
		try {
			String pubKey = ECDSAAlgorithm.decodePublicKey(encodePubKey);
			return pubKey;
		} catch (Exception e) {
			throw new TrustSDKException(ErrorNum.ECDSA_ENCRYPT_ERROR.getRetCode(), ErrorNum.ECDSA_ENCRYPT_ERROR.getRetMsg(), e);
		}
	}

	/**
	 * generateAddrByPubkey:通过公钥获取对应地址. <br/>
	 *
	 * @author Rony
	 * @param pubKey
	 *            公钥字符串
	 * @return
	 * @throws TrustSDKException
	 * @since JDK 1.7
	 */
	public static String generateAddrByPubkey(String pubKey) throws TrustSDKException {
		if (StringUtils.isEmpty(pubKey)) {
			throw new TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.getRetCode(), ErrorNum.INVALID_PARAM_ERROR.getRetMsg());
		}
		try {
			String address = ECDSAAlgorithm.getAddress(Base64.decodeBase64(pubKey));
			return address;
		} catch (Exception e) {
			throw new TrustSDKException(ErrorNum.ECDSA_ENCRYPT_ERROR.getRetCode(), ErrorNum.ECDSA_ENCRYPT_ERROR.getRetMsg(), e);
		}
	}

	/**
	 * generateAddrByPrvkey:通过私钥计算相应地址. <br/>
	 *
	 * @author Rony
	 * @param privateKey
	 *            私钥字符串
	 * @return
	 * @throws TrustSDKException
	 * @since JDK 1.7
	 */
	public static String generateAddrByPrvkey(String privateKey) throws TrustSDKException {
		if (StringUtils.isEmpty(privateKey)) {
			throw new TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.getRetCode(), ErrorNum.INVALID_PARAM_ERROR.getRetMsg());
		}
		try {
			String pubKey = ECDSAAlgorithm.generatePublicKey(privateKey);
			String address = generateAddrByPubkey(pubKey);
			return address;
		} catch (Exception e) {
			throw new TrustSDKException(ErrorNum.ECDSA_ENCRYPT_ERROR.getRetCode(), ErrorNum.ECDSA_ENCRYPT_ERROR.getRetMsg(), e);
		}
	}

	/**
	 * signString:为字符串进行签名, 并返回签名. <br/>
	 *
	 * @author Rony
	 * @param privateKey
	 *            私钥字符串
	 * @param data
	 *            需要签名的字符数组
	 * @return 返回签名字符串
	 * @throws TrustSDKException
	 * @since JDK 1.7
	 */
	public static String signString(String privateKey, byte[] data) throws TrustSDKException {
		if (StringUtils.isEmpty(privateKey)) {
			throw new TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.getRetCode(), ErrorNum.INVALID_PARAM_ERROR.getRetMsg());
		}
		try {
			String result = ECDSAAlgorithm.sign(privateKey, data);
			return result;
		} catch (Exception e) {
			throw new TrustSDKException(ErrorNum.SIGN_ERROR.getRetCode(), ErrorNum.SIGN_ERROR.getRetMsg(), e);
		}
	}

	/**
	 * verifyString:验证一个签名是否有效. <br/>
	 *
	 * @author Rony
	 * @param pubKey
	 *            公钥字符串
	 * @param srcString
	 *            源字符串
	 * @param sign
	 *            签名字符串
	 * @return 返回验证是否通过 true:验证成功 false:验证失败
	 * @throws TrustSDKException
	 * @since JDK 1.7
	 */
	public static boolean verifyString(String pubKey, String srcString, String sign) throws TrustSDKException {
		if (StringUtils.isEmpty(pubKey) || StringUtils.isEmpty(srcString) || StringUtils.isEmpty(sign)) {
			throw new TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.getRetCode(), ErrorNum.INVALID_PARAM_ERROR.getRetMsg());
		}
		try {
			return ECDSAAlgorithm.verify(srcString, sign, pubKey);
		} catch (Exception e) {
			throw new TrustSDKException(ErrorNum.ECDSA_ENCRYPT_ERROR.getRetCode(), ErrorNum.ECDSA_ENCRYPT_ERROR.getRetMsg(), e);
		}
	}

	/**
	 * encryptDES3:. 使用DES3加密字符串<br/>
	 *
	 * @author Rony
	 * @param key
	 *            存放用于des3加密的key 并最大长度不超过KEY_DES3_DIGEST_LENGTH
	 * @param data
	 *            需要加密的字符数组
	 * @return 返回DES3加密字符串
	 * @throws TrustSDKException
	 * @since JDK 1.7
	 */
	public static byte[] encryptDES3(String key, byte[] data) throws TrustSDKException {
		if (StringUtils.isEmpty(key) || key.length() > Constants.KEY_DES3_DIGEST_LENGTH) {
			throw new TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.getRetCode(), ErrorNum.INVALID_PARAM_ERROR.getRetMsg());
		}
		try {
			byte[] keybytes = key.getBytes("UTF-8");
			if (keybytes.length < Constants.KEY_DES3_DIGEST_LENGTH) {
				keybytes = handleKey(keybytes, Constants.KEY_DES3_DIGEST_LENGTH);
			}
			byte[] result = DESAlgorithm.encrypt(data, keybytes);
			return result;
		} catch (Exception e) {
			throw new TrustSDKException(ErrorNum.DES3_ENCRYPT_ERROR.getRetCode(), ErrorNum.DES3_ENCRYPT_ERROR.getRetMsg(), e);
		}
	}

	/**
	 * decryptDES3:使用DES3解密字符串. <br/>
	 *
	 * @author Rony
	 * @param key
	 *            存放用于des3解密的key 并最大长度不超过KEY_DES3_DIGEST_LENGTH
	 * @param data
	 *            需要解密的字符数组
	 * @return 返回明文字符串
	 * @throws TrustSDKException
	 * @since JDK 1.7
	 */
	public static byte[] decryptDES3(String key, byte[] data) throws TrustSDKException {
		if (StringUtils.isEmpty(key) || key.length() > Constants.KEY_DES3_DIGEST_LENGTH) {
			throw new TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.getRetCode(), ErrorNum.INVALID_PARAM_ERROR.getRetMsg());
		}
		try {
			// FIXME
			byte[] keybytes = key.getBytes("UTF-8");
			if (keybytes.length < Constants.KEY_DES3_DIGEST_LENGTH) {
				keybytes = handleKey(keybytes, Constants.KEY_DES3_DIGEST_LENGTH);
			}
			byte[] result = DESAlgorithm.decrypt(data, keybytes);
			return result;
		} catch (Exception e) {
			throw new TrustSDKException(ErrorNum.DES3_ENCRYPT_ERROR.getRetCode(), ErrorNum.DES3_ENCRYPT_ERROR.getRetMsg(), e);
		}
	}

	/**
	 * encryptAES128:使用AES128加密字符串. <br/>
	 *
	 * @author Rony
	 * @param key
	 *            存放用于AES128加密的key 并最大长度不超过KEY_AES128_DIGEST_LENGTH
	 * @param data
	 *           需要加密的字符数组
	 * @return 返回加密字符串
	 * @throws TrustSDKException
	 * @since JDK 1.7
	 */
	public static byte[] encryptAES128(String key, byte[] data) throws TrustSDKException {
		if (StringUtils.isEmpty(key) || key.length() > Constants.KEY_AES128_DIGEST_LENGTH) {
			throw new TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.getRetCode(), ErrorNum.INVALID_PARAM_ERROR.getRetMsg());
		}
		try {
			byte[] keybytes = key.getBytes("UTF-8");
			if (keybytes.length < Constants.KEY_AES128_DIGEST_LENGTH) {
				keybytes = handleKey(keybytes, Constants.KEY_AES128_DIGEST_LENGTH);
			}
			byte[] result = AESAlgorithm.aesEncode(keybytes, data);
			return result;
		} catch (Exception e) {
			throw new TrustSDKException(ErrorNum.AES_ENCRYPT_ERROR.getRetCode(), ErrorNum.AES_ENCRYPT_ERROR.getRetMsg(), e);
		}
	}

	/**
	 * decryptAES128:使用AES128解密字符串. <br/>
	 *
	 * @author Rony
	 * @param key
	 *            存放用于AES128解密的key 并最大长度不超过KEY_AES128_DIGEST_LENGTH
	 * @param data
	 *            需要解密的字符数组
	 * @return 返回解密字符串
	 * @throws TrustSDKException
	 * @since JDK 1.7
	 */
	public static byte[] decryptAES128(String key, byte[] data) throws TrustSDKException {
		if (StringUtils.isEmpty(key) || key.length() > Constants.KEY_AES128_DIGEST_LENGTH) {
			throw new TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.getRetCode(), ErrorNum.INVALID_PARAM_ERROR.getRetMsg());
		}
		try {
			byte[] keybytes = key.getBytes("UTF-8");
			if (keybytes.length < Constants.KEY_AES128_DIGEST_LENGTH) {
				keybytes = handleKey(keybytes, Constants.KEY_AES128_DIGEST_LENGTH);
			}
			byte[] result = AESAlgorithm.aesDecode(keybytes, data);
			return result;
		} catch (Exception e) {
			throw new TrustSDKException(ErrorNum.AES_ENCRYPT_ERROR.getRetCode(), ErrorNum.AES_ENCRYPT_ERROR.getRetMsg(), e);
		}
	}

	/**
	 * issSign:生成一个用于共享信息的签名. <br/>
	 *
	 * @author Rony
	 * @param infoKey
	 *            自定义的信息单号
	 * @param infoVersion
	 *            共享信息版本
	 * @param state
	 *            共享信息状态编码
	 * @param content
	 *            存放content的json格式字段
	 * @param notes
	 *            存放notes的json格式字段
	 * @param commitTime
	 *            提交共享信息时间 格式为YYYY-MM-DD HH:mm:
	 * @param privateKey
	 *            存放共享信息发起方的私钥 长度必须为PRVKEY_DIGEST_LENGTH
	 * @return 返回签名
	 * @throws TrustSDKException
	 * @since JDK 1.7
	 */
	public static String issSign(String infoKey, String infoVersion, int state, String content, String notes,
			String commitTime, String privateKey) throws TrustSDKException {
		if (StringUtils.isEmpty(infoKey) || StringUtils.isEmpty(infoVersion) || StringUtils.isEmpty(content)
				|| StringUtils.isEmpty(notes) || StringUtils.isEmpty(commitTime) || StringUtils.isEmpty(privateKey)) {
			throw new TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.getRetCode(), ErrorNum.INVALID_PARAM_ERROR.getRetMsg());
		}
		try {
			String pubKey = generatePubkeyByPrvkey(privateKey);
			String addr = generateAddrByPrvkey(privateKey);
			content = content.replaceAll("\r", "\\\\\\\\r").replaceAll("\n", "\\\\\\\\n");
			JSONObject contentJson = JSONObject.parseObject(content, Feature.DisableSpecialKeyDetect);
			String contentStr = SignStrUtil.mapToKeyValueStr(SignStrUtil.jsonToMap(contentJson));
			notes = notes.replaceAll("\r", "\\\\\\\\r").replaceAll("\n", "\\\\\\\\n");
			JSONObject noteJson = JSONObject.parseObject(notes);
			String noteStr = SignStrUtil.mapToKeyValueStr(SignStrUtil.jsonToMap(noteJson));
			Map<String, Object> treeMap = new TreeMap<String, Object>();
			treeMap.put("Faccount", addr);
			treeMap.put("Finfo_key", infoKey);
			treeMap.put("Finfo_version", infoVersion);
			treeMap.put("Fstate", state);
			treeMap.put("Fcontent", contentStr);
			treeMap.put("Fnotes", noteStr);
			treeMap.put("Fcommit_time", commitTime);
			treeMap.put("Fpubkey", pubKey);
			String srcStr = SignStrUtil.mapToKeyValueStr(treeMap);
			System.out.println(srcStr);
			String result = signString(privateKey, srcStr.getBytes("UTF-8"));
			return result;
		} catch (Exception e) {
			throw new TrustSDKException(ErrorNum.GENERATE_SIGN_ERROR.getRetCode(), ErrorNum.GENERATE_SIGN_ERROR.getRetMsg(), e);
		}
	}
	
	/**
	 * issVerifySign:验证一个共享信息的签名. <br/>
	 *
	 * @author Rony
	 * @param infoKey
	 *            自定义的信息单号
	 * @param infoVersion
	 *            共享信息版本
	 * @param state
	 *            共享信息状态编码
	 * @param content
	 *            存放content的json格式字段
	 * @param notes
	 *            存放notes的json格式字段
	 * @param commitTime
	 *            提交共享信息时间 格式为YYYY-MM-DD HH:mm:SS
	 * @param pubkey
	 *            存放共享信息发起方的公钥 长度必须为PUBKEY_DIGEST_LENGTH
	 *            
	 * @param sign
	 *            存放签名 长度必须为SIGN_DIGEST_LENGTH
	 *            
	 * @return 返回验证签名结果
	 * @throws TrustSDKException
	 * @since JDK 1.7
	 */
	public static boolean issVerifySign(String infoKey, String infoVersion, int state, String content, String notes, String commitTime, String pubkey, String sign) throws TrustSDKException {
		if (StringUtils.isEmpty(infoKey) || StringUtils.isEmpty(infoVersion) || StringUtils.isEmpty(content)
				|| StringUtils.isEmpty(notes) || StringUtils.isEmpty(commitTime) || StringUtils.isEmpty(pubkey) || StringUtils.isEmpty(sign)) {
			throw new TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.getRetCode(), ErrorNum.INVALID_PARAM_ERROR.getRetMsg());
		}
		try {
			String addr = TrustSDK.generateAddrByPubkey(pubkey);
			content = content.replaceAll("\r", "\\\\\\\\r").replaceAll("\n", "\\\\\\\\n");
			JSONObject contentJson = JSONObject.parseObject(content, Feature.DisableSpecialKeyDetect);
			String contentStr = SignStrUtil.mapToKeyValueStr(SignStrUtil.jsonToMap(contentJson));
			notes = notes.replaceAll("\r", "\\\\\\\\r").replaceAll("\n", "\\\\\\\\n");
			JSONObject noteJson = JSONObject.parseObject(notes);
			String noteStr = SignStrUtil.mapToKeyValueStr(SignStrUtil.jsonToMap(noteJson));
			Map<String, Object> treeMap = new TreeMap<String, Object>();
			treeMap.put("Faccount", addr);
			treeMap.put("Finfo_key", infoKey);
			treeMap.put("Finfo_version", infoVersion);
			treeMap.put("Fstate", state);
			treeMap.put("Fcontent", contentStr);
			treeMap.put("Fnotes", noteStr);
			treeMap.put("Fcommit_time", commitTime);
			treeMap.put("Fpubkey", pubkey);
			String srcStr = SignStrUtil.mapToKeyValueStr(treeMap);
			boolean result = TrustSDK.verifyString(pubkey, srcStr, sign);
			return result;
		} catch(Exception e) {
			throw new TrustSDKException(ErrorNum.VERIFY_SIGN_ERROR.getRetCode(), ErrorNum.VERIFY_SIGN_ERROR.getRetMsg(), e);
		}
		
	}

	/**
	 * generateTransSql:生成一个已经签好名用于发出交易的sql语句. <br/>
	 *
	 * @author Rony
	 * @param fseqno
	 *            输入自定义的交易单号,必须以\0结尾
	 * @param privateKey
	 *            输入存放交易发起方的私钥,长度必须为PRVKEY_DIGEST_LENGTH
	 * @param fdst1
	 *            输入存放交易接收方1的地址,长度必须为ADDR_DIGEST_LENGTH
	 * @param fdst1_amount
	 *            输入存放交易接收方1的份额
	 * @param fdst2
	 *            输入存放交易接收方2的地址(可以为空字符串,但不能为NULL) 长度必须为ADDR_DIGEST_LENGTH
	 * @param fdst2_amount
	 *            输入存放交易接收方2的份额(当Fdst2为空时,此字段被忽略)
	 * @param fassets
	 *            输入存放assets的json格式字段(不能为空字符串或空的json)
	 * @param fattach
	 *            输入存放attach的json格式字段(不能为空字符串或空的json)
	 * @param ftime
	 *            输入存放交易时间 格式为YYYY-MM-DD HH:mm:SS
	 * @return 返回交易的sql语句
	 * @throws TrustSDKException
	 * @since JDK 1.7
	 */
	public static String generateTransSql(String fseqno, String privateKey, String fdst1, double fdst1_amount, String fdst2,
			double fdst2_amount, String fassets, String fattach, String ftime) throws TrustSDKException {
		if (StringUtils.isEmpty(fseqno) || StringUtils.isEmpty(privateKey) || StringUtils.isEmpty(fdst1)
				|| StringUtils.isEmpty(fdst2) || StringUtils.isEmpty(fassets) || StringUtils.isEmpty(fattach)
				|| StringUtils.isEmpty(ftime)) {
			throw new TrustSDKException(ErrorNum.INVALID_PARAM_ERROR.getRetCode(), ErrorNum.INVALID_PARAM_ERROR.getRetMsg());
		}
		try {
			String pubKey = generatePubkeyByPrvkey(privateKey);
			String addr = generateAddrByPrvkey(privateKey);
			fassets = fassets.replaceAll("\r", "\\\\\\\\r").replaceAll("\n", "\\\\\\\\n");
			JSONObject assetsJson = JSONObject.parseObject(fassets);
			String assetsStr = SignStrUtil.mapToKeyValueStr(SignStrUtil.jsonToMap(assetsJson));
			fattach = fattach.replaceAll("\r", "\\\\\\\\r").replaceAll("\n", "\\\\\\\\n");
			JSONObject attachJson = JSONObject.parseObject(fattach);
			String attachStr = SignStrUtil.mapToKeyValueStr(SignStrUtil.jsonToMap(attachJson));
			Map<String, Object> treeMap = new TreeMap<String, Object>();
			treeMap.put("Fseqno", fseqno);
			treeMap.put("Fsrc", addr);
			treeMap.put("Fdst1", fdst1);
			treeMap.put("Fdst1_amount", fdst1_amount);
			treeMap.put("Fdst2", fdst2);
			treeMap.put("Fdst2_amount", fdst2_amount);
			treeMap.put("Fassets", assetsStr);
			treeMap.put("Fattach", attachStr);
			treeMap.put("Ftime", ftime);
			treeMap.put("Fpubkey", pubKey);
			String srcStr = SignStrUtil.mapToKeyValueStr(treeMap);
			String sign = signString(privateKey, srcStr.getBytes("UTF-8"));
			String result = String
					.format("insert into t_transaction (Fseqno,Fsrc,Fdst1,Fdst1_amount,Fdst2,Fdst2_amount,Ftime,Fpubkey,Fsign,Fassets,Fattach) values ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s');",
							fseqno, addr, fdst1, fdst1_amount, fdst2, fdst2_amount, ftime, pubKey, sign, assetsStr, attachStr);
			return result;
		} catch (Exception e) {
			throw new TrustSDKException(ErrorNum.GENERATE_SQL_ERROR.getRetCode(), ErrorNum.GENERATE_SQL_ERROR.getRetMsg(), e);
		}
	}
	
	private static byte[] handleKey(byte[] originBytes, int length) {
		byte[] result = new byte[length];
		System.arraycopy(originBytes, 0, result, 0, originBytes.length);
		for (int i = originBytes.length; i < length; i++) {
			result[i] = 0x00;
		}
		return result;
	}
}
