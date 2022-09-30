package org.rossedth.adaptable_adaptive_fsm;

import org.jeasy.states.api.AbstractEvent;
import org.jeasy.states.api.State;
import org.rossedth.adaptive_logic.Data;

public class FSMData implements Data{
 	protected State state;
    protected AbstractEvent event;

    public FSMData(State s, AbstractEvent e) {
    	this.state=s;
    	this.event=e;
    }
    public State getState() {
 		return state;
 	}

 	public void setState(State state) {
 		this.state = state;
 	}

 	public AbstractEvent getEvent() {
 		return event;
 	}

 	public void setEvent(AbstractEvent event) {
 		this.event = event;
 	}

}
