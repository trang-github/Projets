package environement;

import java.util.ArrayList;

import util.Case;
import gameCommons.Game;

public class Lane {
	private Game game;
	private int ord;
	private int speed;
	private ArrayList<Car> cars = new ArrayList<>();
	private boolean leftToRight;
	private double density;
	private int clock;

	// TODO : Constructeur(s)
	public Lane(Game game, int ord, double density){
		this.cars = new ArrayList<Car>();
		this.game = game;
		this.ord = ord;
		this.speed = game.randomGen.nextInt(game.minSpeedInTimerLoops) + 1;
		this.leftToRight = game.randomGen.nextBoolean();
		this.density = density;
	}

	public Lane(final Game game, final int ord) {
		this(game, ord, game.defaultDensity);
	}

	public void update() {

		// TODO
		this.clock++;
		if (this.clock <= this.speed) {
			this.moveCars(false);
			return;
		}
		this.moveCars(true);
		this.mayAddCar();
		this.clock = 0;
	}

	// TODO : ajout de methodes
	/**
	 * TODO : implement method hideCars
	 * CREATE A LIST OF CARS TO HIDE
	 */
	private void hideCars(){
		ArrayList<Car> hideCars = new ArrayList<Car>();
		for(Car car : cars){
			if(!car.reachesEdge()){
				hideCars.add(car);
			}
		}
		this.removeCars(hideCars);
	}
	private void removeCars(ArrayList<Car> hideCars){
		for(Car car : hideCars){
			cars.remove(car);
		}
	}
	private void moveCars(boolean orderToMove){
		for(Car car : cars){
			car.goMove(orderToMove);
		}
		this.hideCars();
	}
	public boolean isSafe(Case ca){
		for(Car car : cars){
			if(car.caseCovered(ca)){
				return false;
			}
		}
		return true;
	}
	/*
	 * Fourni : mayAddCar(), getFirstCase() et getBeforeFirstCase() 
	 */

	/**
	 * Ajoute une voiture au d�but de la voie avec probabilit� �gale � la
	 * densit�, si la premi�re case de la voie est vide
	 */
	private void mayAddCar() {
		if (isSafe(getFirstCase()) && isSafe(getBeforeFirstCase())) {
			if (game.randomGen.nextDouble() < density) {
				cars.add(new Car(game, getBeforeFirstCase(), leftToRight));
			}
		}
	}

	private Case getFirstCase() {
		if (leftToRight) {
			return new Case(0, ord);
		} else
			return new Case(game.width - 1, ord);
	}

	private Case getBeforeFirstCase() {
		if (leftToRight) {
			return new Case(-1, ord);
		} else
			return new Case(game.width, ord);
	}

}
