package org.rossedth.adaptable_adaptive_fsm;

import java.util.Set;

import org.jeasy.states.api.AbstractEvent;
import org.jeasy.states.api.FiniteStateMachine;
import org.jeasy.states.api.FiniteStateMachineException;
import org.jeasy.states.api.State;
import org.jeasy.states.api.Transition;
import org.jeasy.states.core.TransitionBuilder;
import org.rossedth.adaptable_fsm.AEvent;
import org.rossedth.adaptable_fsm.BEvent;
import org.rossedth.adaptable_fsm.CEvent;
import org.rossedth.adaptable_fsm.NNEvent;
import org.rossedth.adaptive_logic.Executor;
import org.rossedth.adaptive_logic.Memory;
import org.rossedth.adaptive_logic.Plan;
import org.rossedth.adaptable_fsm.RecognizerFSM;

public class Executor_FSM extends Executor {

	public Executor_FSM(Memory mem) {
		super(mem);
	}


	public void command(Plan response) {
		System.out.println("Commanding Actions from Executor"); 
		RecognizerFSM recognizer=(RecognizerFSM)this.getSysU();
		FiniteStateMachine fsm= (FiniteStateMachine)recognizer.getFSM();
		//		Transition transition = fsm.getLastTransition();
		//		
		//		if (transition!=null) {
		//			System.out.println(transition.toString());
		//		}
		//		else {
		//			System.out.println("There was no transition made");
		//		}
		//
		if (response!=null) {
			System.out.println("Executor needs to translate the plan and modify the FSM");	
			FSMAction action=(FSMAction)response.getActions().get(0);
			if(action.getType().equalsIgnoreCase("remove")) {
				applyRemoveAction(action, recognizer, fsm);
			}
			else if(action.getType().equalsIgnoreCase("new")) {
				applyNewAction(action, recognizer, fsm);
			}

		}

	}

	@SuppressWarnings("rawtypes")
	private Class getEventClassType(String eventType) {
		switch (eventType) {
			case "AEvent": return AEvent.class;
			case "BEvent": return BEvent.class;
			case "CEvent": return CEvent.class;
			default: return NNEvent.class;
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void applyRemoveAction(FSMAction action,RecognizerFSM recognizer,FiniteStateMachine fsm) {
		if(action.getElement().equalsIgnoreCase("transition")) {
			System.out.println("Executor needs to remove a transition");
			String source=action.getSource();
			Class eventTypeClass=getEventClassType(action.getEventType());
			Set<Transition> transitions= fsm.getTransitions();
			for (Transition t: transitions) {
				//looking for transition "P to Q with B"
				if (t.getSourceState().getName().equalsIgnoreCase(source)&&
						(t.getEventType()==eventTypeClass)){
					transitions.remove(t);
					break;
				} 
			}		

			recognizer.printCurrentState(Launcher.viewer);
			System.out.println("=================================================");

		}else {
			System.out.println("Executor needs to remove a state");					
		}
	}
	
	private void applyNewAction(FSMAction action,RecognizerFSM recognizer,FiniteStateMachine fsm) {
		// Create new state
		State p1_State = new State("P1");
		fsm.getStates().add(p1_State);
		
		// Look for source state
		
		Transition p_p1 = new TransitionBuilder()
				.name("P to P1 with k")
				.sourceState(fsm.getCurrentState())
				.eventType(NNEvent.class)
				.targetState(p1_State)
				.build();

		Set<State> states=fsm.getStates();
		State q_State=null;
		
		// Look for target state
		for(State s:states) {
			if (s.getName().equalsIgnoreCase("Q")) {
				q_State=s;
			}
		}

		//Build transition
		Transition p1_q = new TransitionBuilder()
				.name("P1 to Q with z")
				.sourceState(p1_State)
				.eventType(NNEvent.class)
				.targetState(q_State)
				.build();

		// Add transition to transitions set
		Set<Transition> transitions= fsm.getTransitions();
		transitions.add(p_p1);
		transitions.add(p1_q);

		// Modify accepted entries for FSM
		recognizer.addFSMAcceptedEntry("K");
		recognizer.addFSMAcceptedEntry("Z");
		
		// This should be another FSMAction, something like "TimeAction" with the parameter
		int delay=5000;
		recognizer.getListener().onTimer(delay);

		//				System.out.println("STATES ");
		//				for(State s:sys.getStates()) {
		//					System.out.println("State : " + s.getName());			
		//				}
		//				System.out.println("TRANSITIONS ");
		//				for(Transition t:sys.getTransitions()) {
		//					System.out.println("Transition : " + t.getName());			
		//				}
		//				System.out.println("=================================================");

		try {
			AbstractEvent event=new NNEvent("KEvent");
			fsm.fire(event);
			recognizer.buildPath(event);   
			System.out.println("Processed sequence: "+ recognizer.getPath());  
		} catch (FiniteStateMachineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
