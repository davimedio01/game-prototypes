import java.awt.*;
import java.util.Random;

public class Ball {

	public double x, y; //Posições da Bola
	public int width, height;
	
	public double dx, dy; //Direção (1 ou -1)
	public double speed = 2.5; //Velocidade
	
	public Ball(int x, int y) {
		this.x = x;
		this.y = y;
		this.width = 5;
		this.height = 5;
		/*
		dx = new Random().nextGaussian(); //Aleatório
		dy = new Random().nextGaussian();*/
		
		int angle = new Random().nextInt(80) + 45;
		dx = Math.cos(Math.toRadians(angle));
		dy = Math.sin(Math.toRadians(angle));
	}
	
	public void tick() {
		//Colisão com Paredes (Baixo e Cima)
		if(y + (dx*speed) + height >= Game.HEIGHT - 15) {
			dy*=-1;
		}
		else if(y + (dy*speed) < 0) {
			dy*=-1;
		}
		
		//Posição
		x += dx*speed;
		y += dy*speed;
		
		//Passou do Inimigo
		if(x >= Game.WIDTH) {
			Game.scorePlayer.pontuacao++;
			x = Game.WIDTH/2;
			y = Game.HEIGHT/2;
			dy*=-1;
			//new Game();
			//return;
		}
		else if(x < 0) { //Passou do Jogador
			Game.scoreEnemy.pontuacao++;
			x = Game.WIDTH/2;
			y = Game.HEIGHT/2;
			dy*=-1;
			//new Game();
			//return;
		}
		
		//Rectangle: testar colisões genericamente
		Rectangle bounds = new Rectangle((int)(x+(dx*speed)), (int)(y+(dy*speed)), width, height);
		//Colisão com Player
		Rectangle boundsPlayer = new Rectangle(Game.player.x, Game.player.y, Game.player.width, Game.player.height);
		//Colisão com Inimigo
		Rectangle boundsEnemy = new Rectangle((int)Game.enemy.x, (int)Game.enemy.y, Game.enemy.width, Game.enemy.height);
		if(bounds.intersects(boundsPlayer) ||
				bounds.intersects(boundsEnemy)) {
			//Caso bata no Player/Inimigo
			dx*=-1;
		}
		
		
	}
	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillRect((int)x, (int)y, width, height);
	}
}
