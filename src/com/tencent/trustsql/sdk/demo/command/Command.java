package com.tencent.trustsql.sdk.demo.command;

public interface Command {
	
	public String execute(String... args) throws Exception;

}
