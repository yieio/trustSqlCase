package com.tencent.trustsql.sdk.demo.command;

public class CommandFactory {
	
	public static final String CMD_ISSAPPEND = "tIssAppend";
	public static final String CMD_ISSQUERY = "tIssQuery";
	public static final String CMD_ISSAPPLY = "tIssApply";
	public static final String CMD_ISSSUBMIT = "tIssSubmit";
	public static final String CMD_GENERATE_PAIRKEY = "tGeneratePairkey";
	public static final String CMD_CHECK_PAIRKEY = "tCheckPairkey";
	public static final String CMD_GENERATE_PUBKEY_BY_PRVKEY = "tGeneratePubkeyByPrvkey";
	public static final String CMD_GENERATE_ADDR_BY_PUBKEY = "tGenerateAddrByPubkey";
	public static final String CMD_GENERATE_ADDR_BY_PRVKEY = "tGenerateAddrByPrvkey";
	public static final String CMD_SIGN_STR = "tSignString";
	public static final String CMD_VERIFY_STR = "tVerifySign";
	public static final String CMD_HELP = "help";
	
	public static final String CMD_USEREGISTER="tUserRegister";
	public static final String CMD_CREATEUSERACCOUNT="tCreateUserAccount";
	
	public static Command getCommand(String option) {
		if(CMD_ISSAPPEND.equals(option)) {
			return new IssAppendCommand();
		} else if(CMD_ISSQUERY.equals(option)) {
			return new IssQueryCommand();
		}else if(CMD_ISSAPPLY.equals(option)) {
			return new IssApplyCommand();
		}else if(CMD_ISSSUBMIT.equals(option)) {
			return new IssSubmitCommand();
		}
		else if(CMD_GENERATE_PAIRKEY.equals(option)) {
			return new GeneratePairKeyCommand();
		} else if(CMD_GENERATE_PUBKEY_BY_PRVKEY.equals(option)) {
			return new GeneratePubKeyByPrvKeyCommand();
		} else if(CMD_GENERATE_ADDR_BY_PUBKEY.equals(option)) {
			return new GenerateAddrByPubKeyCommand();
		} else if(CMD_GENERATE_ADDR_BY_PRVKEY.equals(option)) {
			return new GenerateAddrByPrvKeyCommand();
		} else if(CMD_SIGN_STR.equals(option)) {
			return new SignStrCommand();
		} else if(CMD_VERIFY_STR.equals(option)) {
			return new VerifyStrCommand();
		} else if(CMD_HELP.equals(option)) {
			return new HelpCommand(); 
		}else if(CMD_USEREGISTER.equals(option)) {
			return new UserRegisterCommand(); 
		}else if(CMD_CREATEUSERACCOUNT.equals(option)) {
			return new CreateUserAccountCommand(); 
		}
		return null;
	}

}
