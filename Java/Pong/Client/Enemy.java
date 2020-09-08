import java.awt.*;

public class Enemy {

	public double x, y; //Velocidade de IA pode alterar
	public int width, height;
	
	public Enemy(int x, int y) {
		this.x = x;
		this.y = y;
		this.width = 5;
		this.height = 25;
	}
	
	public void tick() {
		//Inteligência Artificial
		y += (Game.ball.y - y - 6) * 0.17;
		
	}
	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect((int)x, (int)y, width, height);
	}
	
}
