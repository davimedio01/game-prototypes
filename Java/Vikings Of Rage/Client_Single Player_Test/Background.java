import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;

public class Background {
     //Component (Contexto)
     private Component parentComponent;

     //Imagens
     private Image background[];
     private final short BACKGROUND_FRAMES = 5;
     private short changeFrame;

     //Construtor: pegar imagens
     Background(Component parentComponent){
          this.parentComponent = parentComponent;

          //Recuperando imagens
          background = new Image[BACKGROUND_FRAMES];
          try{
               for(int i = 0; i < BACKGROUND_FRAMES; i++){
                    background[i] = ImageIO.read(new File("Data/Meshes/Background/background" + (i + 1) + ".png"));
               }
          } catch(IOException e){
               JOptionPane.showMessageDialog(parentComponent, "O fundoo de tela não pode ser carregado!\n" + e, "Erro",
                         JOptionPane.ERROR_MESSAGE);
               System.exit(1);
          }
          //Iniciando a variação de fundo
          changeFrame = 0;
     }

     //Pintar fundo de tela
     public void paint(Graphics g){
          g.drawImage(background[changeFrame], 0, 0, Game.FRAME_WIDTH, Game.FRAME_HEIGHT, parentComponent);
          changeCurrentFrame();
     }
     //Variar o fundo
     private void changeCurrentFrame(){
          changeFrame++;
          if(changeFrame > BACKGROUND_FRAMES - 1){
               changeFrame = 0;
          }
     }
     
}