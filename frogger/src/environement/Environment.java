package environement;

import java.util.ArrayList;

import util.Case;
import gameCommons.Game;
import gameCommons.IEnvironment;

public class Environment implements IEnvironment {
		
	/*Attributs*/
    private Game game;
    private ArrayList<Lane> lanes;

    public Environment(Game game){
        this.game = game;
        (this.lanes = new ArrayList<Lane>()).add(new Lane(game, 0, 0));
        for(int idx = 1; idx < game.height -1; idx++){
            this.lanes.add(new Lane(game, idx, game.randomGen.nextDouble() + 0.4));
        }
        this.lanes.add(new Lane(game, game.height -1, 0.0));
    }


    @Override
    public boolean isSafe(Case c) {
        return this.lanes.get(c.ord).isSafe(c);
    }

    @Override
    public boolean isWinningPosition(Case c) {
        return c.ord == game.height -1;
    }

    @Override
    public void update() {
        for(Lane lane : lanes){
            lane.update();
        }
    }
}
