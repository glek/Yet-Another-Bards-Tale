package yetanotherbardtale.test;

import static org.junit.Assert.*;

import org.junit.Test;

import yetanotherbardtale.controller.EventFactory;
import yetanotherbardtale.event.*;
import yetanotherbardtale.event.player.*;
import yetanotherbardtale.event.creature.*;
import yetanotherbardtale.event.NonDelegatable.*;
import yetanotherbardtale.controller.CommandWords;

/**
 * These tests ensure that the appropriate events are created from a command
 * and that the appropriate arguments are added.
 * 
 * These tests assume that the CommandWordsTest cases pass which means 
 * that the appropriate CommandWords are assigned to a commandString
 * 
 * TODO I need a way to test if 'look at inventory' works
 * 
 * @author Andrew O'Hara (zalpha314)
 * @version 0.1.0
 */
public class EventFactoryTest {
	private GameEvent event;
	
	private GameEvent parse(String commandString){
		return EventFactory.parseEvent(commandString);
	}

	@Test
	public void testLookAt(){
		event = parse("look at ghost");
		assertTrue("parse 'look at ghost' returns ExamineCommand", event instanceof ExamineCommand);
		assertEquals("ExamineCommand argument is correct", "ghost", ((ExamineCommand)event).getName());
		
		event = parse("look at hairy spider");
		assertTrue("parse 'look at hairy spider' returns ExamineCommand", event instanceof ExamineCommand);
		assertEquals("ExamineCommand argument is correct", "hairy spider", ((ExamineCommand)event).getName());
	}
	
	@Test
	public void testLook(){
		event = parse("look south");
		assertTrue("parse 'look south' returns ExamineCommand", event instanceof ExamineCommand);
		assertEquals("ExamineCommand argument is correct", "south", ((ExamineCommand)event).getName());
		
		event = parse("look over yonder");
		assertTrue("parse 'look over yonder' returns ExamineCommand", event instanceof ExamineCommand);
		assertEquals("ExamineCommand argument is correct", "over yonder", ((ExamineCommand)event).getName());
	}
	
	@Test
	public void testMove(){
		event = parse("move cannon");
		assertTrue("parse 'move cannon' returns MoveCommand", event instanceof MoveCommand);
		assertEquals("MoveCommand argument is correct", "cannon", ((MoveCommand)event).getDirection());
		
		event = parse("move over yonder");
		assertTrue("parse 'move over yonder' returns MoveCommand", event instanceof MoveCommand);
		assertEquals("MoveCommand argument is correct", "over yonder", ((MoveCommand)event).getDirection());
	}
	
	@Test
	public void testPickup(){
		event = parse("pickup shovel");
		assertTrue("Parse 'pickup shovel' returns PickupCommand", event instanceof PickupCommand);
		assertEquals("PickupCommand argument is correct", "shovel", ((PickupCommand)event).getName());
		
		event = parse("pickup energy drink");
		assertTrue("Parse 'pickup energy drink' returns PickupCommand", event instanceof PickupCommand);
		assertEquals("PickupCommand argument is correct", "energy drink", ((PickupCommand)event).getName());
	}
	
	@Test
	public void testDrop(){
		event = parse("drop shovel");
		assertTrue("Parse 'drop shovel' returns DropCommand", event instanceof DropCommand);
		assertEquals("PickupCommand argument is correct", "shovel", ((DropCommand)event).getName());
		
		event = parse("drop empty can");
		assertTrue("Parse 'drope empty can' returns DropCommand", event instanceof DropCommand);
		assertEquals("PickupCommand argument is correct", "empty can", ((DropCommand)event).getName());
	}
	
	@Test
	public void testUseOn(){
		event = parse("use shovel on snow");
		assertTrue("Parse 'use shovel on snow' returns UseItemOnCommand", event instanceof UseItemOnCommand);
		assertEquals("UseItemOnCommand item argument is correct", "shovel", ((UseItemOnCommand)event).getItem());
		assertEquals("UseItemOnCommand target argument is correct", "snow", ((UseItemOnCommand)event).getTarget());
		
		event = parse("use big shovel on snow");
		assertTrue("Parse 'use big shovel on snow' returns UseItemOnCommand", event instanceof UseItemOnCommand);
		assertEquals("UseItemOnCommand item argument is correct", "big shovel", ((UseItemOnCommand)event).getItem());
		assertEquals("UseItemOnCommand target argument is correct", "snow", ((UseItemOnCommand)event).getTarget());
		
		event = parse ("use big shovel on yellow snow");
		assertTrue("Parse 'use big shovel on yellow snow' returns UseItemOnCommand", event instanceof UseItemOnCommand);
		assertEquals("UseItemOnCommand item argument is correct", "big shovel", ((UseItemOnCommand)event).getItem());
		assertEquals("UseItemOnCommand target argument is correct", "yellow snow", ((UseItemOnCommand)event).getTarget());
	}
	
	@Test
	public void testUse(){
		event = parse("use bell");
		assertTrue("Parse 'use bell' returns UseItemCommand", event instanceof UseItemCommand);
		assertEquals("UseItemCommand item argument is correct", "bell", ((UseItemCommand)event).getName());
		
		event = parse("use energy drink");
		assertTrue("Parse 'use energy drink' returns UseItemCommand", event instanceof UseItemCommand);
		assertEquals("UseItemCommand item argument is correct", "energy drink", ((UseItemCommand)event).getName());
	}
	
	@Test
	public void testUndo(){
		event = parse("undo");
		assertTrue("Parse 'undo' returns UndoComamnd", event instanceof UndoCommand);
	}
	
	@Test
	public void testRedo(){
		event = parse("redo");
		assertTrue("Parse 'redo' returns RedoCommand", event instanceof RedoCommand);
	}
	
	@Test
	public void testHelp(){
		event = parse("help");
		assertTrue("Parse 'help' returns HelpCommand", event instanceof HelpCommand);
	}
	
	@Test
	public void testHelpWith(){		
		event = parse("help look at");
		assertTrue("Parse 'help look at' returns HelpWithCommand", event instanceof HelpWithCommand);
		assertEquals("help argument was interpreted correctly", CommandWords.LOOK_AT, ((HelpWithCommand)event).getHelpArg());
		
		event = parse("help look");
		assertEquals("help argument was interpreted correctly", CommandWords.LOOK, ((HelpWithCommand)event).getHelpArg());
		
		event = parse("help move");
		assertEquals("help argument was interpreted correctly", CommandWords.MOVE, ((HelpWithCommand)event).getHelpArg());
		
		event = parse("help pickup");
		assertEquals("help argument was interpreted correctly", CommandWords.PICKUP, ((HelpWithCommand)event).getHelpArg());
		
		event = parse("help drop");
		assertEquals("help argument was interpreted correctly", CommandWords.DROP, ((HelpWithCommand)event).getHelpArg());
		
		event = parse("help use on");
		assertEquals("help argument was interpreted correctly", CommandWords.USE_ON, ((HelpWithCommand)event).getHelpArg());
		
		event = parse("help use");
		assertEquals("help argument was interpreted correctly", CommandWords.USE, ((HelpWithCommand)event).getHelpArg());
		
		event = parse("help undo");
		assertEquals("help argument was interpreted correctly", CommandWords.UNDO, ((HelpWithCommand)event).getHelpArg());
		
		event = parse("help redo");
		assertEquals("help argument was interpreted correctly", CommandWords.REDO, ((HelpWithCommand)event).getHelpArg());
		
		event = parse("help status");
		assertEquals("help argument was interpreted correctly", CommandWords.STATUS, ((HelpWithCommand)event).getHelpArg());
		
		event = parse("help quit");
		assertEquals("help argument was interpreted correctly", CommandWords.QUIT, ((HelpWithCommand)event).getHelpArg());
	}
	
	@Test
	public void testStatus(){
		event = parse("status");
		assertTrue("Parse 'status' returns StatusCommand", event instanceof StatusCommand);
	}
	
	@Test
	public void testQuit(){
		event = parse("quit");
		assertTrue("Parse 'quit' returns QuitCommand", event instanceof QuitCommand);
	}
}
