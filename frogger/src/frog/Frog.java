package frog;

import gameCommons.Game;
import gameCommons.IFrog;
import graphicalElements.Element;
import util.Case;
import util.Direction;

import java.awt.*;

public class Frog implements IFrog {
	/*Attributs*/
	private Game game;
	private Case position;
	private Direction direction;
	
	/*Contructeur*/
	public Frog(final Game game) {
		this.game = game;
		this.position = new Case(game.width / 2,0);
		this.direction = Direction.up; // Logiquement l'utilisateur doit aller vers le haut
	}

	/**
	 * TODO : implement method getPosition
	 * return position:Case
	 */
	@Override
	public Case getPosition() {
		return this.position;
	}
	/**
	 * TODO : implement method getPosition
	 * return direction:Direction
	 */
	@Override
	public Direction getDirection() {
		return this.direction;
	}
	/**
	 * TODO : implement method move
	 * set direction = keyPressed
	 */
	@Override
	public void move(Direction keyPressed) {
		this.direction = keyPressed;
		switch (keyPressed){
			case up:
				if (this.position.ord < this.game.height - 1) {
					System.out.println("=========+ Key pressed "+keyPressed+" +========");
					this.position = new Case(this.position.absc, this.position.ord + 1);
				}
				break;
			case down:
				if (this.position.ord > 0) {
					System.out.println("=========+ Key pressed "+keyPressed+" +========");
					this.position = new Case(this.position.absc, this.position.ord - 1);
				}
				break;
			case left:
				if (this.position.absc > 0) {
					System.out.println("=========+ Key pressed "+keyPressed+" +========");
					this.position = new Case(this.position.absc - 1, this.position.ord);
				}
				break;
			case right:
				if (this.position.absc < this.game.width - 1) {
					System.out.println("=========+ Key pressed "+keyPressed+" +========");
					this.position = new Case(this.position.absc + 1, this.position.ord);
				}
				break;
			default:
				System.out.println("=========+ Something went wrong +========");
		}
		this.game.getGraphic().add(new Element(this.position, Color.GREEN));
		this.game.testWin();
		this.game.testLose();
	}
}
