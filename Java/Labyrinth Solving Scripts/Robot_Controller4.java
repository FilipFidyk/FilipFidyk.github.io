/* Exercise 1 Preamble - Filip Fidyk
Starting with explorerControl and backtrackControl. The robot starts in explore mode in order to 
reach a junction, only in explorer mode we check if we can add a junction to robotData because checking
to do so in backtracking is pointless. Its way of handling deadends, corridors and junctions mostly follows
the guide. Dead ends cause the robot to go into backtracking mode except for when it spawns in a dead end.
Junctions and crossroads are treated the same but there is no function because explore and backtrack modes 
treat them quite differently. For this reason the dead end functionality could have been coded in without
a function but i found it convenient to not have it obfuscate the screen. Also backtrack control can be 
activated a different way as shown by testing in a blank maze. Essentially, if the robot enters a new 
junction but all paths have been explored it is activated.

Backtrack control firstly can't handle deadends and this is because, in theory, it never should. Corridors
are treated normally, meanwhile for junctions the robot uses robotData to return from the direction it came
from. Also, it can activate explore mode when it returns to a former junction and there is a unexplored
passage.

robot.getRuns() has been removed when checking for the first square as it caused issues when debugging
the explorer after a semantic problem has been fixed. Possibly could have used an or operator in the check
as opposed to an and but this worked anyway.
passageExits() was changed so that a path could be selected depending on which element we wanted. It was
very useful in earlier parts of Exercise 1 but later on it wasn't as necessary due to the backtracking,
still passage and beenbefore uses are used so it wasn't reverted 

Implementation of robotData is very similar to the on in the guide but instead of arrays ArrayLists are
used so that we don't reserve unecessary storage and maxJunctions was removed. A way to improve it is 
possibly an array of coordinate objects as opposed to two integer arraylists.

Also I believe I did a good job limiting the amount of repeated blocks of code, although i did stick quite
close to the guide any major optimisations didn't occur. Worth noting this code can't solve loopy mazes.

Worst case Scenario: the robot can still explore the entire maze before reaching the goal. Assume a junction
at the beginning where one branch leads straight to the goal and the other is extremely long but doesn't lead 
anywhere. The robot will explore of it unless there is a loop near the initial junction and it gets lucky.
If you imagine the maze as a tree there is one path that leads to the goal. All those squares will be 
counted once, meanwhile every other square in other branches will be counted twice 
*/

/*
This controller was written from scratch and uses a different approach to exploring the maze
it uses randomness only at junctions and crossroads and remembers the turn it took in the
robotData class so when a dead end is reached it starts backtracking until a junction where
there still are unexplored passages. Individual junctions are remembered through the use of
coordinates
*/

import java.util.ArrayList;
import uk.ac.warwick.dcs.maze.logic.IRobot;

public class Robot_Controller4{

    private int pollRun = 0;        // Incremented after each pass
    private RobotData robotData;    // Data store for junctions
    private int explorerMode;

    //reset robotData when reset in the maze environment is clicked
    //Also reset poll run, had issues with it and getRuns() resetting when reloading the class but that 
    //might have been another piece of code that I have also removed
    //Works after good testing so if it works don't fix it 
    public void reset(){
        robotData.resetJunctionCounter();
        pollRun = 0;
    }

    //Check around the robot for passages or been befores
    private int nonWallExits(IRobot robot){
        int nonWallExits = 0;

        for (int i = 0; i < 4; i++ ){
            if (robot.look(IRobot.AHEAD + i) != IRobot.WALL){
                nonWallExits += 1;
            }
        }
        return nonWallExits;
    }

    //Check around the robot for whatever maze element via the second parameter
    private int elementExits(IRobot robot, int mazeElement){
        int passageExits = 0;

        for (int i = 0; i < 4; i++ ){
            if (robot.look(IRobot.AHEAD + i) == mazeElement){
                passageExits++;
            }
        }
        return passageExits;
    }
    
    //Used only for junctions and crossroads, so all directions have to be a possibility
    //Similar approach as above to specify what element we want to find
    private int pickRandomPath(IRobot robot, int mazeElement){
        int direction;
        
        do{
            double rand = Math.random();
            if (rand < 0.25)
                direction = IRobot.AHEAD;
            else if (rand < 0.5)
                direction = IRobot.RIGHT;
            else if (rand < 0.75)
                direction = IRobot.BEHIND;
            else 
                direction = IRobot.LEFT;
        
        } while(robot.look(direction) != mazeElement);

        return direction;
    }

    //Checking if the x and y coordinates match any of the previous junctions, if not add to robotData
    private void checkJunctionInfo(IRobot robot){
        int recorded = 0;
        for (int i = 1; i<=robotData.getJunctionCounter(); i++){
            //Uses object methods to pull out the coordinates and compares them to all past junctions
            if (robot.getLocation().x == robotData.getJunctionX(i) && robot.getLocation().y == robotData.getJunctionY(i)){
                recorded = 1;
                break;
            }
        }

        //If a match isn't found it is added
        if (recorded == 0){
            robotData.recordJunction(robot.getLocation().x, robot.getLocation().y, robot.getHeading());
            printJunctionInfo(robot);
        }
    }

    //A function to develop the junction info line and print it
    //In a different function even though it isn't reused to easily take this code out when necessary
    //and improve the readability of the function above
    private void printJunctionInfo(IRobot robot){
        String test = "Junction " + robotData.getJunctionCounter() + " (x=" + robot.getLocation().x + ", y=" + robot.getLocation().y + ") heading ";
        if (robot.getHeading() == IRobot.NORTH)
            test += "NORTH";
        else if (robot.getHeading() == IRobot.EAST)
            test += "EAST";
        else if (robot.getHeading() == IRobot.WEST)
            test += "WEST";
        else  
            test += "SOUTH";

        System.out.println(test);
    }

    private int flipDirection(int dir){
        if (dir == IRobot.NORTH)
            return IRobot.SOUTH;
        else if (dir == IRobot.SOUTH)
            return IRobot.NORTH;
        else if (dir == IRobot.EAST)
            return IRobot.WEST;
        else    
            return IRobot.EAST;

    }

    //Can't just reverse the direction as the robot may be initialised in a dead end, where AHEAD and BEHIND are walls
    //but we don't want to loop as this will only be the case once
    //EDIT: explorerMode is switched to 0 to begin backtracking
    private int deadEnd(IRobot robot){  
        
        if(pollRun != 0)
            explorerMode = 0;

        if (robot.look(IRobot.BEHIND) != IRobot.WALL)
            return IRobot.BEHIND;
        else if (robot.look(IRobot.RIGHT) != IRobot.WALL)
            return IRobot.RIGHT;
        else if (robot.look(IRobot.LEFT) != IRobot.WALL)
            return IRobot.LEFT; 
        return IRobot.AHEAD;
    }

    //Keep going down the corridor and only turn left or right
    //Even when initialising we will be able to go a direction that isnt behind
    //And then this function will never be called when the robot needs to turn back
    private int corridor(IRobot robot){
        if (robot.look(IRobot.AHEAD) == IRobot.WALL){
            for (int i = 1; i < 4; i += 2){
                if (robot.look(IRobot.AHEAD + i) != IRobot.WALL)
                    return (IRobot.AHEAD + i);
            }
        }
        return IRobot.AHEAD;
    }

    //The function is used when explorerMode is active 
    private void exploreControl(IRobot robot, int passageExits){
        int exits = nonWallExits(robot);
        int direction = 0;

        switch (exits){
            case 1:
                direction = deadEnd(robot);
                break;
            case 2:
                direction = corridor(robot);
                break;
            case 3:
            case 4:
                checkJunctionInfo(robot);

                //When testing in a blank maze, the robot entered a new junction with only been befores
                //This code turns explorerMode off and lets backtrackControl handle it whem it occurs
                if (exits == elementExits(robot, IRobot.BEENBEFORE)){
                    explorerMode = 0;
                    backtrackControl(robot, passageExits);
                }
                else{
                    //only one passage use it
                    if (passageExits == 1){
                        for (int i = 0; i < 4; i++){
                            if (robot.look(IRobot.AHEAD + i) == IRobot.PASSAGE)
                                direction = IRobot.AHEAD + i;
                        }
                    }
                    //more than 1 passage so pick randomly
                    else
                        direction = pickRandomPath(robot, IRobot.PASSAGE);
                }
                break;       
        }

        //We want to only use this when backtrackControl isn't necessary so when we are in explorerMode
        //deadEnd() changes deactivates explorerMode so I made a special case for it.
        if (exits == 1 || explorerMode == 1)
            robot.face(direction);
    }

    private void backtrackControl(IRobot robot, int passageExits){
        int exits = nonWallExits(robot);

        switch (exits){
            case 2:
                robot.face(corridor(robot));
                break;
            case 3:
            case 4:
                //turn on explorerMode and let exploreControl handle the passages
                if (passageExits > 0){
                    explorerMode = 1;
                    exploreControl(robot, passageExits);
                }
                //Here we use search junction to get the direction we entered a junction. flip it 
                //and point the robot
                else
                    robot.setHeading(flipDirection(robotData.searchJunction(robot.getLocation().x, robot.getLocation().y)));
                break;
        
        }
    }

    public void controlRobot(IRobot robot){
        //Creates a robotData object only at the start of the program
        //EDIT: Initialises the explorer mode of the robot
        if (pollRun == 0){
            robotData = new RobotData();
            explorerMode = 1;
        }

        int passageExits = elementExits(robot, IRobot.PASSAGE);

        if (explorerMode == 1)
            exploreControl(robot, passageExits);
        else
            backtrackControl(robot, passageExits);

        pollRun++;      //Ensures robotData isn't redeclared
    }
}

class RobotData{
    private int junctionCounter;                      // No. of junctions stored
    private ArrayList<Integer> juncX;                 // X-coordinates of the junctions
    private ArrayList<Integer> juncY;                 // Y-coordinates of the junctions
    private ArrayList<Integer> arrived;               // Heading the robot first arrived from

    public RobotData(){
        this.junctionCounter = 0;
        this.juncX = new ArrayList<Integer>();
        this.juncY = new ArrayList<Integer>();
        this.arrived = new ArrayList<Integer>();
    }

    public void resetJunctionCounter(){
        this.junctionCounter = 0;
    }

    //Adds a junction to the arrays
    public void recordJunction(int x, int y, int absoluteDirection){
        this.juncX.add(x);
        this.juncY.add(y);
        this.arrived.add(absoluteDirection);
        this.junctionCounter += 1;
    }

    //returns the amount of junctions visited
    public int getJunctionCounter(){
        return this.junctionCounter;
    }
    
    //returns the x coordinate of the needed junction
    public int getJunctionX(int junctionNum){
        return this.juncX.get(junctionNum - 1);
    }

    //returns the y coordinate of the needed junction
    public int getJunctionY(int junctionNum){
        return this.juncY.get(junctionNum - 1);
    }

    //returns the absolute direction in which the robot approached the junction
    public int searchJunction(int x, int y){
        //Loop through all junction coordinates
        for (int i = 0; i<this.juncX.size();i++){
            //If we find it return it
            if (x == this.juncX.get(i) && y == this.juncY.get(i))
                return this.arrived.get(i);
        }
        //if we dont find it, return 0 and deal with it in the backtrackingControl
        return 0;
    }
}