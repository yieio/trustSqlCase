package com.tencent.trustsql.sdk.demo.command;

public class HelpCommand implements Command {

	@Override
	public String execute(String... args) throws Exception {
	    StringBuilder sb = new StringBuilder();
	    sb.append("\nUsage: %s <command> <parameters> [options]\n");
	    sb.append("\ncommand:\n");
	    sb.append("tGeneratePairkey [count]\n");
	    sb.append("tGeneratePubkeyByPrvkey <'[\"private_key\", ...]'>\n");
	    sb.append("tGenerateAddrByPubkey <'[\"public_key\", ...]'>\n");
	    sb.append("tGenerateAddrByPrvkey <'[\"private_key\", ...]'>\n");
	    sb.append("tSignString <'private_key'> <'text'>\n");
	    sb.append("tVerifySign <'public_key'> <'text'> <'sign'>\n");
	    sb.append("tIssAppend <mch_id> <mch_private_key> <'[{\"version\",\"info_key\",\"info_version\",\"state\",\"content\",\"notes\",\"commit_time\",\"private_key\"} , ...]'> [--domain \"\"] [--proxy \"\"] [--verbose]\n");
	    sb.append("tIssQuery <mch_id> <mch_private_key> <'[{\"version\",\"info_key\",\"state\",\"content\",\"notes\",\"range\",\"address\",\"t_hash\",\"page_no\",\"page_limit\"} , ...]'> [--domain \"\"] [--proxy \"\"] [--verbose]\n");
	    sb.append("\n");
	    return sb.toString();
	}

}
