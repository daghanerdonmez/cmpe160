import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Environment {
    //general constants
    public static int canvasWidth = 800;
    public static int canvasHeight = 500;
    public static double[] scaleX = {0.0,16.0};
    public static double[] scaleY = {-1.0,9.0};
    public static int totalGameDuration = 40000;
    public static int pauseDuration = 15;
    public static String background = "background.png";
    public static String arrowImage = "arrow.png";
    public static String ballImage = "ball.png";
    public static String playerBack = "player_back.png";
    public static String gameScreen = "game_screen.png";
    public static String bar = "bar.png";
    //player constants
    public static int periodOfPlayer = 6000; //speed const.
    public static double playerHeightWidthRate = 37.0/27.0;
    public static double heightScaleYRate = 1.0/8.0;
    //arrow constants
    public static int periodOfArrow = 1500; //speed const.
    //ball constants
    public static int periodOfBall = 15000;
    public static double heightMultiplier = 1.75;
    public static double radiusMultiplier = 2.0;
    public static double minPossibleHeight = scaleY[1]*heightScaleYRate*1.4;
    public static double minPossibleRadius = scaleY[1]*0.0175;
    public static double gravity = 0.000003 * scaleY[1];
    public static double practicalGravity = gravity*110; //scale the gravity to a practical value, original too small for my code
    //bar constants
    public static double barHeightScale = 0.5;

    //create an arraylist to store all the ball objects in
    public ArrayList<Ball> balls = new ArrayList<>();

    //these variables are used to create two new balls out of the destroyed one
    public boolean deleted = false;
    public int tempLevel;
    public double tempPosX;
    public double tempPosY;

    //these variables are used to track time
    public static double startTime;
    public static double currentTime;

    //Environment is the main method that runs the game. This method is called on the Main class and only this method runs, everything else is called in this method.
    Environment() {

        //game start time to track time passed
        startTime = System.currentTimeMillis();

        //set StdDraw settings
        StdDraw.setCanvasSize(canvasWidth, canvasHeight);
        StdDraw.setXscale(scaleX[0], scaleX[1]);
        StdDraw.setYscale(scaleY[0], scaleY[1]);
        StdDraw.enableDoubleBuffering();

        //create arrow and player objects
        Player player = new Player(scaleX[1]/2,0);
        Arrow arrow = new Arrow(player.posX);

        //create initial ball objects
        Ball initialBall1 = new Ball(2,scaleX[1]/4,scaleY[1]*0.1,0.033,-0.05);
        Ball initialBall2 = new Ball(1,scaleX[1]/3,scaleY[1]*0.1,-0.033,-0.05);
        Ball initialBall3 = new Ball(0,scaleX[1]/4,scaleY[1]*0.1,0.033,-0.05);

        //create bar object
        Bar timebar = new Bar();

        //add the initial balls to the arraylist
        balls.add(initialBall1);
        balls.add(initialBall2);
        balls.add(initialBall3);

        //boolean values to track if the game is over
        boolean gameOver = false;
        boolean win = true;

        //set the fonts for endgame screen
        Font font1 = new Font("Helvetica",Font.BOLD,30);
        Font font2 = new Font("Helvetica",Font.ITALIC,15);

        //this while loop is the main loop of the game, every loop of this while loop is a frame in the game
        while (true) {

            //this part checks if all the bubbles are popped and sets the game over variables accordingly
            boolean allInactive = true;
            for (Ball ball: balls){
                if (ball.isActive){
                    allInactive = false;
                }
            }
            if (allInactive){
                gameOver = true;
                win = true;
            }

            //game over
            if (gameOver){
                //lose
                if(!win) {
                    //draw the endgame screen and write required texts. check y/n
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.picture(scaleX[1]/2, scaleY[1]/2.18, gameScreen,scaleX[1]/3.8,scaleY[1]/4);
                    StdDraw.setFont(font1);
                    StdDraw.text(scaleX[1]/2,scaleY[1]/2,"YOU LOSE!");
                    StdDraw.setFont(font2);
                    StdDraw.text(scaleX[1]/2,scaleY[1]/2.3,"To Replay Click “Y”");
                    StdDraw.text(scaleX[1]/2,scaleY[1]/2.6,"To Quit Click “N”");
                    StdDraw.show();
                    boolean response = false;
                    while(!response) {
                        if (StdDraw.isKeyPressed(KeyEvent.VK_Y)) {
                            Environment game = new Environment();
                            response = true;
                        } else if (StdDraw.isKeyPressed(KeyEvent.VK_N)) {
                            System.exit(0);
                            response = true;
                        }
                    }
                    break;

                }
                //win
                else{
                    //draw the endgame screen and write required texts. check y/n
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.picture(scaleX[1]/2, scaleY[1]/2.18, gameScreen,scaleX[1]/3.8,scaleY[1]/4);
                    StdDraw.setFont(font1);
                    StdDraw.text(scaleX[1]/2,scaleY[1]/2,"YOU WIN!");
                    StdDraw.setFont(font2);
                    StdDraw.text(scaleX[1]/2,scaleY[1]/2.3,"To Replay Click “Y”");
                    StdDraw.text(scaleX[1]/2,scaleY[1]/2.6,"To Quit Click “N”");
                    StdDraw.show();
                    boolean response = false;
                    while(!response) {
                        if (StdDraw.isKeyPressed(KeyEvent.VK_Y)) {
                            Environment game = new Environment();
                            response = true;
                        } else if (StdDraw.isKeyPressed(KeyEvent.VK_N)) {
                            System.exit(0);
                            response = true;
                        }
                    }
                    break;
                }
            }


            currentTime = System.currentTimeMillis(); //variable tracking current time

            StdDraw.clear(StdDraw.WHITE); //clear
            StdDraw.picture(scaleX[1] / 2, scaleY[1] / 2, background, scaleX[1], scaleY[1]); //background

            //arrow part
            if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE) && !arrow.arrowActive){ //if space is pressed and there is no active arrow
                arrow.posX = player.posX; //set the arrow position to the player position
                arrow.arrowActive = true; //activate the arrow
                arrow.renderIncrementAndDestroy(); //render the arrow, move it and check if it needs to be destroyed
            }
            if (arrow.arrowActive){ //if the arrow is active
                arrow.renderIncrementAndDestroy();
            }

            //ball part
            for(Ball ball: balls) { //iterate over all balls (currently active or previously destroyed)
                if (ball.isActive) { //if it is active
                    String collisionResult = (ball.checkCollision(arrow.posX, arrow.posY + (scaleY[1] * (8.0 / 15.0)),player.posX,scaleY[1]*heightScaleYRate,scaleY[1]*heightScaleYRate/playerHeightWidthRate));
                    if ((collisionResult.equals("arrow"))&(arrow.arrowActive)) { //if it collides with an arrow
                        //destroy the ball and arrow
                        ball.isActive = false;
                        arrow.arrowActive = false;
                        arrow.posY = -(Environment.scaleY[1])*(8.0/15.0);
                        deleted = true;
                        tempLevel = ball.level;
                        tempPosX = ball.posX;
                        tempPosY = ball.posY;
                    }
                    if (collisionResult.equals("player")){ //if the ball collides with the player end the game
                        gameOver = true;
                        win = false;
                    }
                    if (!collisionResult.equals("arrow")){
                        ball.show();
                        ball.moveAndAccelerate();
                    }

                }
            }

            //if there is a deleted ball create two new balls at the same location with decreased level
            if((deleted)&(tempLevel!=0)){
                balls.add(new Ball(tempLevel-1,tempPosX,tempPosY,0.05,0.05));
                balls.add(new Ball(tempLevel-1,tempPosX,tempPosY,-0.05,0.05));
                deleted = false;
            }

            //player part
            //move the player according to the keyboard input
            if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)){
                player.move("left");
            }
            else if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)){
                player.move("right");
            }
            player.show();

            //bar part
            StdDraw.picture(scaleX[1] * 0.5, -0.5, bar, scaleX[1], scaleY[1] / 9); //set the bar background
            if((timebar.calculateBarLength(timebar.calculateTimeDiff(startTime,currentTime)))>=0) { //if the bar length is not less than zero
                timebar.drawBar(startTime, currentTime);
            }
            else{ //if the time is up end the game
                gameOver = true;
                win = false;
            }


            StdDraw.show();
            StdDraw.pause(pauseDuration);

        }
    }
}
