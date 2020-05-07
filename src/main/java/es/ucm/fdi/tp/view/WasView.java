package es.ucm.fdi.tp.view;

import java.awt.Color;

import javax.swing.JOptionPane;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.extra.jboard.JBoard.Shape;
import es.ucm.fdi.tp.was.WolfAndSheepAction;
import es.ucm.fdi.tp.was.WolfAndSheepState;

public class WasView<S extends GameState<S,A>, A extends GameAction<S,A>> extends ReetBoardGameView<WolfAndSheepState,WolfAndSheepAction>{
	
	private static final long serialVersionUID = 1L;
	private int arow;
	private int acol;
	private int row;
	private int col;
	private boolean click;
	private int dim;
	
	public WasView(int dim){
		this.click = false;
		this.dim = dim;
		color1 = Color.RED;
		color2 = Color.BLUE;
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
			//System.out.println("arow = " + arow);
			//System.out.println("acol = " + acol);
			//System.out.println("row = " + this.row);
			//System.out.println("col = " + this.col);
			//if(comprobarAccion(this.arow, this.acol, row, col)){
			WolfAndSheepAction action = new WolfAndSheepAction(this.state.getTurn(), this.row, this.col, this.arow, this.acol);
			//System.out.println("Accion que entra: WolfAndSheepAction " + this.state.getTurn() + " " + this.row + " " + this.col + " " + this.arow + " " + this.acol);
			if(this.state.isValid(action)){
				gCtrl.makeManualMove(action);
				click = false;
			}
			else{
				JOptionPane.showMessageDialog(null, "Movimiento no válido.", "ADVERTENCIA!", 0);
				click = false;
			}
		}
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
		return Shape.CIRCLE;
	}

	@Override
	protected Color getColor(int player) {
		
		return player == 0 ? color1 : color2;
	}

	@Override
	protected Integer getPosition(int row, int col) {
		
		Integer i;
		if(this.state.at(row, col) == -1){
			i = null;
		}
		else{
			i = this.state.at(row, col);
		}
		return i;
	}
}
