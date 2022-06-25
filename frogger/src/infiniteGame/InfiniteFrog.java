package infiniteGame;

import graphicalElements.Element;
import java.awt.Color;
import gameCommons.Game;
import util.Direction;
import util.Case;
import gameCommons.IFrog;

public class InfiniteFrog implements IFrog {
	private Case position;
    private Direction direction;
    private Game game;

    public InfiniteFrog(final Game game) {
        this.position = new Case(game.width / 2, 1);
        this.direction = Direction.up;
        this.game = game;
    }

    public Case getPosition() {
        return this.position;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void move(final Direction keyPressed) {
        this.direction = keyPressed;
        switch (keyPressed){
            case up:
                System.out.println("=========+ Key pressed "+keyPressed+" +========");
                Game game = this.game;
                this.position = new Case(this.position.absc, this.position.ord + 1);
                game.score += 1;
                if (this.game.score > this.game.maxScore) {
                    this.game.maxScore = this.game.score;
                    this.game.newLane();
                }
                break;
            case down:
                System.out.println("=========+ Key pressed "+keyPressed+" +========");
                if (this.position.ord > 1) {
                    this.position = new Case(this.position.absc, this.position.ord - 1);
                    Game oldGame = this.game;
                    oldGame.score -= 1;
                }
                break;
            case left:
                System.out.println("=========+ Key pressed "+keyPressed+" +========");
                if (this.position.absc > 0) {
                    this.position = new Case(this.position.absc - 1, this.position.ord);
                }
                break;
            case right:
                System.out.println("=========+ Key pressed "+keyPressed+" +========");
                if (this.position.absc < this.game.width - 1) {
                    this.position = new Case(this.position.absc + 1, this.position.ord);
                }
                break;
        }

    }
	
}
