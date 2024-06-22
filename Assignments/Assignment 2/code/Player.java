public class Player {
    private final String playerback = Environment.playerBack;
    public double posX;
    public final double posY;
    private final int periodOfPlayer = Environment.periodOfPlayer; //speed const.
    private final double playerHeightWidthRate = Environment.playerHeightWidthRate; // height/width ratio
    private final double heightScaleYRate = Environment.heightScaleYRate; // height of player/total height

    //constructor method that assigns values
    Player(double posX, double posY){
        this.posX = posX;
        this.posY = posY;
    }

    //this method draws the player on its correct coordinates
    public void show(){
        StdDraw.picture(posX,posY+(Environment.scaleY[1]*heightScaleYRate)/2,playerback,Environment.scaleY[1]*heightScaleYRate/playerHeightWidthRate,Environment.scaleY[1]*heightScaleYRate);
    }

    //this method changes the players position according to keyboard activity
    public void move(String direction){
        if ((direction.equals("left")) & (posX-(Environment.scaleY[1]*heightScaleYRate/playerHeightWidthRate)/2)>0){
            posX -= Environment.scaleX[1]/(periodOfPlayer/30.0);
        }
        else if ((direction.equals("right")) & (posX+(Environment.scaleY[1]*heightScaleYRate/playerHeightWidthRate)/2)<Environment.scaleX[1]){
            posX += Environment.scaleX[1]/(periodOfPlayer/30.0);
        }
    }
}
