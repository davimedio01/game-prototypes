import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;

public class PlayerLifes {
     int player1;
     int player2;

     public PlayerLifes(int player1, int player2) {
          this.player1 = player1;
          this.player2 = player2;
     }

     public void paint(Graphics g){
          if(player1 > 0){
               g.setColor(Color.WHITE);
               g.fillRect(0, 5, 100, 20);
               g.setColor(Color.BLUE);
               g.drawString("VIDA ATUAL: " + player1, 2, 20);
          }
          if(player2 > 0){
               g.setColor(Color.WHITE);
               g.fillRect(GameArea.FRAME_WIDTH - 100, 5, 100, 20);
               g.setColor(Color.RED);
               g.drawString("VIDA ATUAL: " + player2, GameArea.FRAME_WIDTH - 98, 20);
          }
     }

}