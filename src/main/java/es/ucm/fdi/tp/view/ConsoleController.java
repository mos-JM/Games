package es.ucm.fdi.tp.view;

import java.util.List;

import es.ucm.fdi.tp.base.model.*;
import es.ucm.fdi.tp.mvc.GameTable;

public class ConsoleController<S extends GameState<S,A>, A extends GameAction<S,A>> extends GameController<S,A>{
	
	private List<GamePlayer> players;
	boolean stopped;
	
	public ConsoleController(List<GamePlayer> players, GameTable<S,A> game) {
		super(game);
		this.players = players;
	}

	@Override
	public void makeMove(A action) {
		game.execute(action);
	}

	@Override
	public void stopGame() {
		game.stop();
		stopped = true;
	}

	@Override
	public void startGame() {
		game.start();
		stopped =  false;
	}

	@Override
	public void run() {
		
		startGame();
		int playerCount = 0;
		for (GamePlayer p : players) {
			p.join(playerCount++); // welcome each player, and assign
									// playerNumber
		}
		while (!stopped) {
			// request move
			A action = players.get(game.getState().getTurn()).requestAction(game.getState());
			// apply move
			game.execute(action);
			if(game.getState().isFinished()){
				stopGame();
			}
		}
	}
}