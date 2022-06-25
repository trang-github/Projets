package environement;

import java.awt.Color;

import util.Case;
import gameCommons.Game;
import graphicalElements.Element;

public class Car {
	
	/*Attributs*/
	private Game game;
	private Case leftPosition;
	private boolean leftToRight;
	private int length;
	private final Color colorLtR = Color.BLACK;
	private final Color colorRtL = Color.BLUE;

	//TODO Constructeur(s)

	public Car(Game game, Case leftPosition, boolean leftToRight) {
		this.game = game;
		this.length = game.randomGen.nextInt(3) +1;
		this.leftPosition = new Case(leftToRight ? (leftPosition.absc - length) : leftPosition.absc, leftPosition.ord);
		this.leftToRight = leftToRight;
	}

	//TODO : ajout de methodes

	public void goMove(boolean orderToMove){

		if(orderToMove){leftPosition= new Case(this.leftPosition.absc + (this.leftToRight ? 1 : -1), this.leftPosition.ord);}
		addToGraphics();
	}
	public boolean reachesEdge(){
		if (leftPosition.absc + length > 0 || leftPosition.absc < game.width) return true; else return false;
	}
	public boolean caseCovered(Case ca) {
		if(ca.ord == this.leftPosition.ord && (ca.absc >= this.leftPosition.absc && ca.absc < this.leftPosition.absc + this.length)) return true; else return false;
	}
	/* Fourni : addToGraphics() permettant d'ajouter un element graphique correspondant a la voiture*/

	private void addToGraphics() {
		for (int i = 0; i < length; i++) {
			Color color = colorRtL;
			if (this.leftToRight){
				color = colorLtR;
			}
			game.getGraphic()
					.add(new Element(leftPosition.absc + i, leftPosition.ord, color));
		}
	}
}

