import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Player {
     // Component (Context)
     private Component parentComponent;

     // Número do Jogador (1 ou 2)
     private final int numberOfPlayer;

     // Tamanho
     private final short WIDTH = 200, HEIGHT = 150;

     // Posições
     private int posX, posY;
     private final int moveX = 10, moveY = 10; // Velocidade de Movimento
     private int horizontalDirection;
     private int verticalDirection;
     // Limites (Paredes)
     private final int LIMIT_X_LEFT = 60;
     private final int LIMIT_X_RIGHT = 940;
     private final int LIMIT_Y_TOP = 210;
     private final int LIMIT_Y_BOTTOM = 415;

     // Renderização
     // Indicador
     private Image indicator;

     // Parado
     private Image idle[];
     private final short IDLE_FRAMES = 6;
     private short changeIdleFrame;

     // Ataque
     private Image attack[];
     private final short ATTACK_FRAMES = 6;
     private short changeAttackFrame;

     // Movimento
     private Image movement[];
     private final short MOVEMENT_FRAMES = 6;
     private short changeMovementFrame;

     // Sofreu Dano
     private Image hit[];
     private final short HIT_FRAMES = 3;
     private short changeHitFrame;

     // Morte
     private Image dead[];
     private final short DEAD_FRAMES = 4;
     private short changeDeadFrame;

     // Estado da Renderização
     private int state;

     // Lógica
     private int currentLife;

     // Construtor
     Player(Component parentComponent, int numberOfPlayer, int initialPosX, int initialPosY) {
          this.parentComponent = parentComponent;
          this.numberOfPlayer = numberOfPlayer;
          // Recuperando imagens
          try {
               // Indicador
               if (this.numberOfPlayer == 1) {
                    indicator = ImageIO.read(new File("Data/Meshes/Player/indicator/player1.png"));
               } else if (this.numberOfPlayer == 2) {
                    indicator = ImageIO.read(new File("Data/Meshes/Player/indicator/player2.png"));
               }
               // Parado
               idle = new Image[IDLE_FRAMES];
               for (int i = 0; i < IDLE_FRAMES; i++) {
                    idle[i] = ImageIO.read(new File("Data/Meshes/Player/idle/idle" + (i + 1) + ".png"));
               }
               // Ataque
               attack = new Image[ATTACK_FRAMES];
               for (int i = 0; i < ATTACK_FRAMES; i++) {
                    attack[i] = ImageIO.read(new File("Data/Meshes/Player/attack/attack" + (i + 1) + ".png"));
               }
               // Movimento
               movement = new Image[MOVEMENT_FRAMES];
               for (int i = 0; i < MOVEMENT_FRAMES; i++) {
                    movement[i] = ImageIO.read(new File("Data/Meshes/Player/walk/walk" + (i + 1) + ".png"));
               }
               // Sofreu Dano
               hit = new Image[HIT_FRAMES];
               for (int i = 0; i < HIT_FRAMES; i++) {
                    hit[i] = ImageIO.read(new File("Data/Meshes/Player/hit/hit" + (i + 1) + ".png"));
               }
               // Morte
               dead = new Image[DEAD_FRAMES];
               for (int i = 0; i < DEAD_FRAMES; i++) {
                    dead[i] = ImageIO.read(new File("Data/Meshes/Player/dead/dead" + (i + 1) + ".png"));
               }

          } catch (IOException e) {
               JOptionPane.showMessageDialog(parentComponent, "O personagem não pode ser carregado!\n" + e, "Erro",
                         JOptionPane.ERROR_MESSAGE);
               System.exit(1);
          }

          // Posições Iniciais
          posX = initialPosX;
          posY = initialPosY;

          // Iniciando as variações
          changeIdleFrame = 0;
          changeAttackFrame = 0;
          changeMovementFrame = 0;
          changeHitFrame = 0;
          changeDeadFrame = 0;

          // Movimento
          state = 0;
          if (this.numberOfPlayer == 1) {
               horizontalDirection = 1;
          } else {
               horizontalDirection = -1;
          }
          verticalDirection = 1;

          // Vida
          currentLife = 10;
     }

     // Pintar Estado (Movimento/Ataque)
     public void paintState(Graphics g) {
          // Parado
          if (state == 0) {
               changeAttackFrame = 0;
               changeMovementFrame = 0;
               changeHitFrame = 0;

               g.drawImage(idle[changeIdleFrame], posX - horizontalDirection * 83, posY, WIDTH * horizontalDirection,
                         HEIGHT, parentComponent);

               changeIdleFrame++;

               if (changeIdleFrame >= IDLE_FRAMES) {
                    changeIdleFrame = 0;
               }
          }
          // Morte
          else if (state == 1) {
               changeMovementFrame = 0;
               changeIdleFrame = 0;
               changeAttackFrame = 0;
               changeHitFrame = 0;

               g.drawImage(dead[changeDeadFrame], posX - horizontalDirection * 83, posY, WIDTH * horizontalDirection,
                         HEIGHT, parentComponent);

               changeDeadFrame++;

               if (changeDeadFrame >= DEAD_FRAMES) {
                    changeDeadFrame = DEAD_FRAMES - 1;
                    state = -1;
               }
          }
          // Hit
          else if (state == 2) {
               changeMovementFrame = 0;
               changeIdleFrame = 0;
               changeAttackFrame = 0;

               g.drawImage(hit[changeHitFrame], posX - horizontalDirection * 83, posY, WIDTH * horizontalDirection,
                         HEIGHT, parentComponent);

               changeHitFrame++;

               if (changeHitFrame >= HIT_FRAMES) {
                    changeHitFrame = 0;
                    state = 0;
               }
          }
          // Ataque
          else if (state == 3) {
               changeMovementFrame = 0;
               changeIdleFrame = 0;
               changeHitFrame = 0;

               g.drawImage(attack[changeAttackFrame], posX - horizontalDirection * 83, posY,
                         WIDTH * horizontalDirection, HEIGHT, parentComponent);

               changeAttackFrame++;

               if (changeAttackFrame >= ATTACK_FRAMES) {
                    changeAttackFrame = 0;
                    state = 0;
               }
          }
          // Movimento: Direita/Esquerda
          else if (state == 4) {
               changeAttackFrame = 0;
               changeIdleFrame = 0;
               changeHitFrame = 0;

               posX += horizontalDirection * moveX;

               // Colisão
               if (posX < LIMIT_X_LEFT) {
                    posX = LIMIT_X_LEFT;
               } else if (posX > LIMIT_X_RIGHT) {
                    posX = LIMIT_X_RIGHT;
               }

               g.drawImage(movement[changeMovementFrame], posX - horizontalDirection * 83, posY,
                         WIDTH * horizontalDirection, HEIGHT, parentComponent);

               changeMovementFrame++;

               if (changeMovementFrame >= MOVEMENT_FRAMES) {
                    changeMovementFrame = 0;
                    state = 0;
               }
          }
          // Movimento: Cima/Baixo
          else if (state == 5) {
               changeAttackFrame = 0;
               changeIdleFrame = 0;
               changeHitFrame = 0;

               posY += verticalDirection * moveY;

               // Colisão
               if (posY < LIMIT_Y_TOP) {
                    posY = LIMIT_Y_TOP;
               } else if (posY > LIMIT_Y_BOTTOM) {
                    posY = LIMIT_Y_BOTTOM;
               }

               g.drawImage(movement[changeMovementFrame], posX - horizontalDirection * 83, posY,
                         WIDTH * horizontalDirection, HEIGHT, parentComponent);

               changeMovementFrame++;

               if (changeMovementFrame >= MOVEMENT_FRAMES) {
                    changeMovementFrame = 0;
                    state = 0;
               }
          }

     }

     // Pintando a Vida e indicadores
     public void paintLife(Graphics g) {
          // Pintando indicador de Jogador
          if (currentLife != 0) {
               if (horizontalDirection == 1) {
                    g.drawImage(indicator, getPosX() - 34, getPosY() + 15, parentComponent);
               } else {
                    g.drawImage(indicator, getPosX() - 64, getPosY() + 15, parentComponent);
               }
          }

          if (numberOfPlayer == 1) {
               g.setColor(Color.WHITE);
               g.fillRect(0, 5, 90, 20);
               g.setColor(Color.BLUE);
               g.drawString("VIDA ATUAL: " + currentLife, 2, 20);
          } else {
               g.setColor(Color.WHITE);
               g.fillRect(Game.FRAME_WIDTH - 90, 5, 90, 20);
               g.setColor(Color.RED);
               g.drawString("VIDA ATUAL: " + currentLife, Game.FRAME_WIDTH - 88, 20);
          }
     }

     // Comandos pelo Teclado: Movimento e Ataque
     public KeyAdapter commands() {
          return new KeyAdapter() {
               @Override
               public void keyPressed(KeyEvent e) {
                    // Movimento
                    // Cima
                    if (e.getKeyCode() == KeyEvent.VK_W) {
                         // Colisão
                         if (posY > LIMIT_Y_TOP) {
                              state = 5;
                              verticalDirection = -1;
                         }
                    }
                    // Baixo
                    if (e.getKeyCode() == KeyEvent.VK_S) {
                         // Colisão
                         if (posY < LIMIT_Y_BOTTOM) {
                              state = 5;
                              verticalDirection = 1;
                         }
                    }
                    // Esquerda
                    if (e.getKeyCode() == KeyEvent.VK_A) {
                         // Colisão
                         if (posX > LIMIT_X_LEFT) {
                              state = 4;
                              horizontalDirection = -1;
                         }
                    }
                    // Direita
                    if (e.getKeyCode() == KeyEvent.VK_D) {
                         // Colisão
                         if (posX < LIMIT_X_RIGHT) {
                              state = 4;
                              horizontalDirection = 1;
                         }
                    }

                    // Ataque
                    if (e.getKeyCode() == KeyEvent.VK_J) {
                         state = 3;
                    }

               }
          };
     }

     // Recuperando Estado atual
     public int getState() {
          return this.state;
     }

     public void setState(int state) {
          this.state = state;
     }

     // Recuperando coordenadas atuais
     public int getPosX() {
          return this.posX;
     }

     public int getPosY() {
          return this.posY;
     }

     // Recuperando Vida Atual
     public int getCurrentLife() {
          return this.currentLife;
     }

     public void setCurrentLife(int currentLife) {
          this.currentLife = currentLife;
     }

     // Lógica
     // Ataque
     public void attack(Player enemyPlayer) {
          // Verificando o frame de ataque
          if (changeAttackFrame == 4) {
               if (horizontalDirection == 1) {
                    if ((this.posX + WIDTH >= enemyPlayer.getPosX() + 62
                              && this.posX + WIDTH <= enemyPlayer.getPosX() + (enemyPlayer.WIDTH - 62))
                              && (this.posY + 76 >= enemyPlayer.getPosY() + 50
                                        && this.posY + 76 <= enemyPlayer.getPosY() + enemyPlayer.HEIGHT - 30)) {
                         // Colocar o estado de hit e diminuir a vida
                         enemyPlayer.setCurrentLife(enemyPlayer.getCurrentLife() - 1);
                         enemyPlayer.setState(2);
                    }
               } else {
                    if ((this.posX >= enemyPlayer.getPosX() + 62
                              && this.posX <= enemyPlayer.getPosX() + (enemyPlayer.WIDTH - 62))
                              && (this.posY + 76 >= enemyPlayer.getPosY() + 50
                                        && this.posY + 76 <= enemyPlayer.getPosY() + enemyPlayer.HEIGHT - 30)) {
                         // Colocar o estado de hit e diminuir a vida
                         enemyPlayer.setCurrentLife(enemyPlayer.getCurrentLife() - 1);
                         enemyPlayer.setState(2);
                    }
               }
          }
     }

}
