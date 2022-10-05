package org.rossedth.adaptable_adaptive_fsm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import org.jeasy.states.api.AbstractEvent;
import org.rossedth.adaptable_fsm.NNEvent;
import org.rossedth.adaptable_fsm.RecognizerFSM;
import org.rossedth.adaptable_fsm.RecognizerFSM.UndefinedEntryListener;
import org.rossedth.adaptive_logic.Memory;
import org.rossedth.adaptive_logic.Monitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Monitor_FSM extends Monitor {
	private RecognizerFSM sys_U;

	/*
	 * New properties to represent the adaptable behavior
	 * */
	
	private EntryTracker_FSM tracker;
    private List<String> blockedEntries;
    
    private ILimitReachedEvent limitReachedListener;
    private UndefinedEntryListener undefinedEntryListener;

	public Monitor_FSM() {};
	public Monitor_FSM (Memory mem,int max) {
		super(mem);
		this.tracker=new EntryTracker_FSM();
		this.tracker.setLimit(max);
		this.blockedEntries=new ArrayList<String>();
		this.limitReachedListener=null;
		undefinedEntryListener=null;
	}
	
	public void setTracker(EntryTracker_FSM tracker) {
		this.tracker=tracker;
	}
	
	public EntryTracker_FSM getTracker() {
		return this.tracker;
	}
	
    public ILimitReachedEvent getLimitReachedListener() {
    	return this.limitReachedListener;
    }
    public void setLimitReachedListener(ILimitReachedEvent listener) {
        this.limitReachedListener = listener;
        
    }    

	public UndefinedEntryListener getUndefinedEntryListener() {
		return undefinedEntryListener;
	}
	public void setUndefinedEntryListener(UndefinedEntryListener undefinedEntryListener) {
		this.undefinedEntryListener = undefinedEntryListener;
		this.sys_U.setUndefinedEntryListener(undefinedEntryListener);
	}
	
	public void setBlockedEntries(List<String> blocked) {
		this.blockedEntries=blocked;
	}
	
	public List<String> getBlockedEntries() {
		return this.blockedEntries;
	}

	/*
	 * New sensor to identify when an unidentified entry has reach the max. num of appearances
	 * */
	
	public interface ILimitReachedEvent{
		public void onLimitReached(String entry, EntryTracker_FSM tracker);
	}
	
	public void sense() {
		sys_U=(RecognizerFSM)this.getSysU();

		sys_U.setInvalidEntryListener(new RecognizerFSM.InvalidEntryListener() {
			
			@Override
			public void onInvalidEntry(AbstractEvent event) {
				// TODO Auto-generated method stub
				System.out.println("Invalid input for current state "+ sys_U.getFSM().getCurrentState().getName() +" from InvalidEntryListener");				
				saveData(new FSMData(sys_U.getFSM().getCurrentState(),event));
				saveDataToFile();
				sendData();

			}
		});
		
		undefinedEntryListener= new UndefinedEntryListener() {
			
			@Override
			public void onUnidentifiedEntry(AbstractEvent event) {
				// TODO Auto-generated method stub
				System.out.println("Unidentified input " +event.getName()+" detected at state "+ sys_U.getFSM().getCurrentState().getName()+" reported from UnidentifiedEntryListener");
				trackUnidentifiedEntries(event.getName());
				saveData(new FSMData(sys_U.getFSM().getCurrentState(),event));
				saveDataToFile();
				sendData();				
			}
		};
		
		sys_U.setUndefinedEntryListener(undefinedEntryListener);
	
		sys_U.setTimerListener(new RecognizerFSM.TimerListener() {
			
			@Override
			public void onTimer(int delay) {
				// TODO Auto-generated method stub
				Timer timer=sys_U.getTimer();
				if (timer==null){
					timer=new Timer(delay,null);
					sys_U.setTimer(timer);
				}
				timer.setDelay(delay);
				timer.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent evt) {
				    	System.out.println(delay+" seconds have passed since K was detected as input");
				    	sys_U.getTimer().stop();
						saveData(new FSMData(sys_U.getFSM().getCurrentState(),new NNEvent("TimeEvent") {
						}));
						saveDataToFile();
						sendData();
				    }
				});
				
				timer.start();
			}
		});
		
	}	

	public void saveDataToFile() {
		 ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		 try {
			mapper.writeValue(Paths.get("Monitor_Data.json").toFile(), this.getData());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * Functionality to track the number of tries an undefined entry has
	 * */
	
	public void trackUnidentifiedEntries(String entry) {
		tracker.addEntry(entry);
		if (tracker.verifyLimit(entry)) {
				this.limitReachedListener.onLimitReached(entry, tracker);
		}
	}

	/*
	 * Functionality to verify if a particular entry has reached the max. num of appearances allowed 
	 * */
	
	public boolean entryBlocked(String entry) {
		boolean result=false;
		for (String blocked: blockedEntries) {
			if(blocked.equalsIgnoreCase(entry)) {
				result=true;
			}
		}
		return result;
	}

}
