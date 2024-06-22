public class Ball {
    //getting needed constants and variables from Environment class
    private final String ballImage = Environment.ballImage;
    private final int periodOfBall = Environment.periodOfBall;
    private final double heightMultiplier = Environment.heightMultiplier;
    private final double radiusMultiplier = Environment.radiusMultiplier;
    private final double minPossibleHeight = Environment.minPossibleHeight;
    private final double minPossibleRadius = Environment.minPossibleRadius;
    private final double practicalGravity = Environment.practicalGravity;

    //a ball has a level x,y coords and velocities
    public int level;
    public double posX;
    public double posY;
    public double velX;
    public double velY;

    //it also has a radius, max height that it can reach and a vertical max velocity component to reach that height (sqrt(2gh))
    public double radius;
    public double maxHeight;
    public double normalVelocity;

    public boolean isActive = true;

    //constructor method that calculates and assigns the variables above
    Ball(int level, double posX, double posY, double velX, double velY){
        this.level = level;
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;

        this.radius = minPossibleRadius*Math.pow(radiusMultiplier,level);
        this.maxHeight = minPossibleHeight*Math.pow(heightMultiplier,level);
        this.normalVelocity = Math.sqrt(2*practicalGravity*maxHeight);
    }

    //this will happen at each while loop, the ball will move and accelerate according to its state
    public void moveAndAccelerate() {
        posX += velX;
        posY += velY;
        velY -= practicalGravity; //gravity acts as a negative value
    }

    //checks if the ball collides with the side walls, the floor or with the arrow. returns corresponding collision type
    public String checkCollision(double arrowX, double arrowY, double playerPosX, double playerPosY, double playerWidth){
        //check if the ball has a collision with the side walls
        if ((posX-radius < 0) || (posX+radius) > Environment.scaleX[1]){
            velX = -velX;
            return "sidewall";
        }
        //check if the ball has a collision with the floor
        else if(posY-radius < 0){
            velY = normalVelocity;
            return "floor";
        }
        //check if the ball has a collision with the arrow
        else if(((posY < arrowY)&&(Math.abs(posX-arrowX)<radius)) || (Math.sqrt((Math.pow(Math.abs(posY-arrowY),2))+(Math.pow(Math.abs(posX-arrowX),2)))<radius)){
            return "arrow";
        }
        //there are four different options for the ball to collide with the players hitbox
        //top
        else if((posY < playerPosY) && (Math.abs(posX-playerPosX)<(radius+playerWidth/2))){
            return "player";
        }
        //topright
        else if((posX > playerPosX)&&(Math.sqrt((Math.pow(Math.abs(posY-playerPosY),2))+(Math.pow(Math.abs(posX-playerPosX-playerWidth/2),2)))<radius)){
            return "player";
        }
        //topleft
        else if((posX < playerPosX)&&(Math.sqrt((Math.pow(Math.abs(posY-playerPosY),2))+(Math.pow(Math.abs(playerPosX-playerWidth/2-posX),2)))<radius)){
            return "player";
        }
        //sides
        else if(((posX<playerPosX+playerWidth/2)&&(posX>playerPosX-playerWidth/2))&&((posY-radius)<playerPosY)){
            return "player";
        }
        else{
            return "none"; //no collision at that moment
        }
    }

    //draw the ball and rescale it according to its radius
    public void show(){
        StdDraw.picture(posX,posY,ballImage,2*radius,2*radius);
    }

}
