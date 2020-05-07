package es.ucm.fdi.tp.view;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;

public interface GameViewCtrl<S extends GameState<S,A>, A extends GameAction<S,A>> {
	
	//Interfaz que le indica a GameWindow los metodos que tendra que implementar para la barra
	//de botones setting.
	
	void randomActionButtonPressed();
	void smartActionButtonPressed();
	void resetActionButtonPressed();
	void quitActionButtonPressed();
	void playerModeSelected(PlayerMode mode);
	void stopActionButtonPressed(Thread smartThread);
	void setMaxThreads(int threads);
	void setTimeOut(int timeOut);
}
