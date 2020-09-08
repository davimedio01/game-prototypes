import java.awt.*;

public class Logic {
    public static final Position INITIAL_POSITION_PLAYER1 = new Position(300, 350);
    public static final Position INITIAL_POSITION_PLAYER2 = new Position(700, 350);
    
    private Rectangle hitBoxes[] = {new Rectangle(0, 0, 0, 0), new Rectangle(0, 0, 0, 0)};
    private Position positionVariation[] = {new Position(0, 0), new Position(0, 0)};

    private int[] lifes = {10, 10};

    public void resetPosition(){
        positionVariation[0].x = 0;
        positionVariation[0].y = 0;
        positionVariation[1].x = 0;
        positionVariation[1].y = 0;
    }

    public Position getPosition(int player) {
        return positionVariation[player];
    }

    public void movement(int player, String direction) {
        int dx = 0;
        int dy = 0;

        if(direction.equals("RIGHTWARD")){
            dx = 10;
        }
        
        if(direction.equals("LEFTWARD")){
            dx = -10;
        }
        
        if(direction.equals("UPWARD")){
            dy = -10;
        }
        
        if(direction.equals("DOWNWARD")){
            dy = 10;
        }

        positionVariation[player].x = dx;
        positionVariation[player].y = dy;
    }

    public boolean attack(int player, Position player1, Position player2) {
        hitBoxes[0] = new Rectangle(player1.x + 120, player1.y + 60, 50, 40);
        hitBoxes[1] = new Rectangle(player2.x - 140, player2.y + 30, 45, 70);

        if(hitBoxes[0].intersects(hitBoxes[1])){
            if(lifes[player] > 0){
                lifes[player]--;
            }

            return true;
        }
        
        return false;
    }

    public int getLifes(int player){
        return lifes[player];
    }

    public int[] getLifes(){
        return lifes;
    }

}