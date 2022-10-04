package org.rossedth.adaptable_adaptive_fsm;

import java.util.ArrayList;
import java.util.List;

import org.jeasy.states.api.AbstractEvent;
import org.jeasy.states.api.State;
import org.rossedth.adaptable_fsm.NNEvent;
import org.rossedth.adaptive_logic.Action;
import org.rossedth.adaptive_logic.Data;
import org.rossedth.adaptive_logic.Memory;
import org.rossedth.adaptive_logic.Plan;
import org.rossedth.adaptive_logic.Reasoner;

public class Reasoner_FSM extends Reasoner{
	private EntryTracker_FSM tracker;
    private ILimitReachedEvent listener;
    private List<String> blockedEntries;
	
	public Reasoner_FSM(Memory mem) {
		super(mem);
		this.tracker=new EntryTracker_FSM();
		this.tracker.setLimit(3);
		this.listener=null;
	}
	
	public void process(Data data) {
		FSMData fsm_data=(FSMData)data;
		State s=fsm_data.getState();
		List<Plan> responses=new ArrayList<Plan>();;
		
		AbstractEvent e=fsm_data.getEvent();
		System.out.println("Processing Data with reasoner");
		
		if(s.getName().equalsIgnoreCase("P") &&
			e.getName().equalsIgnoreCase("K")) {
			Plan plan=new Plan();
			List<Action> actions=new ArrayList<Action>();
			
			actions.add(new FSMAction("new", "state", "P1"));
			actions.add(new FSMAction("new", "transition", "P to P1", "P", "P1", "KEvent"));
			actions.add(new FSMAction("new", "transition", "P1 to Q", "P1", "Q", "ZEvent"));
			plan.setActions(actions);
			responses.add(plan);
			
		}
		
		// If the event was raised by a TimeEvent		
		if(e.getName().equalsIgnoreCase("TimeEvent")) {
			Plan plan=new Plan();
			List<Action> actions=new ArrayList<Action>();
			actions.add(new FSMAction("remove", "transition", "P to Q","P","Q","BEvent"));
			plan.setActions(actions);
			responses.add(plan);
		}
		
		// If the event was raised by an UNIDENTIFIED ENTRY (NNEvent)
		if(e.getClass()==NNEvent.class){
			String entry=e.getName();
			trackUnidentifiedEntries(entry);
		}
		
		setResponses(responses);
		sendResponses();
		
	}
	
	public void setTracker(EntryTracker_FSM tracker) {
		this.tracker=tracker;
	}
	
	public EntryTracker_FSM getTracker() {
		return this.tracker;
	}
	
	public void setBlockedEntries(List<String> blocked) {
		this.blockedEntries=blocked;
	}
	
	public List<String> getBlockedEntries() {
		return this.blockedEntries;
	}
	
	public interface ILimitReachedEvent{
		public void onLimitReached(String entry, EntryTracker_FSM tracker);
	}

	
    public ILimitReachedEvent getListener() {
    	return this.listener;
    }
    public void setListener(ILimitReachedEvent listener) {
        this.listener = listener;
    }    

	public void trackUnidentifiedEntries(String entry) {
		tracker.addEntry(entry);
		if (tracker.verifyLimit(entry)) {
				this.listener.onLimitReached(entry, tracker);

		}
	}
	
}