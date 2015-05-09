package com.intellbit.v2.data.importer;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class BiUser {
	
	private int id;
	private int groupId;
	private String username;
	private String realname;
	private String password;
	private Date gmtCreate;
	private Date gmtLogin;
	private int status;
	
}
