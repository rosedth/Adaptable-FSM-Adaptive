package org.rossedth.adaptable_adaptive_fsm;

import org.rossedth.adaptable_adaptive_fsm.Reasoner_FSM.ILimitReachedEvent;
import org.rossedth.adaptive_logic.AdaptiveLogic;

public class AdaptiveLogicListener{

	private AdaptiveLogic sys_U;
	public AdaptiveLogicListener() {		
	}

	public void setSys_U(AdaptiveLogic AL) {
		this.sys_U=AL;
		Reasoner_FSM reasoner= (Reasoner_FSM)sys_U.getReasoner();
		reasoner.setListener(new ILimitReachedEvent() {
			
			@Override
			public void onLimitReached(String entry, EntryTracker_FSM tracker) {
				// TODO Auto-generated method stub
				System.out.println("Entry:"+ entry +" reached the limit");				
			}
		});
	}	
}
