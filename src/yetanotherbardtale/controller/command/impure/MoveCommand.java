package yetanotherbardtale.controller.command.impure;

import yetanotherbardtale.entity.Creature;
import yetanotherbardtale.entity.room.Exit;
import yetanotherbardtale.entity.room.Room;
import yetanotherbardtale.event.Notification;
import yetanotherbardtale.event.ModelStateChange;

/** Move a creature from one room to another. */
public class MoveCommand extends ImpureCommand {
    static {
        commonName = "Move";
        description = "Move the creature through an exit with a given name.";
    }

    private Creature creature;
    private Exit exit;
    private Room oldRoom;
    private String direction;

    /** Set up for move by directly referencing an exit. */
    public MoveCommand(Creature creature, Exit exit) {
        if (creature == null)
            throw new NullPointerException("Cannot move a null creature.");
        else if (exit == null)
            throw new NullPointerException("Cannot move to a null exit.");

        this.creature  = creature;
        this.oldRoom   = creature.getCurrentRoom();
        this.exit      = exit;
        this.direction = null;
    }

    /** Set up for move by specying a direction/exit name to be searched at
     * <em>execution time</em>. */
    public MoveCommand (Creature creature,
                        String direction) {
        if (creature == null)
            throw new NullPointerException("Cannot move a null creature.");
        else if (direction == null)
            throw new NullPointerException("Cannot move to a null direction.");

        this.creature  = creature;
        this.oldRoom   = creature.getCurrentRoom();
        this.direction = direction;
        this.exit      = null;
    }

    /**
     * Actually move.
     *
     * Fails if the exit is not in the creature's current room, the exit is
     * locked, or a direction name was specified but no exit exists in that
     * direction in the current room. */
    @Override
    public boolean execute() {
        if (exit == null && oldRoom.containsExit(direction)) {
            exit = oldRoom.getExit(direction);
        } else {
            creature.notifyListeners(
                new Notification(this,
                                 "No " +
                                     direction.toLowerCase() +
                                     " exit.",
                                 Notification.Type.FAILURE));
            return false;
        }
        if(exit.isLocked()) {
            creature.notifyListeners(
                new Notification(this,
                                 "That exit is locked!",
                                 Notification.Type.FAILURE));
            return false;
        }

        // Remove entity from room? No such code in room yet.
        creature.move(direction); // Why was this moved to creature? The idea was to centralize logic.
        creature.notifyListeners(new ModelStateChange(creature));
        return true;
    }

    @Override
    public void unExecute() {
        creature.setRoom(oldRoom);
        creature.notifyListeners(new ModelStateChange(creature));
    }
}
