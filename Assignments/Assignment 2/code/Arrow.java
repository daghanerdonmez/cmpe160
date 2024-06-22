public class Arrow {
    private final int periodOfArrow = Environment.periodOfArrow; //speed const.
    private final String arrowImage = Environment.arrowImage;
    public double posX; //this will be equal to player's posX when an arrow is created
    public double posY = -(Environment.scaleY[1])*(8.0/15.0); //arrow's tip should start from the ground
    public boolean arrowActive = false; //used to determine when to delete the arrow

    Arrow(double posX){
        this.posX = posX; //posX is defined on creation (equal to player's posX)
    }

    //this method checks if the arrow's tip touches the ceiling, deletes if true, moves it one step higher if not
    public void renderIncrementAndDestroy(){
        StdDraw.picture(posX, posY, arrowImage);
        if((posY) > Environment.scaleY[1]*(7.0/15.0)){
            arrowActive = false;
            posY = -(Environment.scaleY[1])*(8.0/15.0);
        }
        posY+=Environment.scaleY[1]/(periodOfArrow/(100.0/3));
    }
}
