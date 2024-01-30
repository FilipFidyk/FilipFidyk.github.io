/* Grand Finale Preamble - Filip Fidyk
As it happens my solution from Exercise heavily resembled route B in the guide so I'm deciding
to go with that. To show the path I will create an array that stores the direction that was
chosen when leaving a junction, if the robot backtracks to the junction it is updated. Both 
of those things are done by a conditional in exploreControl. In order to distinguish between
the two, there is another parameter that is used to specify if we were exploring or just 
changed from backtracking.

Moreover both explore and backtrack control function calls were take out of controlTest and
put into a new function, discoverNewRoute, in order to compartmentalise the code away from 
the code used to repeat are solution which is in showSuccess.

To optimise the bot exploreControl and backtrackControl, where given new parameters for non-wall
exits and passage exits, to reduce code duplication

Another idea is to optimise routes, mainly for loopy mazes, where if a junction appears multiple
times the occurence and all junctions until the last occurence are
*/

/*
The final update on the robot was to optimise the robots movement through the maze. Though it 
could be taken further as mentioned in the preamble, i left it at repeating the route that the 
robot formulated would take it to the finish
*/

import java.util.ArrayList;
import java.util.Scanner;
import uk.ac.warwick.dcs.maze.logic.IRobot;

public class GrandFinale{

    private int pollRun = 0;                // Incremented after each pass
    private RobotData robotData;            // Data store for junctions
    private int explorerMode = 1;           
    private int repeatRouteCounter = 0;     //Counter used when repeating our path
    private int initialX, initialY;         //Coordinates where the robot started

    //reset robotData when reset in the maze environment is clicked
    //Also reset poll run, had issues with it and getRuns() resetting when reloading the class but that 
    //might have been another piece of code that I have also removed
    //Works after good testing so if it works don't fix it 
    public void reset(){
        robotData.resetJunctionCounter();
        pollRun = 0;
        explorerMode = 1;
        repeatRouteCounter = 0;
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

    //Checking if the robot is in explorerMode
    private void checkJunctionInfo(IRobot robot){
        if(explorerMode == 1){
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
    private void exploreControl(IRobot robot, int exits, int passageExits, int changeFromBacktrack){
        
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
                //When testing in a blank maze, the robot entered a new junction with only been befores
                //This code turns explorerMode off and lets backtrackControl handle it when it occurs
                //EDIT: This was changed again, this exercise it simply treats those cases as dead ends
                if (exits == elementExits(robot, IRobot.BEENBEFORE)){
                    direction = deadEnd(robot);
                }
                else{
                    //checkJunctionInfo was moved inside this statement to optimise the solution and reduce 
                    //the total amount of calls of this function 
                    checkJunctionInfo(robot);
                    //explorerMode is turned on much later so that the first junction we backtrack
                    //isn't logged into robotData
                    explorerMode = 1;

                    //If we are backtracking we want to change the direction of the robot to 
                    //what it was when it first encountered the junction, so the direction for
                    //later is correct
                    if (changeFromBacktrack == 1 && robotData.getArrivedSize() > 0)
                        robot.setHeading(robotData.searchJunction());

                    //If there is only one passage use it
                    if (passageExits == 1){
                        for (int i = 0; i < 4; i++){
                            if (robot.look(IRobot.AHEAD + i) == IRobot.PASSAGE)
                                direction = IRobot.AHEAD + i;
                        }
                    }
                    //more than 1 passage so pick randomly
                    else
                        direction = pickRandomPath(robot, IRobot.PASSAGE);

                    //Add or update the leaving direction of a junction to robotData
                    if (changeFromBacktrack == 0)
                        robotData.recordLeavingDirection(direction);
                    else
                        robotData.setLeavingDirection(direction);
                }
                break;       
        }
            robot.face(direction);
    }

    private void backtrackControl(IRobot robot, int exits, int passageExits){

        switch (exits){
            case 2:
                robot.face(corridor(robot));
                //if the robot backtracks through the initial coordinates we need to reassign its initial
                //heading direction. only can happen if initial C's are in a corridor
                if (robot.getLocation().x == initialX && robot.getLocation().y == initialY){
                    robotData.setInitialHeading(robot.getHeading());
                }
                break;
            case 3:
            case 4:
                //turn on explorerMode and let exploreControl handle the passages
                if (passageExits > 0){
                    //explorerMode = 1;
                    exploreControl(robot, exits, passageExits, 1);
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

    //Code used to repeat our solution from the first run
    private void showSolution(IRobot robot, int exits, int passageExits){
        int direction = 0;
        if (pollRun == 0){
            robot.setHeading(robotData.getStartHeading());
        }

        //Treat dead ends and corridors the same, but use robotData for copying junction turns
        switch (exits){
            case 1:
                direction = deadEnd(robot);
                break;
            case 2:
                direction = corridor(robot);
                break;
            case 3:
            case 4:
                direction = robotData.getJunctionLeaving(repeatRouteCounter);
                repeatRouteCounter++;
                break;
        }      
        robot.face(direction);
    }

    //Old code from controlTest
    private void discoverNewRoute(IRobot robot, int exits, int passageExits){
        //robot's initial coordinates of the robot are set to change the initial direction if necessary
        if (pollRun == 0){
                robotData = new RobotData(robot.getHeading(), robot.getLocation().x, robot.getLocation().y);
                initialX = robot.getLocation().x;
                initialY = robot.getLocation().y;
        }

        if (explorerMode == 1)
            exploreControl(robot, exits, passageExits, 0);
        else
            backtrackControl(robot, exits, passageExits);
    }


    //Now controlTest is used to pick between finding our route and repeating it
    public void controlRobot(IRobot robot){
        
        int exits = nonWallExits(robot);
        int passageExits = elementExits(robot, IRobot.PASSAGE);
        
        if (robot.getRuns() == 0){
            discoverNewRoute(robot, exits, passageExits);
        }
        else{
            showSolution(robot, exits, passageExits);
        }
        pollRun++;      //Ensures robotData isn't redeclared
    }
}

class RobotData{
    private int junctionCounter;                           // No. of junctions stored
    private int initialHeading;
    private ArrayList<Integer> arrived;                    // Heading of the robot first arrived from
    private ArrayList<Integer> departed;                   // Heading of the robot when it leaves

    public RobotData(int startHeading, int x, int y){
        this.junctionCounter = 0;
        this.initialHeading = startHeading;
        this.arrived = new ArrayList<Integer>();
        this.departed = new ArrayList<Integer>();
    }

    public void resetJunctionCounter(){
        this.junctionCounter = 0;
    }

    //Adds a junction to the arrays
    public void recordJunction(int absoluteDirection){
        this.arrived.add(absoluteDirection);
        this.junctionCounter += 1;
    }

    //Adds the leaving direction to a junction
    public void recordLeavingDirection(int absoluteDirection){
        this.departed.add(absoluteDirection);
    }

    //Change the leaving the direction of a junction, only used when we backtrack and find 
    //a junction with an open passage, will only be used on the most recent junction so we 
    //we dont need an index parameter
    public void setLeavingDirection(int absoluteDirection){
        this.departed.set(junctionCounter - 1, absoluteDirection);
    }
    
    public void setInitialHeading(int absoluteDirection){
        this.initialHeading = absoluteDirection;
    }

    //returns the amount of junctions visited
    public int getJunctionCounter(){
        return this.junctionCounter;
    }

    public int getStartHeading(){
        return this.initialHeading;
    }

    public int getJunctionLeaving(int counter){
        return this.departed.get(counter);
    }

    public int getArrivedSize(){
        return this.arrived.size();
    }

    //Removes the most recent junction and decrements junctionCouter to keep it accurate
    public void removeJunction(){
        this.arrived.remove(junctionCounter - 1);
        this.departed.remove(junctionCounter - 1);
        System.out.println("Junction " + junctionCounter + " was removed");
        junctionCounter--;
    }

    //returns the absolute direction in which the robot approached the junction
    public int searchJunction(){
        //Loop through all junction coordinates
        return this.arrived.get(junctionCounter - 1);
    }
}