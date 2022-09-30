package org.rossedth.adaptable_adaptive_fsm;
import org.rossedth.adaptive_logic.Action;

public class FSMAction implements Action{

	private String type;
	private String element;
	private String name;
	private String source;
	private String target;
	private String eventType;
	private String eventHandler;
	
	
	public FSMAction() {
	}
	
	public FSMAction(String type, String element, String name) {
		this.type=type;
		this.element=element;
		this.name=name;
	}
	
	public FSMAction(String type, String element, String name, String source, String target, String eventType) {
		this.type=type;
		this.element=element;
		this.name=name;
		this.source=source;
		this.target=target;
		this.eventType=eventType;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getEventHandler() {
		return eventHandler;
	}
	public void setEventHandler(String eventHandler) {
		this.eventHandler = eventHandler;
	}
	
	public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Action");
        sb.append("{type='").append(type).append('\'');
        sb.append(", element='").append(element).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", source='").append(source).append('\'');
        sb.append(", target='").append(target).append('\'');
        sb.append(", event type='").append(eventType).append('\'');
        sb.append(", event handler='").append(eventHandler);
        sb.append("'}");
        return sb.toString();
	}
}