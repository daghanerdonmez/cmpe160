public class Bar {
    private final double barHeightScale = Environment.barHeightScale;

    //constructor method of this class does nothing, methods are enough
    Bar(){
    }

    //this method calculates the time difference between when the game started and the moment that its called
    //it is called on every frame of the game
    public double calculateTimeDiff(double startTime, double currentTime){
        return currentTime-startTime;
    }

    //this method calculates the time bar's length according to the time difference calculated previously
    public double calculateBarLength(double timeDiff){
        return Environment.scaleX[1]*(1-(timeDiff/Environment.totalGameDuration));
    }

    //this method determines what the color of the time bar will be according to the time difference calculated previously, it fades from yellow to red smoothly in 40s
    public int greenValue(double timeDiff){
        return (int)(225*(1-(timeDiff/Environment.totalGameDuration)));
    }

    //this method combines all the methods above, draws the bar with the correct color and length at each instant
    public void drawBar(double startTime, double currentTime){
        double timeDiff = calculateTimeDiff(startTime,currentTime);
        double barLength = calculateBarLength(timeDiff);
        StdDraw.setPenColor(255,greenValue(timeDiff),0);
        StdDraw.filledRectangle(barLength/2,-barHeightScale,barLength/2,0.25);
    }
}
