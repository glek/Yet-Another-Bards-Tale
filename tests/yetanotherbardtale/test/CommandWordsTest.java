package yetanotherbardtale.test;

import static org.junit.Assert.*;

import org.junit.Test;

import yetanotherbardtale.controller.CommandWords;

/**
 * Unit Tests for the CommandWords class
 * @author Andrew
 * @version 0.1.0
 */
public class CommandWordsTest {
	
	private CommandWords parse(String commandString){
		return CommandWords.parseCommand(commandString);
	}

	@Test
	public void testLookAt(){
		assertEquals("Parse 'look at ghost'", CommandWords.LOOK_AT, parse("look at ghost"));
		assertEquals("Parse 'LOOK at ghost'", CommandWords.LOOK_AT, parse("LOOK at ghost"));
		assertEquals("Parse 'look at'", CommandWords.LOOK, parse("look at"));
	}
	
	@Test
	public void testLook(){
		assertEquals("Parse 'look south'", CommandWords.LOOK, parse("look south"));
		assertEquals("Parse 'LOOK south'", CommandWords.LOOK, parse("LOOK south"));
		assertNull("Parse 'look'", parse("look"));
	}
	
	@Test
	public void testMove(){
		assertEquals("Parse 'move south'", CommandWords.MOVE,parse("move south"));
		assertEquals("Parse 'MOVE south'", CommandWords.MOVE,parse("MOVE south"));
		assertNull("Parse 'move'", CommandWords.parseCommand("move"));
	}
	
	@Test
	public void testPickup(){
		assertEquals("Parse 'pickup shovel'", CommandWords.PICKUP, parse("pickup shovel"));
		assertEquals("Parse 'PICKUP shovel'", CommandWords.PICKUP, parse("PICKUP shovel"));
		assertNull("Parse 'pickup'", parse("pickup"));
	}
	
	@Test
	public void testDrop(){
		assertEquals("Parse 'drop shovel'", CommandWords.DROP, parse("drop shovel"));
		assertEquals("Parse 'DROP shovel'", CommandWords.DROP, parse("DROP shovel"));
		assertNull("Parse 'drop'", parse("drop"));
	}
	
	@Test
	public void testUseOn(){
		assertEquals("Parse 'use shovel on snow'", CommandWords.USE_ON, parse("use shovel on snow"));
		assertEquals("Parse 'USE shovel on snow'", CommandWords.USE_ON, parse("USE shovel on snow"));
		assertEquals("Parse 'USE shovel ON snow'", CommandWords.USE_ON, parse("USE shovel ON snow"));
		//assertEquals("Parse 'use shovel on'", null, parse("use shovel on"));
		//assertNull("Parse 'use on snow'", parse("use on snow"));
		//assertNull("Parse 'use on'", parse("drop"));
		assertNull("Parse 'on snow'", parse("on snow"));
		assertNull("Parse 'on'", parse("on"));
	}
	
	@Test
	public void testUse(){
		assertEquals("Parse 'use shovel'", CommandWords.USE, parse("use shovel"));
		assertEquals("Parse 'USE shovel'", CommandWords.USE, parse("USE shovel"));
		assertNull("Parse 'use'", parse("use"));
	}
	
	@Test
	public void testUndo(){
		assertEquals("Parse'undo'", CommandWords.UNDO, parse("undo"));
		assertEquals("Parse 'UNDO", CommandWords.UNDO, parse("UNDO"));
		assertNull("Parse 'redo all'", parse("redo all"));
		assertNull("Parse 'redo '", parse("redo "));
		assertNull("Parse 'und'", parse("und"));
	}
	
	@Test
	public void testRedo(){
		assertEquals("Parse 'redo'", CommandWords.REDO, parse("redo"));
		assertEquals("Parse 'REDO'", CommandWords.REDO, parse("REDO"));
		assertNull("Parse 'undo all'", parse("undo all"));
		assertNull("Parse 'undo '", parse("undo "));
		assertNull("Parse 'red'", parse("red"));
	}
	
	@Test
	public void testHelp(){
		assertEquals("Parse 'help'", CommandWords.HELP, parse("help"));
		assertEquals("Parse 'HELP'", CommandWords.HELP, parse("HELP"));
		assertNull("Parse 'hel'", parse("hel"));
	}
	
	@Test
	public void testHelpWith(){
		assertEquals("Parse 'help move'", CommandWords.HELP_, parse("help move"));
		assertEquals("Parse 'HELP move'", CommandWords.HELP_, parse("HELP move"));
	}
	
	@Test
	public void testStatus(){
		assertEquals("status'", CommandWords.STATUS, parse("status"));
		assertNull("Parse 'status foo'", parse("status foo"));
		assertNull("Parse 'status '", parse("status "));
		assertNull("Parse 'statu'", parse("statu"));
	}
	
	@Test
	public void testQuit(){
		assertEquals("Parse 'quit'", CommandWords.QUIT, parse("quit"));
		assertEquals("Parse 'QUIT'", CommandWords.QUIT, parse("QUIT"));
		assertNull("Parse 'quit please'", parse("quit please"));
		assertNull("Parse 'quit '", parse("quit "));
		assertNull("Parse 'qui'", parse("qui"));
	}
	
	@Test
	public void testGarbage(){
		assertNull("Parse 'asdf'", parse("asdf"));
		assertNull("Parse ''", parse(""));
		assertNull("Parse ' '", parse (" "));
		assertNull("Parse ' - '", parse(" - "));
		assertNull("Parse 'foo bar baz'", parse("foo bar baz"));
	}
}
