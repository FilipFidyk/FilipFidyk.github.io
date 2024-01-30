/*
 * File:    Broken	.java
 * Created: 7 September 2001
 * Author:  Stephen Jarvis
 */

/*
Preamble Exercise 3 Filip Fidyk
1) Figured out if the robot is north, south or at the latitude of the target, same with east and west.
This was tested via switch statements outputting the correct string statements respective of the functions' outputs
The functions have been altered slightly to the spec, instead of outputting 1 or -1 they just output the correct directions

2)The solution checks first if there are preferred directions or are the x or y coordinates the same as the target's
Then it check if the preferred directions are blocked by walls, and it follows the instructions of the spec
If there are preferred directions but they're blocked, other directions are picked randomly
This ensures that the robot always approaches the target if possible but it doesn't mean it will always reach it.
Long dead end corridors that share a coordinate with the target break this script.
The robot reaches the target coordinate and from there it can only move away by one before returning 
*/

import uk.ac.warwick.dcs.maze.logic.IRobot;

public class Ex3 {
     //A function to figure out whether our robot is north, south or at the latitude of the target
     private int isTargetNorth(IRobot robot){
          if (robot.getLocation().y == robot.getTargetLocation().y)
               return 0;
          else if (robot.getLocation().y > robot.getTargetLocation().y)
               return robot.NORTH;
          return robot.SOUTH;
     }

     //A function to figure out whether our robot is east, west or at the longitude of the target
     private int isTargetEast(IRobot robot){
          if (robot.getLocation().x == robot.getTargetLocation().x)
               return 0;
          else if (robot.getLocation().x > robot.getTargetLocation().x)
               return robot.WEST;
          return robot.EAST;
     }

     //A function that sense the robot's surroundings using absolute directions
     private int lookHeading(IRobot robot, int absoluteDirection){
          robot.setHeading(absoluteDirection);
          return robot.look(IRobot.AHEAD);
     }
     
     //A function to choose a random direction if preferred directions aren't available
     //This code is reused frequently so it makes sense for it to be in a function
     private int randomDirection(IRobot robot){
          int direction;
          do {
               //Find a direction that isn't blocked by a wall
               double rand = Math.random();           

               //All directions are used for when the robot goes past the target
               //This code is used when two directions are unavailable, this maintains equal random probabilities
               if (rand < 0.25)
                    direction = IRobot.WEST;
               else if (rand < 0.5)
                    direction = IRobot.EAST;
               else if (rand < 0.75)
                    direction = IRobot.SOUTH;
               else 
                    direction = IRobot.NORTH;
     
          } while (lookHeading(robot, direction) == IRobot.WALL);
          return direction;
     }

     private int headingController (IRobot robot){
          //Set up an array for the preferred directions so that they can be more easily accessed
          int[] directions = new { isTargetEast(robot), isTargetNorth(robot) };

          //Check if both preferred directions are non-zero so we can pick between the two
          if (directions[0]!=0 && directions[1]!=0){
               //Check east or west for a wall
               if (lookHeading(robot, directions[0]) != IRobot.WALL){
                    //Check north or south for a wall
                    if(lookHeading(robot, directions[1]) != IRobot.WALL){
                         //If both aren't blocked, choose randomly
                         double randno = Math.random();
                         if(randno < 0.5)
                              return directions[0];
                         else 
                              return directions[1];
                    }
                    //Preferred direction along the y-axis is blocked
                    else
                         return directions[0];    //Choose the preferred direction along the x-axis
                    
               }
               //Preferred direction along the x-axis is blocked
               else{
                    if(lookHeading(robot, directions[1]) != IRobot.WALL)
                         return directions[1];    //Choose the preferred direction along the y-axis
                    else     
                         return randomDirection(robot);     //Both preferred directions are blocked, pick randomly                  
               }
          }
          //The robot's y-coordinate is the same as the target's, 
          //so move in the preferred direction along the x-axis
          else if (directions[0] != 0 && directions[1] == 0){
               if (lookHeading(robot, directions[0]) != IRobot.WALL)
                    return directions[0];
               else
                    return randomDirection(robot);
          }
          //The robot's x-coordinate is the same as the target's,
          //so move in the preferred direction along the y-axis
          else if (directions[1] != 0 && directions[0] == 0){     
               if (lookHeading(robot, directions[1]) != IRobot.WALL)
                    return directions[1];
               else
                    return randomDirection(robot); 
          }
          //The robot's coordinates == target's coordinates, this won't ever be reached,
          //but Java wants a return at the end of every possible branch 
          return randomDirection(robot); 
     }

     //Testing code      
     public void reset(){
          ControlTest.printResults();
     }

     public void controlRobot(IRobot robot) {
          int heading = headingController(robot);

          ControlTest.test(heading, robot);       //Also testing code
          robot.setHeading(heading);
  }

}
