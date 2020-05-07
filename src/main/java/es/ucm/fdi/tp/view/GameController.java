package es.ucm.fdi.tp.view;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameTable;

public class GameController<S extends GameState<S, A>, A extends GameAction<S, A>> {

	protected GameTable<S,A> game;
	
	public GameController(GameTable<S,A> game){
		this.game = game;
	}
	
	public void makeMove(A action){
		game.execute(action);
		
	}
	public void stopGame(){
		game.stop();
	}
	public void startGame(){
		game.start();
	}
	public void run(){
		game.start();
	}
	
	
}
