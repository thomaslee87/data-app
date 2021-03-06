package com.intellbi.data.dao.dataobject;

import java.util.HashMap;
import java.util.Map;

public class ConsumerSessionMap {

	private Map<String,ConsumerSession> session = new HashMap<String, ConsumerSession>();
	
	public void add(String phoneNo, ConsumerBillDetailWrapper consumerBillDetailWrapper) {
		ConsumerSession userSession = session.get(phoneNo);
		if(userSession == null) {
			userSession = new ConsumerSession();
			session.put(phoneNo, userSession);
		}
		userSession.add(phoneNo, consumerBillDetailWrapper);
	}
	
	public Map<String,ConsumerSession> getConsumerSession(){
		return session;
	}
}
