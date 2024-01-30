/*
* File: DumboController.java
* Created: 17 September 2002, 00:34
* Author: Stephen Jarvis
*/

/*
Exercise 1 preamble by Filip Fidyk
1) To avoid collisions we can only let the robot move when it isn't facing a wall
   This is will bedone via looping the randomising of the direction until the robot faces either a passage or a been before
2) We also want to log the movements of the robot through different elements of the maze.
   This will be done by incrementally creating a string through conditional statements
   and comparing relative directions of the robot to its initial absolute direction 
*/

/*
Github update all the files in this folder are scripts that are meant to control a robot through a maze

This one is to use randomness but avoid colliding into walls
*/

import uk.ac.warwick.dcs.maze.logic.IRobot;

public class Ex1
{

	public void controlRobot(IRobot robot) {

		int randno;
		int direction;
		//gets the direction of the robot at the start and after every movement 
		int initialDirection = robot.getHeading();

		// a do while loop to change the direction and keep doing it until not facing a wall
		do{
		randno = (int) Math.round(Math.random()*3); // Select a random number

		//Ensures that when logging the movement it will be correct with what the screen shows
		robot.setHeading(initialDirection);

		// Convert the random number to a direction
		if (randno == 0)
		direction = IRobot.LEFT;
		else if (randno == 1)
		direction = IRobot.RIGHT;
		else if (randno == 2)
		direction = IRobot.BEHIND;
		else 
		direction = IRobot.AHEAD;

		robot.face(direction); // Face the robot in this direction 
		} while (robot.look(IRobot.AHEAD) == IRobot.WALL);

		String statement = "I'm going "; //a string variable to build up the log statement
		
		//a switch statement to log the direction in which the robot is moving, with the use of the relative directions
		switch (direction){
			case IRobot.AHEAD:
				statement += "forward ";
				break;
			case IRobot.BEHIND:
				statement += "backwards ";
				break;
			case IRobot.RIGHT:
				statement += "right ";
				break;
			case IRobot.LEFT:
				statement += "left ";
				break;	
		}

		//Figure out if the robot is in a corridor, a dead end or at a junction and add it to the statement
		int walls = 0;
		for (int i = 0; i < 4; i++){
			if (robot.look(IRobot.AHEAD + i) == IRobot.WALL){
				walls++;
			}
		}
		
		switch (walls){
			case 0 : 
				statement += "at a crossroads";
				break;
			case 1 : 
				statement += "at a junction";
				break;
			case 2 : 
				statement += "down a corridor";
				break;
			case 3 : 
				statement += "at a dead end";
				break;
			default:
				statement = "I'm stuck";
		} 
		System.out.println(statement);
	}

}