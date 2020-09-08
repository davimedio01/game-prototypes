import java.awt.event.*;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;

public class Game extends JFrame {
     // Painel de Execução do Jogo
     private RunGame panel;

     // Tamanho da Janela
     final static short FRAME_WIDTH = 1000;
     final static short FRAME_HEIGHT = 600;

     // Timer para execução do jogo
     private Timer t;

     // Construtor do Frame
     Game() {
          super("Vikings of Rage");

          panel = new RunGame();

          add(panel);
          pack();

          setResizable(false);
          setLocationRelativeTo(null);
          setDefaultCloseOperation(EXIT_ON_CLOSE);

          setVisible(true);

          // Timer para Execução das Funções
          t = new Timer(120, new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    panel.repaint();
                    panel.tick();
               }
          });
          t.start();

          // Ações do Jogador (Teclado)
          addKeyListener(panel.player1.commands());
     }

     // Classe de Execução do Jogo
     class RunGame extends JPanel {
          // Fundo de Tela
          private Background background;

          // Jogador 1
          private Player player1;

          // Jogador 2
          private Player player2;

          // Construtor do Panel
          RunGame() {
               setPreferredSize(new Dimension(Game.FRAME_WIDTH, Game.FRAME_HEIGHT));

               // Fundo de Tela
               background = new Background(this);

               // Jogador 1
               player1 = new Player(this, 1, 300, 350);

               // Jogador 2
               player2 = new Player(this, 2, 700, 350);
          }

          // Renderização
          @Override
          protected void paintComponent(Graphics g) {
               super.paintComponent(g);

               // Fundo de Tela + Variação
               background.paint(g);

               // Jogador 1
               player1.paintState(g);
               player1.paintLife(g);

               // Jogador 2
               player2.paintState(g);
               player2.paintLife(g);

               // Toolkit
               Toolkit.getDefaultToolkit().sync();

          }

          // Lógica (Ações do jogo)
          private void tick() {
               // Jogador 1
               if(player2.getCurrentLife() > 0){
                    player1.attack(player2);
               } else{
                    // Fim de jogo -> Vitória Jogador 1
                    if (player2.getState() == -1) {
                         EndOfGame(1);
                    }
                    player2.setState(1);
               }

               // Jogador 2
               if (player1.getCurrentLife() > 0) {
                    player2.attack(player1);
               } else {
                    // Fim de jogo -> Vitória Jogador 2
                    if (player1.getState() == -1) {
                         EndOfGame(2);
                    }
                    player1.setState(1);
               }
               
          }

          // Fim de Jogo
          private void EndOfGame(int numberOfWinner){
               t.stop();
               JOptionPane.showMessageDialog(this, "JOGADOR " + numberOfWinner + " VENCEU!", 
                    "VITÓRIA DO JOGADOR " + numberOfWinner + "!", JOptionPane.INFORMATION_MESSAGE);
               System.exit(1);
          }

     }

     public static void main(String[] args) {
          new Game();
     }
}
