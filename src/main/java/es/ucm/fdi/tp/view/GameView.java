package es.ucm.fdi.tp.view;

import javax.swing.JPanel;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;

public abstract class GameView <S extends GameState<S,A>, A extends GameAction<S,A>> extends JPanel {

	private static final long serialVersionUID = 1L;

	public void enable(){};
	public void disable(){};
	public void showInfoMessage(String message){};
	public void updateView(S state){};
	public void setGameViewCtrl(GameWindow<S,A> gCtrl){};
	public void setState(S state) {}
    public void deleteText(){}
}
