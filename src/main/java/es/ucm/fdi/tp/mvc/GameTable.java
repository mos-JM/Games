package es.ucm.fdi.tp.mvc;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameError;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameEvent.EventType;

/**
 * An event-driven game engine.
 * Keeps a list of players and a state, and notifies observers
 * of any changes to the game.
 */
public class GameTable<S extends GameState<S, A>, A extends GameAction<S, A>> implements GameObservable<S, A> {

    //private S initialState;
    private S finalState;
    private S initState;
    boolean active;
    private List <GameObserver<S, A>> list;

    public GameTable(S initState) {
    	this.initState = initState;
        this.finalState = initState;
        this.list  = new ArrayList <GameObserver<S,A>>();
    }
    
    public void start() {
    	
        active = true;
        GameEvent<S,A> event = new GameEvent<S,A>(EventType.Start, null,
        		initState, null, "The Game is Starting...");
        this.finalState = initState;
        System.out.println("finalState \n" + finalState);
        notifyObserver(event);
        
    }
    public void stop() {
        active = false;
        GameEvent<S,A> event = new GameEvent<S,A>(EventType.Stop, null,
        		finalState, null, "The Game is ended: ");
        notifyObserver(event);
    }
    public void execute(A action) {
        if(active && !finalState.isFinished()){
        	System.out.println(finalState);
        	this.finalState = action.applyTo(finalState);
        	GameEvent<S,A> e = new GameEvent<S,A>(EventType.Change, action, 
        			finalState, null, "The game was change: ");
        	notifyObserver(e);
        }
        else{
        	GameError error = new GameError("Error al realizar la accion");
        	GameEvent<S,A> e = new GameEvent<S,A>(EventType.Error, action, 
        			finalState, error, "ERROR!!!");
        	notifyObserver(e);
        }
    }
    public S getState() {
		return finalState;
    }
    
    public boolean activeGame(){
    	return active;
    }

    public void addObserver(GameObserver<S, A> o) {
        list.add(o);
    }
    public void removeObserver(GameObserver<S, A> o) {
        list.remove(o);
    }
    
    public void notifyObserver(GameEvent <S, A> e){
    	
    	for(GameObserver<S,A> o: list){
    		o.notifyEvent(e);
    	}
    }
}
