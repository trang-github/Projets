package infiniteGame;

import graphicalElements.Element;
import java.awt.Color;
import util.Case;
import gameCommons.Game;

public class InfiniteCar {

	/*Attributs*/
	private Game game;
    private Case leftPosition;
    private boolean leftToRight;
    private int length;
    private final Color colorLtR;
    private final Color colorRtL;

    public InfiniteCar(final Game game, final Case leftPosition, final boolean leftToRight) {

        this.colorLtR = Color.BLUE;
        this.colorRtL = Color.ORANGE;
        this.game = game;
        this.length = game.randomGen.nextInt(3) + 1;
        this.leftToRight = leftToRight;
        this.leftPosition = new Case(leftToRight ? (leftPosition.absc - this.length) : leftPosition.absc, leftPosition.ord);
    }

    public void moveCar(final boolean orderToMove) {
        if (orderToMove) {
            this.leftPosition = new Case(this.leftPosition.absc + (this.leftToRight ? 1 : -1), this.leftPosition.ord);
        }
        this.addToGraphics();
    }
    public boolean reachesEdge() {
        if(this.leftPosition.absc + this.length > 0 || this.leftPosition.absc < this.game.width)
            return true; else return false;
    }

    public boolean caseCovered(Case case_) {
        if(case_.ord == this.leftPosition.ord && (case_.absc >= this.leftPosition.absc && case_.absc < this.leftPosition.absc + this.length))
            return true; else return false;
    }

    private void addToGraphics() {
        for (int i = 0; i < this.length; i++) {

            this.game.getGraphic().add(new Element(this.leftPosition.absc + i, this.leftPosition.ord - this.game.score, this.leftToRight ? this.colorLtR : this.colorRtL));
        }
    }
}
