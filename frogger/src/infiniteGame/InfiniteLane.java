package infiniteGame;

import util.Case;
import java.util.Iterator;
import java.util.ArrayList;
import gameCommons.Game;

public class InfiniteLane {

	/*Attributs*/
	private Game game;
    private int ord;
    private int speed;
    private ArrayList<InfiniteCar> cars;
    private boolean leftToRight;
    private double density;
    private int clock;

    public InfiniteLane(Game game, int ord, double density) {
        this.cars = new ArrayList<InfiniteCar>();
        this.game = game;
        this.ord = ord;
        this.speed = game.randomGen.nextInt(game.minSpeedInTimerLoops) + 1;
        this.leftToRight = game.randomGen.nextBoolean();
        this.density = density;
        for (int i = 0; i < game.height*2; i++) {
            this.moveCars(true);
            this.mayAddCar();
        }
    }

    public InfiniteLane(Game game, int ord) {
        this(game, ord, game.defaultDensity);
    }

    public void update() {
        this.clock += 1;
        if (this.clock <= this.speed) {
            this.moveCars(false);
            return;
        }

        this.clock = 0;
        this.moveCars(true);
        this.mayAddCar();
    }

    private void moveCars(boolean orderToMove) {
        for (InfiniteCar car : this.cars) {
            car.moveCar(orderToMove);
        }
        this.hideCars();
    }

    private void hideCars() {
        ArrayList<InfiniteCar> hideCars = new ArrayList<InfiniteCar>();
        for (InfiniteCar c : this.cars) {
            if (!c.reachesEdge()) {
                hideCars.add(c);
            }
        }
        this.removeCars(hideCars);
    }
    private void removeCars(ArrayList<InfiniteCar> hideCars){

      for (InfiniteCar car : hideCars) {
          this.cars.remove(car);
      }
    }
    private void mayAddCar() {
        if (this.isSafe(this.getFirstCase()) && this.isSafe(this.getBeforeFirstCase()) && this.game.randomGen.nextDouble() < this.density) {
            this.cars.add(new InfiniteCar(this.game, this.getBeforeFirstCase(), this.leftToRight));
        }
    }

    public boolean isSafe(Case pos) {
        for (InfiniteCar car : this.cars) {
            if (car.caseCovered(pos)) {
                return false;
            }
        }
        return true;
    }

    private Case getFirstCase() {
        if (this.leftToRight) {
            return new Case(0, this.ord);
        }
        return new Case(this.game.width - 1, this.ord);
    }

    private Case getBeforeFirstCase() {
        if (this.leftToRight) {
            return new Case(-1, this.ord);
        }
        return new Case(this.game.width, this.ord);
    }

}
