package es.ucm.fdi.tp.view;

import java.awt.Color;
import java.util.List;

import javax.swing.JOptionPane;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.chess.ChessAction;
import es.ucm.fdi.tp.chess.ChessBoard;
import es.ucm.fdi.tp.chess.ChessState;
import es.ucm.fdi.tp.extra.jboard.JBoard.Shape;

public class ChessView <S extends GameState<S,A>, A extends GameAction<S,A>> extends ReetBoardGameView<ChessState, ChessAction>{


	private static final long serialVersionUID = 1L;
	private int arow;
	private int acol;
	private int row;
	private int col;
	private int dim = 8;
	private boolean click;
	private ChessAction action;
	
	public ChessView(int dim){
		this.dim = dim;
		this.click = false;
		color1 = Color.WHITE;
		color2 = Color.GRAY;
		repaint();
	}

	@Override
	protected int getNumCols() {
		
		return dim;
	}

	@Override
	protected int getNumRows() {
		
		return dim;
	}

	@Override
	protected void mouseClicked(int row, int col, int clickCount,
			int mouseButton) {
		if(click == false){
			this.acol = col;
			this.arow = row;
			click = true;
			showInfoMessage("Selected (" + row + "," + col + "). Click on destination cell or ESC to cancel selection.");
		}
		else{
			this.col = col;
			this.row = row;
		    action = new ChessAction(this.state.getTurn(), this.arow, this.acol, this.row, this.col, ChessAction.Special.QueenQ);
			
			if(state.isValid(action)){
				gCtrl.makeManualMove(action);
				click = false;
			}
			else{
				action = generateSpaecial(action);
				if(action == null){
					JOptionPane.showMessageDialog(null, "Movimiento no válido.", "ADVERTENCIA!", 0);
					click = false;
				}
				else{
					gCtrl.makeManualMove(action);
					click = false;
				}
				
			}
		}
	}
	
	private ChessAction generateSpaecial(ChessAction action){
		ChessAction act = null;
		List<ChessAction> valid = state.validActions(state.getTurn());
		for(ChessAction a: valid){
			if(action.getSrcCol() == a.getSrcCol() 
			   && action.getDstCol() == a.getDstCol()
			   && action.getSrcRow() == a.getSrcRow()
			   && action.getDstRow() == a.getDstRow())
				    act = a;
		}
		
		return act;
	}
	@Override
	protected void keyTyped(int keyCode) {
		if(keyCode == 0){
			click = false;
			showInfoMessage("Cancel selection.");
		}
	}

	@Override
	protected Shape getShape(int player) {
		
		return Shape.CIRCLECHESS;
	}

	@Override
	protected Color getColor(int player) {
		
		return player == 0 ? color1 : color2;
	}

	@Override
	protected Integer getPosition(int row, int col) {
		
		Integer i;
		if(this.state.at(row, col)== 16){
			i = null;
		}
		else{
			byte p = (byte)this.state.at(row, col);
			if(ChessBoard.black(p)){
				i = 1;
			}
			else{
				i = 0;
			}
		}
		return i;
}

}
