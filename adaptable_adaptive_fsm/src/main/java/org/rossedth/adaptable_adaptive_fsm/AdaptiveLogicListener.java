package org.rossedth.adaptable_adaptive_fsm;

import org.rossedth.adaptable_adaptive_fsm.Monitor_FSM.ILimitReachedEvent;
import org.rossedth.adaptive_logic.AdaptiveLogic;

public class AdaptiveLogicListener{

	private AdaptiveLogic sys_U;
	public AdaptiveLogicListener() {		
	}

	public void setSys_U(AdaptiveLogic AL) {
		this.sys_U=AL;
		Monitor_FSM monitor= (Monitor_FSM)sys_U.getMonitor();
		monitor.setLimitReachedListener(new ILimitReachedEvent() {
			
			@Override
			public void onLimitReached(String entry, EntryTracker_FSM tracker) {
				// TODO Auto-generated method stub
				System.out.println("Entry:"+ entry +" reached the limit of tries, we will ignore its use.");
				monitor.getBlockedEntries().add(entry);
			}
		});
	}	
}
