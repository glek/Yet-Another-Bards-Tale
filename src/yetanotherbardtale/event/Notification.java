package yetanotherbardtale.event;

/** Something that needs to be displayed to the screen. */
public class Notification extends GameEventObject {
    private String message;
    private Type type;

    public enum Type {
        SUCCESS, FAILURE, INFORMATIONAL
    }
    
    public Notification(Object source, String message, Type type) {
        super(source);

        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public Type getType() {
        return type;
    }
}
