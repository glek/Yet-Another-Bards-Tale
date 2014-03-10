package yetanotherbardtale.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import yetanotherbardtale.controller.CommandWords;
import yetanotherbardtale.entity.GameEntity;
import yetanotherbardtale.event.GameEvent;

/**
 * These test cases for GameEntity ensure that the Event Stacks work properly
 * @author Andrew
 * @version 0.1.0
 */
public class GameEntityTest {	
	private GameEntityExtension entity;
	private GameEvent event1, event2;
	
	private class GameEntityExtension extends GameEntity{		
		@Override public boolean canReverse(GameEvent e) {return false;}
		@Override public void execute(GameEvent e){}
		@Override public boolean unExecute(GameEvent e) { return false; }
		@Override public boolean canExecute(GameEvent e) { return false; }
		
		public void ignoreNextPush() {
	        super.ignoreNextPush();
	    }

	    protected void pushEventOntoStack(GameEvent e) {
	        super.pushEventOntoStack(e);
	    }
	    
	    protected GameEvent getUndoEvent() {
	        return super.getUndoEvent();
	    }
	    
	    protected GameEvent getRedoEvent() {
	        return super.getRedoEvent();
	    }

	    protected boolean undoCommand() {
	        return super.undoCommand();
	    }
	    
	    protected boolean redoCommand() {
	        return super.redoCommand();
	    }

	    protected boolean canUndo() {
	        return super.canUndo();
	    }

	    protected boolean canRedo() {
	        return super.canRedo();
	    }
	}
	
	@Before
	public void setup(){
		entity = new GameEntityExtension();
		event1 = new yetanotherbardtale.event.UndoCommand(CommandWords.UNDO);
		event2 = new yetanotherbardtale.event.RedoCommand(CommandWords.REDO);
	}
	
	@Test
	public void initialTest(){
		assertFalse("Make sure it cannot Undo intially", entity.canUndo());
		assertFalse("Make sure it cannot redo intiallt", entity.canRedo());
	}
	
	@Test
	public void ignorePush(){
		entity.ignoreNextPush();
		entity.pushEventOntoStack(event1);
		
		assertFalse("Make sure it cannot Undo after ignoring first push", entity.canUndo());
		
		assertNull("Make sure that trying to get undo event from empty stack is handled gracefully", entity.getUndoEvent());
		assertNull("Make sure that trying to get redo event from empty stack is handled gracefulle", entity.getRedoEvent());
	}

	@Test
	public void undoRedoOneEvent() {
		entity.pushEventOntoStack(event1);
		
		assertTrue("Make sure it can undo the event that was pushed onto the stack", entity.canUndo());
		assertFalse("Make sure that it cannot redo an event yet", entity.canRedo());
		assertEquals("Verify that the next event to undo is correct", event1, entity.getUndoEvent());
		
		entity.undoCommand();
		
		assertFalse("Make sure that the entity cannot undo any more events", entity.canUndo());
		assertTrue("Make sure that the entity can redo the undone event", entity.canRedo());
		assertEquals("Verify redo event is correct event", event1, entity.getRedoEvent());
		
		entity.redoCommand();
		
		assertTrue("Make sure the entity can undo the event again", entity.canUndo());
		assertFalse("Make sure the entity cannot redo the event anymore", entity.canRedo());
		assertEquals("Verify undo event is correct event", event1, entity.getUndoEvent());
		
	}
	
	@Test
	public void undoRedoTwoEvents(){
		entity.pushEventOntoStack(event1);
		entity.pushEventOntoStack(event2);
		
		assertEquals("Verify that the next event to undo is correct", event2, entity.getUndoEvent());
		assertFalse("Make sure that there is still nothing on redo stact", entity.canRedo());
		
		entity.undoCommand();
		
		assertEquals("After undoing, verify that next undo event is correct", event1, entity.getUndoEvent());
		assertEquals("After undoing, make sure that redo event is correct", event2, entity.getRedoEvent());
		
		entity.undoCommand();
		
		assertFalse("After undoing twice, make sure that cannot undo another event", entity.canUndo());
		assertEquals("After undoing twice, make sure that next redo event is correct", event1, entity.getRedoEvent());
		
		entity.redoCommand();
		
		assertEquals("After redoing one command, verify next undo command is correct", event1, entity.getUndoEvent());
		assertEquals("After redoing one command, verify next redo command is correct", event2, entity.getRedoEvent());
		
		entity.redoCommand();
		
		assertEquals("After redoing both commands, verify next undo command is correct", event2, entity.getUndoEvent());
		assertFalse("After redoing both commands, make sure that you cannot redo another command", entity.canRedo());
	}

}
