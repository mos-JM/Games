package es.ucm.fdi.tp.view;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.ConcurrentAiPlayer;

public class SmartThread<S extends GameState<S,A>, A extends GameAction<S,A>> extends Thread {

	private ConcurrentAiPlayer smart;
	private S state;
	private GameController<S,A> gCtrl;
	private GameView<S,A> gView;
	private long time;
	private Setting setting;
	private boolean terminado;
	
	public SmartThread(ConcurrentAiPlayer smart, S state, GameController<S,A> gCtrl, GameView<S,A> gView, Setting setting){
		this.smart = smart;
		this.state = state;
		this.gCtrl = gCtrl;
		this.gView = gView;
		this.setting = setting;
		this.terminado = false;
	}
	
	public void run(){
		
		long startTime = System.currentTimeMillis();
	    A action = smart.requestAction(state);
		long endTime = System.currentTimeMillis();
		time = endTime - startTime;
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				try{
					gCtrl.makeMove(action);
					setting.setDefaultColor();
					setting.setEnableStop(false, null);
					terminado = true;
				}
				catch(Exception e){}
				
			}
		});
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				if(terminado){
					gView.showInfoMessage(smart.getEvaluationCount() + " nodes in " + time + 
							 " ms (" + (smart.getEvaluationCount()/time) + " n/ns) value = " + String.format("%.8f",  smart.getValue()));
				}
			}
		});
	}
	
	public void setTime(long time){
		this.time = time;
	}
}
