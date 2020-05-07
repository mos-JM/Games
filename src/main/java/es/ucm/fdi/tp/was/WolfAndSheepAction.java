package es.ucm.fdi.tp.was;

import es.ucm.fdi.tp.base.model.GameAction;


public class WolfAndSheepAction implements GameAction<WolfAndSheepState, WolfAndSheepAction> {

	private static final long serialVersionUID = -7061739732232203112L;
	
	private int player;
	private int row;
	private int col;
	private int acol;
	private int arow;
	
	public WolfAndSheepAction(int player,int nrow, int ncol, int row, int col) {
        this.player = player;
        this.row = nrow;
        this.col = ncol;
        this.acol = col;
        this.arow = row;
    }

	@Override
	public int getPlayerNumber() {
		 return player;
	}

	@Override
	public WolfAndSheepState applyTo(WolfAndSheepState state) {
		 if (player != state.getTurn()) {
			 	System.out.println("Jugador num: " + player);
	            throw new IllegalArgumentException("Not the turn of this player");
	        }
	        // make move
	        int[][] board = state.getBoard();
	        
	        board[row][col] = player;
	        board[arow][acol] = -1;
	       
	        // update state
	        WolfAndSheepState next =  new WolfAndSheepState(state, board, false, -1);
	        if (WolfAndSheepState.isWinner(board, state.getTurn(), next)) {
	            next = new WolfAndSheepState(state, board, true, state.getTurn());
	        } else {	        
	            if(next.validActions(next.getTurn()).isEmpty())
		        	next = new WolfAndSheepState(next, board, false, -1);
	        }
	        
	        return next;
	}

	public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    
    public int getACol(){
    	return acol;
    }
    
    public int getARow(){
    	return arow;
    }

    public String toString() {
        return "place " + player + " at (" + row + ", " + col + ")";
    }


}
