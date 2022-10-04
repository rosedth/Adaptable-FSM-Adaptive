package org.rossedth.adaptable_adaptive_fsm;

import java.util.HashMap;


public class EntryTracker_FSM {
	private HashMap<String,Integer> dictionary;
	private Integer limit;
	
	public EntryTracker_FSM() {
		dictionary=new HashMap<String, Integer>();
	}
	
	public Integer getLimit() {
		return limit;
	}


	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	
	public void addEntry(String entry) {
		if (dictionary.containsKey(entry)){
			dictionary.replace(entry, dictionary.get(entry)+1);
		}else {
			dictionary.put(entry, 1);
		}
	}
	
	public boolean verifyLimit(String entry) {
		return dictionary.get(entry)==limit;
	}

}
