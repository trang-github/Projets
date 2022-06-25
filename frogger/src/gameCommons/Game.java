package gameCommons;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import graphicalElements.Element;
import graphicalElements.IFroggerGraphics;
import infiniteGame.InfiniteEnv;
import util.SecToHours;


public class Game {

	public final Random randomGen = new Random();

	// Caracteristique de la partie
	public final int width;
	public final int height;
	public final int minSpeedInTimerLoops;
	public final double defaultDensity;

	// Lien aux objets utilis�s
	/*private IEnvironment environment;*/
	private InfiniteEnv environment;
	private IFrog frog;
	private IFroggerGraphics graphic;

	// Calculer le score
	public int score;
	public int maxScore;
	public int runTime;
	/**
	 * 
	 * @param graphic
	 *            l'interface graphique
	 * @param width
	 *            largeur en cases
	 * @param height
	 *            hauteur en cases
	 * @param minSpeedInTimerLoop
	 *            Vitesse minimale, en nombre de tour de timer avant d�placement
	 * @param defaultDensity
	 *            densite de voiture utilisee par defaut pour les routes
	 */
	public Game(IFroggerGraphics graphic, int width, int height, int minSpeedInTimerLoop, double defaultDensity) {
		super();
		this.graphic = graphic;
		this.width = width;
		this.height = height;
		this.minSpeedInTimerLoops = minSpeedInTimerLoop;
		this.defaultDensity = defaultDensity;
		this.score = 0;
		this.maxScore = 0;
	}

	/**
	 * Lie l'objet frog � la partie
	 * 
	 * @param frog
	 */
	public void setFrog(IFrog frog) {
		this.frog = frog;
	}

	/**
	 * Lie l'objet environment a la partie
	 *
	 * @param environment
	 */
	/*public void setEnvironment(IEnvironment environment) {
		this.environment = environment;
	}*/
	public void setEnvironment(InfiniteEnv environment){
		this.environment = environment;
	}

	/**
	 * 
	 * @return l'interface graphique
	 */
	public IFroggerGraphics getGraphic() {
		return graphic;
	}

	/**
	 * Teste si la partie est perdue et lance un �cran de fin appropri� si tel
	 * est le cas
	 * 
	 * @return true si le partie est perdue
	 */
	public boolean testLose() {
		// TODO
		if(!this.environment.isSafe(this.frog.getPosition())){
			int timeExe = this.runTime * 100;
			timeExe = timeExe / 1000;
			SecToHours secToHours = new SecToHours();
			int[] times = secToHours.start(timeExe);
			this.graphic.endGameScreen("VOTRE SCORE EST: "+this.maxScore+" Temps: "+times[0]+"h:"+times[1]+"m:"+times[2]+"s");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	/**
	 * Teste si la partie est gagnee et lance un �cran de fin appropri� si tel
	 * est le cas
	 * 
	 * @return true si la partie est gagn�e
	 */
	public boolean testWin() {
		// TODO
		if (this.environment.isWinningPosition(this.frog.getPosition())) {
			this.graphic.endGameScreen("C'est gagn�");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	/**
	 * Actualise l'environnement, affiche la grenouille et verifie la fin de
	 * partie.
	 */
	public void update() {
		graphic.clear();
		environment.update();
		this.graphic.add(new Element(frog.getPosition(), Color.GREEN));
		testLose();
		testWin();
	}
	public void newLane(){

		this.environment.newLane();
	}

	public void runTime(int time) {


	}
}
