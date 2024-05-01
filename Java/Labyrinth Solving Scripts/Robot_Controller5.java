/* Exercise 2 Preamble - Filip Fidyk
In order to not use coordinates I adopted a removal of junctions idea. Essentially, if  all paths of a 
junction were exhausted and we had to backtrack through then its entry in robotData would be removed 
and the junctionCounter would be decremented.

Any uses of coordinates in the previous exercise were either completely removed or edited so that 
junctionCounter was utilised. In theory we should always be interacting with the most recent
junction thus using junctionCounter is possible but the editing of junctionCounter has to be very rigorous
*/

/*
Iteration on the previous controller where coordinates are no longer used and junctions are 
remembered solely on the order in the robots path. If a junction was backtracked through it was
removed from the robots memory
*/

import java.util.ArrayList;
import uk.ac.warwick.dcs.maze.logic.IRobot;

public class Robot_Controller5{

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

    //Checking if the robot is at a new junction, if so add it to robotData
    private void checkJunctionInfo(IRobot robot, int beenBefores){
        if (beenBefores == 1){
            robotData.recordJunction(robot.getHeading());
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
                checkJunctionInfo(robot, elementExits(robot, IRobot.BEENBEFORE));

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
        //deadEnd() changes explorerMode though so I made it a special case.
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
                else{
                    robot.setHeading(flipDirection(robotData.searchJunction()));
                    robotData.removeJunction();
                }
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
    private int junctionCounter;                             // No. of junctions stored
    private ArrayList<Integer> arrived;                      // Heading the robot first arrived from

    public RobotData(){
        this.junctionCounter = 0;
        this.arrived = new ArrayList<Integer>();
    }

    public void resetJunctionCounter(){
        this.junctionCounter = 0;
    }

    //Adds a junction to the arrays
    public void recordJunction(int absoluteDirection){
        this.arrived.add(absoluteDirection);
        this.junctionCounter += 1;
    }

    //returns the amount of junctions visited
    public int getJunctionCounter(){
        return this.junctionCounter;
    }

    //Removes the most recent junction and decrements junctionCouter to keep it accurate
    public void removeJunction(){
        this.arrived.remove(junctionCounter - 1);
        System.out.println("Junction " + junctionCounter + " was removed");
        junctionCounter--;
    }

    //returns the absolute direction in which the robot approached the junction
    public int searchJunction(){
        //Loop through all junction coordinates
        return this.arrived.get(junctionCounter - 1);
    }
}