/*
* File: DumboController.java
* Created: 17 September 2002, 00:34
* Author: Stephen Jarvis
*/

/*
Exercise 2 preamble by Filip Fidyk
1)To ensure equal probabilities the round function and multiplier were removed and the raw output from the random function was kept
This range of 0 to 1 was split into 4 segments with exclusive upper bounds namely: 0-0.25, 0.25-0.5, 0.5-0.75 and 0.75-1 
And the 4 directions have been attached to these segments via altering the if statement that sets the direction variable
2)A 1 in 8 chance is added for changing directions via a logical or operator in the if statement that decides if we change direction
This is done via the random function and if the output is less than 0.125 then the random change of direction happens
*/

import uk.ac.warwick.dcs.maze.logic.IRobot;

public class Ex2
{

	public void controlRobot(IRobot robot) {

		double randno;
		double randChance = Math.random();
		int direction; 
		int initialDirection = robot.getHeading(); //gets the direction of the robot at the start and after every movement

		//Decide whether to change direction or not, depends if the robot is facing a wall or on chance 
		if (robot.look(IRobot.AHEAD) == IRobot.WALL || randChance < 0.125){
			// a do while loop to change the direction and keep doing it until not facing a wall
			do{
			randno = Math.random(); // Select a random number

			//Ensures that when logging the movement it will be correct with what the screen shows
			robot.setHeading(initialDirection);

			// Convert the random number to a direction
			if (randno < 0.25)
			direction = IRobot.LEFT;
			else if (randno < 0.5)
			direction = IRobot.RIGHT;
			else if (randno < 0.75)
			direction = IRobot.BEHIND;
			else 
			direction = IRobot.AHEAD;

			// Face the robot in this direction 
			robot.face(direction); 
			} while (robot.look(IRobot.AHEAD) == IRobot.WALL);
		}
		else
			direction = IRobot.AHEAD; //if not facing a wall then we keep the direction in which the robot was going

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