import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Score{

	public int x, y; //Posições
	public int pontuacao; //Contador
	
	public Score(int x, int y) {
		this.x = x;
		this.y = y;
		this.pontuacao = 0;
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawString(Integer.toString(pontuacao), x, y);
	}
}
