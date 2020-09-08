import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;

public class Player{
     JPanel gameArea;

     private final short WIDTH = 200, HEIGHT = 150;

     private Image indicator;
     private Image idle[];
     private Image attack[];
     private Image movement[];
     private Image hit[];
     private Image dead[];

     private final short IDLE_FRAMES = 6;
     private final short ATTACK_FRAMES = 6;
     private final short MOVEMENT_FRAMES = 6;
     private final short HIT_FRAMES = 3;
     private final short DEAD_FRAMES = 4;

     private final Rectangle wall = new Rectangle(10, 205, 990, 210);

     private int player;

     private Position position;
     private String action = "IDLE";
     private short changeFrame = 0;
     private int horizontal;

     Player(JPanel gameArea, int player) {
          this.gameArea = gameArea;
          this.player = player;

          try {
               if (this.player == 1) {
                    indicator = ImageIO.read(new File("Data/Meshes/Player/indicator/player1.png"));

                    horizontal = 1;
               } else {
                    indicator = ImageIO.read(new File("Data/Meshes/Player/indicator/player2.png"));

                    horizontal = -1;
               }

               idle = new Image[IDLE_FRAMES];
               for (int i = 0; i < IDLE_FRAMES; i++) {
                    idle[i] = ImageIO.read(new File("Data/Meshes/Player/idle/idle" + (i + 1) + ".png"));
               }

               attack = new Image[ATTACK_FRAMES];
               for (int i = 0; i < ATTACK_FRAMES; i++) {
                    attack[i] = ImageIO.read(new File("Data/Meshes/Player/attack/attack" + (i + 1) + ".png"));
               }

               movement = new Image[MOVEMENT_FRAMES];
               for (int i = 0; i < MOVEMENT_FRAMES; i++) {
                    movement[i] = ImageIO.read(new File("Data/Meshes/Player/walk/walk" + (i + 1) + ".png"));
               }

               hit = new Image[HIT_FRAMES];
               for (int i = 0; i < HIT_FRAMES; i++) {
                    hit[i] = ImageIO.read(new File("Data/Meshes/Player/hit/hit" + (i + 1) + ".png"));
               }

               dead = new Image[DEAD_FRAMES];
               for (int i = 0; i < DEAD_FRAMES; i++) {
                    dead[i] = ImageIO.read(new File("Data/Meshes/Player/dead/dead" + (i + 1) + ".png"));
               }

          } catch (IOException e) {
               JOptionPane.showMessageDialog(gameArea, "O personagem não pode ser carregado!\n" + e, "Erro",
                         JOptionPane.ERROR_MESSAGE);
               System.exit(1);
          }
     }

     public void initialPosition(Position initial) {
          position = initial;
     }

     public Position getPosition() {
          return position;
     }

     public String returnAction() {
          return action;
     }

     public boolean hitBlocked() {
          return action == "DEAD";
     }

     public boolean attackBlocked() {
          return action == "HIT" || hitBlocked() ;
     }

     public boolean movementBlocked() {
          return action == "ATTACK" || attackBlocked();
     }

     public void dead() {
          action = "DEAD";
          changeFrame = 0;
     }

     public void hit() {
          if (!hitBlocked()) {
               action = "HIT";
               changeFrame = 0;
          }
     }

     public void attack() {
          if (!attackBlocked()) {
               action = "ATTACK";
               changeFrame = 0;
          }
     }

     public void movement(Position variation) {
          if (movementBlocked()) {
               return;
          } else if (variation.x == 0 && variation.y == 0) {
               action = "IDLE";
               return;
          }

          action = "MOVEMENT";

          int newPosition;

          if (horizontal == 1) {
               newPosition = position.y + variation.y;

               if (variation.y != 0 && wall.contains(position.x, newPosition)) {
                    position.y = newPosition;
               }

               newPosition = position.x + variation.x;

               if (variation.x > 0 && wall.contains(newPosition + WIDTH, position.y)) {
                    position.x = newPosition;
               } else if (variation.x < 0 && wall.contains(newPosition, position.y)) {
                    position.x = newPosition;
               }
          } else {
               newPosition = position.y + variation.y;

               if (variation.y != 0 && wall.contains(position.x, newPosition)) {
                    position.y = newPosition;
               }

               newPosition = position.x - variation.x;

               if (variation.x > 0 && wall.contains(newPosition - WIDTH, position.y)) {
                    position.x = newPosition;
               } else if (variation.x < 0 && wall.contains(newPosition, position.y)) {
                    position.x = newPosition;
               }
          }
     }

     public void paint(Graphics g){
          if (action == "DEAD") {
               if (changeFrame >= DEAD_FRAMES) {
                    changeFrame = 0;
               }
               
               g.drawImage(dead[changeFrame], position.x, position.y, WIDTH * horizontal, HEIGHT, gameArea);
          } else if (action == "HIT") {
               if (changeFrame >= HIT_FRAMES) {
                    action = "IDLE";
                    changeFrame = 0;
               }

               g.drawImage(hit[changeFrame], position.x, position.y, WIDTH * horizontal, HEIGHT, gameArea);
          } else if (action == "ATTACK") {
               if (changeFrame >= ATTACK_FRAMES) {
                    action = "IDLE";
                    changeFrame = 0;
               }

               g.drawImage(attack[changeFrame], position.x, position.y, WIDTH * horizontal, HEIGHT, gameArea);
          } else if (action == "MOVEMENT") {
               if (changeFrame >= MOVEMENT_FRAMES) {
                    changeFrame = 0;
               }

               g.drawImage(movement[changeFrame], position.x, position.y, WIDTH * horizontal, HEIGHT, gameArea);
          } else {
               if (changeFrame >= IDLE_FRAMES) {
                    changeFrame = 0;
               }

               g.drawImage(idle[changeFrame], position.x, position.y, WIDTH * horizontal, HEIGHT, gameArea);
          }

          if (horizontal == 1) {
               g.drawImage(indicator, position.x + 45, position.y + 15, gameArea);
          } else {
               g.drawImage(indicator, position.x - 145, position.y + 15, gameArea);
          }


          changeFrame++;
     }
}