package es.ucm.fdi.tp.view;

import java.awt.Color;

import javax.swing.JOptionPane;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.extra.jboard.JBoard.Shape;
import es.ucm.fdi.tp.ttt.TttAction;
import es.ucm.fdi.tp.ttt.TttState;

public class TttView <S extends GameState<S,A>, A extends GameAction<S,A>> extends ReetBoardGameView<TttState,TttAction>{
	
	private static final long serialVersionUID = 1L;
	private int dim;
	
	public TttView(int dim){
		this.dim = dim;
		super.color1 = Color.RED;
		super.color2 = Color.BLUE;
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
	protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
		//System.out.println("row = " + row);
		//System.out.println("col = " + col);
		TttAction action = new TttAction(this.state.getTurn(), row, col);
		if(state.isValid(action)){
			gCtrl.makeManualMove(action);
		}
		else{
			JOptionPane.showMessageDialog(null, "Movimiento no válido.");
		}
	}

	@Override
	protected void keyTyped(int keyCode) {}

	@Override
	protected Shape getShape(int player) {
		
		return Shape.CIRCLE;
		
	}

	//Para el ajedrez los colores tienen que ser negro y blanco luego esta estara en cada una de las
	//vistas en lugar de aqui
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
