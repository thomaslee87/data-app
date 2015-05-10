package com.intellbit.v2.data.importer;

import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class BiConsumer {

	private int id;
	private int managerId;
	private String phone;
	private String name;
	private String manager;
	private String vipLevel;
	private String isGroupUser;
	private String identity;
	private String identityType;
	
}
