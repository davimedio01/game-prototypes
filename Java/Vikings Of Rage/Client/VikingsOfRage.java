import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class GameArea extends JPanel {
     VikingsOfRage game;
     JDialog timer = new JDialog();
     JLabel info = new JLabel("Aguarde a entrada de outro jogador.");

     Network network;
     Background background = new Background(this);
     Player player = new Player(this, 1); 
     Player enemy = new Player(this, 2);
     PlayerLifes lifes = new PlayerLifes(0, 0);
     
     final static short FRAME_WIDTH = 1000;
     final static short FRAME_HEIGHT = 600;

     boolean gameRunning = false;
     public int x, y, horizontal, vertical;
     
     GameArea(VikingsOfRage game, Network network) {
          this.game = game;
          this.network = network;

          setFocusable(true);
          setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));

          timer.setTitle("Início de Jogo");
          timer.setLayout(new FlowLayout());
          timer.add(new JLabel("Bem-Vindo"));
          timer.add(info);
          timer.setSize(300, 100);
          timer.setLocationRelativeTo(null);
          timer.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
          timer.setAlwaysOnTop(true);
          timer.setVisible(true);

          addKeyListener(new KeyAdapter() {
               public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_W) {
                         network.sendMovement("UPWARD");
                         network.flushOutput();
                    }
                    
                    if (e.getKeyCode() == KeyEvent.VK_S) {
                         network.sendMovement("DOWNWARD");
                         network.flushOutput();
                    }
                    
                    if (e.getKeyCode() == KeyEvent.VK_A) {
                         network.sendMovement("LEFTWARD");
                         network.flushOutput();
                    }
                    
                    if (e.getKeyCode() == KeyEvent.VK_D) {
                         network.sendMovement("RIGHTWARD");
                         network.flushOutput();
                    }
                    
                    if (e.getKeyCode() == KeyEvent.VK_J) {
                         if(!player.movementBlocked()){
                              network.sendAttack(player.getPosition(), enemy.getPosition());
                              network.flushOutput();
                         }
                    }
                    
               }
          });

          new Thread() {
               Position playerPosition = new Position(0, 0);
               Position enemyPosition = new Position(0, 0);

               public void run() {
                    startNetwork();

                    gameRunning = true;
                    
                    String action;

                    while(network.alive()) {

                         action = network.readTypeMessage();
                         network.readPosition(playerPosition, enemyPosition);
                         
                         switch(action){
                              case "COORDINATES":
                                   player.movement(playerPosition);
                                   enemy.movement(enemyPosition);

                                   break;
                              case "HIT":
                                   player.attack();
                                   
                                   network.readLife(lifes);

                                   new Hit(enemy, "HIT").start();
                                   
                                   break;
                              case "ENEMY HIT":
                                   enemy.attack();
                                   
                                   network.readLife(lifes);

                                   new Hit(player, "HIT").start();
                                   
                                   break;
                              case "MISS":
                                   player.attack();

                                   break;
                              case "ENEMY MISS":
                                   enemy.attack();

                                   break;
                              case "WON":
                                   player.attack();

                                   network.readLife(lifes);

                                   new Hit(enemy, "VOCÊ VENCEU!").start();

                                   break;
                              case "ENEMY WON":
                                   enemy.attack();

                                   network.readLife(lifes);

                                   new Hit(enemy, "VOCÊ PERDEU.").start();

                                   break;
                         }

                         repaint();
                    }
               }
          }.start();
     }

     private void startNetwork(){
          Position playerPosition = new Position(0, 0);
          Position enemyPosition = new Position(0, 0);

          network.readPosition(playerPosition, enemyPosition);

          player.initialPosition(playerPosition);
          enemy.initialPosition(enemyPosition);

          for(int i = 10; i >= 1; i--){
               changeInfoDialog("O jogo começa em: " + i + " segundo(s).");
               network.readTypeMessage();
          }

          timer.dispose();
          network.readLife(lifes);
     }

     public void paintComponent(Graphics g) {
          super.paintComponent(g);
          
          background.paint(g);
          
          if(gameRunning){
               player.paint(g);
               enemy.paint(g);

               lifes.paint(g);
          }

          Toolkit.getDefaultToolkit().sync();
          
     }

     public void changeInfoDialog(String info){
          this.info.setText(info);
     }

     class Hit extends Thread{

          Player player;
          String winner;

          Hit(Player player, String winner){
               this.player = player;
               this.winner = winner;
          }

          @Override
          public void run() {
               try {
                    sleep(120);
                    if (winner == "HIT") {
                         player.hit();
                    } else {
                         player.dead();

                         sleep(400);
          
                         gameRunning = false;

                         JOptionPane.showMessageDialog(game, winner, "FINAL DE JOGO", JOptionPane.INFORMATION_MESSAGE);

                         System.exit(0);
                    }

               } catch (InterruptedException e) {}
          }
     }
}



public class VikingsOfRage extends JFrame {
     Network network = new Network(this, "127.0.0.1", 8080);
     GameArea area = new GameArea(this, network);

     public VikingsOfRage() {
          super("Vikings of RAGE");

          setResizable(false);
          setDefaultCloseOperation(EXIT_ON_CLOSE);
          add(area);

          pack();
          
          setLocationRelativeTo(null);
          setVisible(true);
     }

     public static void main(String[] args) {
          new VikingsOfRage();
     }
}