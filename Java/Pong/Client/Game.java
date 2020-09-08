import java.awt.Canvas;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class Game extends Canvas implements Runnable, KeyListener{

	//1°: Criando Janela do Jogo
		//Tamanho da Tela e Escala
	public static int WIDTH = 240;
	public static int HEIGHT = 120;
	public static int SCALE = 3; //Leveza e Otimização
	public static boolean isRunning;
	
	//2°: Gráficos e Game Looping
	public static Player player;
	//Renderização de Fundo (evitar mostrar refresh/tela piscando)
	public BufferedImage layer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	
	//3°: IA, Bolinha, Placar
	public static Enemy enemy;
	public static Ball ball;
	
	//Bônus: Placar e Davizaum
	public static Score scorePlayer, scoreEnemy;
	
	public Game(){
		isRunning = true;
		//2°: Gráficos e Game Looping
		this.addKeyListener(this); //Adicionar o KeyListener
		player = new Player(5, 30); //Posição inicial do Player
		
		//3°: IA, Bolinha, Placar
		enemy = new Enemy(WIDTH-15, 30);
		ball = new Ball(WIDTH/2, 30);
		
		//Bônus: Placar e Davizaum
		scorePlayer = new Score(12, 12);
		scoreEnemy = new Score(WIDTH-24, 12);
	}
	
	
	public static void main(String[] args) {
		Game game = new Game();
		//1°: Criando Janela do Jogo
		JFrame frame = new JFrame("Pongvi - BETA 1.0 (*CHEIO DE BUGS*)");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		frame.add(game);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		//2°: Gráficos e Game Looping
		new Thread(game).start();
	}
	
	//2°: Gráficos e Game Looping
		//Refresh
	public void tick() {
		//2°: Gráficos e Game Looping
		player.tick();
		//3°: IA, Bolinha, Placar
		enemy.tick();
		ball.tick();
		//Bônus: Placar e Davizaum
		scorePlayer.tick();
		scoreEnemy.tick();
	}
		//Renderização
	public void render() {
		//2°: Gráficos e Game Looping
		//Player
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return; //Ao criar, deve retornar para dar continuidade
		}
		Graphics g = layer.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		player.render(g);
		
		//3°: IA, Bolinha, Placar
		enemy.render(g);
		ball.render(g);
		
		//Bônus: Placar e Davizaum
		scorePlayer.render(g);
		scoreEnemy.render(g);
			//Título
		g.setColor(Color.WHITE);
		g.drawString("PONGVI: THE TRASH", 60, 12);
			//Créditos
		g.setColor(Color.WHITE);
		g.drawString("Criado por: Davi Augusto", 50, HEIGHT-15);
		
		g = bs.getDrawGraphics();
		g.drawImage(layer, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null); //Desenhar tela de fundo 
		
		g.dispose();
		bs.show(); //Mostrando toda renderização na tela
	}
	
	@Override
	public void run() {
		//2°: Gráficos e Game Looping
		boolean finalJogo = false;
		while(isRunning) {
			requestFocus();
			tick();
			render();
			try {
				Thread.sleep(1000/60); //60FPS
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Bônus: Placar e Davizaum
				//Captando resultado final e vencedor
			if(scorePlayer.pontuacao == 10) {
				JOptionPane.showMessageDialog(null, "Vencedor: Jogador! Parabéns!", "Fim de Jogo",
	                    JOptionPane.INFORMATION_MESSAGE);
				finalJogo = true;
			}
			else if(scoreEnemy.pontuacao == 10) {
				JOptionPane.showMessageDialog(null, "Vencedor: CPU. Boa Sorte na Próxima Vez.", 
						"Fim de Jogo", JOptionPane.ERROR_MESSAGE);
				finalJogo = true;
			}
			
			if(finalJogo == true) {
				final int opcao = new JOptionPane().showConfirmDialog(null,
		                "Deseja reiniciar o jogo?", "Fim de Jogo",
		                JOptionPane.YES_NO_OPTION);
				if(opcao == 1) {
					System.exit(0);
				}
				finalJogo = false;
				scorePlayer.pontuacao = scoreEnemy.pontuacao = 0;
			}
		}
		
	}
	
	//2°: Gráficos e Game Looping
		//Eventos do Teclado
	@Override
	public void keyTyped(KeyEvent e) { }
	@Override
	public void keyPressed(KeyEvent e) {
		//2°: Gráficos e Game Looping
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			//player.down = false;
			player.up = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			//player.up = false;
			player.down = true;
		}	
	}
	@Override
	public void keyReleased(KeyEvent e) {
		//2°: Gráficos e Game Looping
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}
	}

}
