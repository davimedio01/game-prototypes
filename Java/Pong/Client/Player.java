import java.awt.*;

public class Player {
	//2°: Gráficos e Game Looping
	public boolean up, down; //Movimentos
	public int x, y; //Eixos
	public int width, height;
	
	public Player(int x, int y) {
		//2°: Gráficos e Game Looping
		this.x = x;
		this.y = y;
		this.width = 5;
		this.height = 25;
		
	}
	
	//Refresh
	public void tick() {
		//2°: Gráficos e Game Looping
		//Movimentação
		if(up) {
			y-=2;
		}
		else if(down) {
			y+=2;
		}
		//Colisão
		if(y+height > Game.HEIGHT - 15) {
			y = (Game.HEIGHT - height - 15);
		}
		else if(y < 3) {
			y = 3;
		}
		
	}
	//Renderização
	public void render(Graphics g) {
		//2°: Gráficos e Game Looping
		g.setColor(Color.BLUE);
		g.fillRect(x, y, width, height);
	}
}
