package com.intellbi.data.dataobject;

import java.util.HashMap;
import java.util.Map;

public class ConsumerSessionMap {

	private Map<String,ConsumerSession> session = new HashMap<String, ConsumerSession>();
	
	public void add(String phoneNo, ConsumerSessionNode consumerSessionNode) {
		ConsumerSession userSession = session.get(phoneNo);
		if(userSession == null) {
			userSession = new ConsumerSession();
			session.put(phoneNo, userSession);
		}
		userSession.add(phoneNo, consumerSessionNode);
	}
	
	public Map<String,ConsumerSession> getMap(){
		return session;
	}
}
