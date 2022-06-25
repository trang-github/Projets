package infiniteGame;

import java.util.Iterator;
import util.Case;
import gameCommons.Game;
import java.util.ArrayList;
import gameCommons.IEnvironment;

public class InfiniteEnv implements IEnvironment {

	  /*Attributs*/
	private ArrayList<InfiniteLane> lanes;
    private Game game;

    public InfiniteEnv(Game game) {
        this.game = game;
        (this.lanes = new ArrayList<InfiniteLane>()).add(new InfiniteLane(game, 0, 0.0));
        this.lanes.add(new InfiniteLane(game, 1, 0.0));
        for (int i = 2; i < game.height * 2; i++) {
            this.newLane(i);
        }
    }

    public void newLane(int ord) {
        this.lanes.add(new InfiniteLane(this.game, ord));
    }

    public boolean isSafe(Case case_) {
        return this.lanes.get(case_.ord).isSafe(case_);
    }

    public boolean isWinningPosition(Case case_) {
        return false;
    }

    public void update() {
        for (InfiniteLane lane : this.lanes) {
            lane.update();
        }
    }

    public void newLane() {
        this.newLane(this.lanes.size());
    }
}
