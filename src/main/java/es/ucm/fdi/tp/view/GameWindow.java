 package es.ucm.fdi.tp.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.ConcurrentAiPlayer;
import es.ucm.fdi.tp.mvc.GameEvent;
import es.ucm.fdi.tp.mvc.GameObserver;
import es.ucm.fdi.tp.mvc.GameTable;

public class GameWindow<S extends GameState<S,A>, A extends GameAction<S,A>> extends JFrame implements GameViewCtrl<S,A>, GameObserver<S,A> {

	//Esta clase se encargara de definir los propgramas principales de las vistas basicas y de iniciar la interfaz 
	//LLamando a los metodos que derivan de ella para crearlo poco a poco
	
	private static final long serialVersionUID = 1L;
	private GamePlayer random;
	private ConcurrentAiPlayer smart;
	private int playerNumber;
	private PlayerMode mode;
	private GameView<S,A> gView;
	private GameController<S,A> gCtrl;
	@SuppressWarnings("unused")
	private GameTable<S,A> game;
	private S state;
	private boolean initialized;
	private boolean stopped;
	private int currentTurn;
	private Setting setting;
	private Thread smartThread;
	
	@SuppressWarnings("static-access")
	public GameWindow(int playerNumber, GamePlayer random, ConcurrentAiPlayer smart, GameView<S,A> gView, GameController<S,A> gCtrl, GameTable<S,A> game){
		
		super(game.getState().getGameDescription() + " (View for player " + playerNumber + ")");
		this.mode = mode.Manual;
		this.playerNumber = playerNumber;
		this.random = random;
		this.smart = smart;
		this.gView = gView;
		this.gCtrl = gCtrl;
		this.setting = new Setting(this);
		this.game = game;
		this.state = game.getState();
		this.stopped = false;
		this.initialized = true;
		this.currentTurn = game.getState().getTurn();
		//gView.setState(game.getState());
		gView.updateView(game.getState());
		gView.setGameViewCtrl(this);
		initGUI();
		game.addObserver(this);
		
	}
	
	//Arranca Setting y GameView y lo anade al JFrame;
	private void initGUI(){
		
		this.getContentPane().setLayout(new BorderLayout());
		setting = new Setting(this);
		this.getContentPane().add(setting, BorderLayout.PAGE_START);
		//Crear objeto de tipo reetBoard que sera el JPanel de abajo y anadirlo.
		this.getContentPane().add(gView, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setSize(700, 520);
		if(this.playerNumber == 0){
			this.setLocation(20, 100);
		}
		else{
			this.setLocation(680, 100);
		}
		//this.setResizable(false);
		this.setVisible(true);
		
	}
	
	public void makeManualMove(A action){
		if(!stopped && playerNumber == currentTurn){
			gCtrl.makeMove(action);
			gView.showInfoMessage("You have requested a Manual move");
		}
	}
	
	
	@Override
	public void randomActionButtonPressed() {
		
		gView.showInfoMessage("You have requested a random move.");
		makeRandomActionMove();
	}
	
	private void makeRandomActionMove(){
		if(!stopped && playerNumber == currentTurn){
			try{
				gCtrl.makeMove(random.requestAction(state));
			}
			catch(Exception e){
				
			}
		}
	}

	@Override
	public void smartActionButtonPressed() {
		
		gView.showInfoMessage("You have requested a smart move.");
		makeSmartActionMove();
	}
	
	private void makeSmartActionMove(){
		if(!stopped && playerNumber == currentTurn){
			try{
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						setting.setYellow();
						System.out.println("Color cambiado");
						setting.setEnableStop(true, smartThread);
					}
				});
				smartThread = new SmartThread<S, A>(smart, state, gCtrl, gView, setting);
				smartThread.start();
			}
			catch(Exception e){}
		}
	}

	@Override
	public void resetActionButtonPressed() {
		if(smartThread != null){
			smartThread.interrupt();
		}
		gCtrl.startGame();
		gView.updateView(this.state);
	}

	@Override
	public void quitActionButtonPressed() {
		
		if(JOptionPane.showConfirmDialog(null, "¿Desea salir del juego?", 
		   "Salir del juego", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
		   System.exit(0);
		}
	}

	@Override
	public void playerModeSelected(PlayerMode mode) {
		this.mode = mode;
		decideAutomaticGameMove(mode);
		
	}
	
	@SuppressWarnings("incomplete-switch")
	private void decideAutomaticGameMove(PlayerMode mode){
		switch(mode){
		case Smart: this.mode = PlayerMode.Smart;
			SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				makeSmartActionMove();
			}
		}); break;
		case Random: this.mode = PlayerMode.Random;
			SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				makeRandomActionMove();	
			}
		}); break;
		}
	}
	
	public int getPlayerNumber(){
		return this.playerNumber;
	}

	@Override
	public void notifyEvent(GameEvent<S, A> e) {
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				handleEvent(e);
			}
		});
	}
	
	private void changeWindow(){
		if(currentTurn == playerNumber){
			 gView.showInfoMessage("Your turn...");
			 gView.showInfoMessage("Click on source cell...");
			 gView.enable();
		}
		else{
			gView.showInfoMessage("Turn for player " + currentTurn);
			gView.disable();
		}
	}
	
	private void stop(S state,  GameController<S,A> gCtrl){
		if(state.isFinished()){
			gCtrl.stopGame();
			this.stopped = true;
		}
	}
	
	private void handleEvent(GameEvent<S,A> e){
		switch(e.getType()){
		case Start:
			    this.mode = PlayerMode.Manual;
			    this.stopped = false;
			    gView.deleteText();
				gView.showInfoMessage("The Game was started...");
				this.state = e.getState();
				this.currentTurn = e.getState().getTurn();
				changeWindow();
				setting.setResetParameters();
				gView.updateView(e.getState());break;
			       
			       
		case Info: //En principio info no se usa para nada.
					gView.updateView(e.getState()); break;
		case Change: decideAutomaticGameMove(mode);
					 this.state = e.getState();
					 this.currentTurn = e.getState().getTurn();
					 changeWindow();
					 gView.updateView(e.getState());
					 stop(state, gCtrl);
					
					break;
		
		case Stop:  if(stopped && initialized){
						gView.showInfoMessage("Ha ganado el jugador " + e.getState().getWinner());
					    gView.updateView(e.getState()); 
					    gView.enable();
					    initialized = false;
					}
					break;
			      
		case Error: gView.showInfoMessage("Error " + e.getError().getMessage());
					gView.updateView(e.getState()); break;
		}

	}

	@Override
	public void setMaxThreads(int threads) {
		smart.setMaxThreads(threads);
		
	}

	@Override
	public void setTimeOut(int timeOut){
		
		smart.setTimeout(timeOut);
		
	}

	@Override
	public void stopActionButtonPressed(Thread smartThread) {
	
			smartThread.interrupt();
			setting.setEnableStop(false, null);
			setting.setDefaultColor();
	}
}
