package es.ucm.fdi.tp.was;

import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.model.GameAction;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Random;

public class WasStateTest {

	
	@Test
	public void WolfInitial(){
		
		WolfAndSheepState state = new WolfAndSheepState(8);
		List<WolfAndSheepAction> v =  state.validActions(0);
		if(v.size() == 1){
			state = (WolfAndSheepState)takeRandomAction(state);
			v =  state.validActions(0);
			System.out.println("Num acciones: " + v.size());
			assertEquals("Lobo tiene al principio 0 acc y tras 1 movimiento 4", 4, v.size());
		}
	}
	
	@Test
	public void SheepInitial(){
		
		WolfAndSheepState state = new WolfAndSheepState(8);
		List<WolfAndSheepAction> v =  state.validActions(1);
		assertEquals("Ovejas tienen al principio 7 acc.", 7, v.size());
		
	}
	
	@Test
	public void WolfWinner(){
		
		WolfAndSheepState state = new WolfAndSheepState(8);
		
		while(!WolfAndSheepState.isWinner(state.getBoard(), 0, state)){
			
			state = (WolfAndSheepState)takeRandomAction(state);
			if(WolfAndSheepState.isWinner(state.getBoard(), 0, state)){
				assertEquals("El lobo ha ganado con pos y = 0.", true, lfWolf(state.getBoard()));
			}
			else{
				if(WolfAndSheepState.isWinner(state.getBoard(), 1, state)){
					state = new WolfAndSheepState(8);
				}
			}
		}
		System.out.println("Tablero Lobo y = 0. \n" + state);
	}
	
	@Test
	public void WolfSurr(){
		
		WolfAndSheepState state = new WolfAndSheepState(8);
		
		while(!WolfAndSheepState.isWinner(state.getBoard(), 1, state)){
			state = (WolfAndSheepState)takeRandomAction(state);
			if(WolfAndSheepState.isWinner(state.getBoard(), 0, state)){
				state = new WolfAndSheepState(8);
			}
			else{
				if(Surrounded(state.getBoard(), 0)){
					assertEquals("Lobo rodeado: ", true, WolfAndSheepState.isWinner(state.getBoard(), 1, state));
					System.out.println("estado \n" + state);
				}
				else{
					if(WolfAndSheepState.isWinner(state.getBoard(), 1, state) && !Surrounded(state.getBoard(), 0)){
						state = new WolfAndSheepState(8);
					}
				}
			}
		}
	}
	
	private boolean Surrounded(int[][] board, int playerNumber){
		
		boolean ok = false;
		 for (int i = 1; i < board.length-1; i++) {
            for (int j = 1; j < board.length-1; j++) {
            	if(board[i][j] == 0 && playerNumber == 0){
            		if(board[i+1][j+1] == 1){
            			if(board[i-1][j+1] == 1){
            				if(board[i+1][j-1] == 1){
            					if(board[i-1][j-1] == 1){
            						ok = true;
            					}
            				}
            			}
            		}
            	}
            }
		 }
		 
		 return ok;
	}
	private boolean lfWolf(int[][] board){
		
		boolean ok = false;
		int i = 0;
		for(int j = 0; j < 8; j++){
			if(board[i][j] == 0){
				ok = true;
			}
		}
		return ok;
	}
	
	
	private static <S extends GameState<S,A>, A extends GameAction<S,A>> S takeRandomAction(S state) {
		List<A> actions = (List<A>) state.validActions(state.getTurn());
		return actions.get(new Random().nextInt(actions.size())).applyTo(state);
	}
}
