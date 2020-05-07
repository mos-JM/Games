package es.ucm.fdi.tp.view;

import java.util.List;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.*;
import es.ucm.fdi.tp.mvc.GameEvent.EventType;


public class ConsoleView <S extends GameState<S, A>, A extends GameAction<S,A>> implements GameObserver<S,A> {
	
	private List<GamePlayer> players;
	
	public ConsoleView(GameObservable<S,A> gameTable, List<GamePlayer> players) {
		this.players = players;
		gameTable.addObserver(this);
		
	}
	
	@Override
	public void notifyEvent(GameEvent<S, A> e) {
		
		EventType event = e.getType(); 
		switch(event){
		case Start: System.out.println(e + "\n" + e.getState()); break;
		case Change: System.out.println(e + "\n" + e.getState()); break;
		case Error: System.out.println(e.getError()); break;
		case Stop:  int winner = e.getState().getWinner();
					if (winner == -1) {
						System.out.println(e + " draw! \n" + e.getState());
		} else {
			System.out.println(e + "player " + (winner + 1) + " " + players.get(winner).getName() + " won!");
		}
		 break;
		case Info: System.out.println(e); break;
		}
	}

}
