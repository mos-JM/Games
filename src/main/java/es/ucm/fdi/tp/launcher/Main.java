/*
	Tic tac toe, El lobo y las obejas y ajedrez
* autor Agustín Jofre Millet
*/

package es.ucm.fdi.tp.launcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.base.console.ConsolePlayer;
import es.ucm.fdi.tp.base.model.*;
import es.ucm.fdi.tp.base.player.*;
import es.ucm.fdi.tp.chess.ChessAction;
import es.ucm.fdi.tp.chess.ChessState;
import es.ucm.fdi.tp.exceptions.*;
import es.ucm.fdi.tp.mvc.*;
import es.ucm.fdi.tp.ttt.*;
import es.ucm.fdi.tp.view.*;
import es.ucm.fdi.tp.was.*;


public class Main {
	
	private static Scanner s = new Scanner(System.in);
	
	
	// aqui tendremos createGameTable
	/*public static GameState<?, ?> createInitialState(String gameName) throws ParameterException{
		
		gameName = gameName.toUpperCase();
		GameState<?,?> game = null;
		
		switch(gameName){
		case "TTT": game = new TttState(3); break;
		case "WAS": game = new WolfAndSheepState(8); break;
		
		}
		if(game == null){
			throw new ParameterException("Error: juego " + gameName + " no definido");
		}
	
		return game;
	}
	
	public static GamePlayer createPlayer(String gameName, String playerType, String playerName) throws ParameterException{
		
		GamePlayer player = null;
		playerType = playerType.toUpperCase();
		
		switch(playerType){
		case "CONSOLE": player = new ConsolePlayer(playerName, s); break;
		case "RAND": player = new RandomPlayer(playerName); break;
		case "SMART": player = new SmartPlayer(playerName, 5); break;
		}
		
		if(player == null){
			throw new ParameterException("Error: jugador " + playerType + " no definido");
		}
		
		return player;
	}
	*/
	
	private static GameTable<?,?> createGame  (String gType){
			
			gType = gType.toUpperCase();
			System.out.println("GTYPE " + gType);
			
			GameTable<?,?> game = null;
			
			switch(gType){
			case "TTT":
						game = new GameTable<TttState, TttAction>(new TttState(3));
			break;			
			case "WAS": 
				System.out.println("GTYPE 1 " + gType);
						game = new GameTable<WolfAndSheepState, WolfAndSheepAction>(new WolfAndSheepState(8));
					
			break;
			case "CHESS":
				game = new GameTable<ChessState, ChessAction>(new ChessState());
			break;
			default: System.out.println("NULO");//throw new ParameterException("Error: juego " + gType + " no definido");
			}
			
			return game;
			
		}
		
	private static <S extends GameState<S, A>, A extends GameAction<S, A>> 
	void startConsoleMode (String gType, GameTable<S, A> game, String playerModes[]) {
		// create the lis of players as in assignemnt 4 // ...
		
		
		ArrayList<GamePlayer> players = new ArrayList<GamePlayer>();
		gType = gType.toUpperCase();
		
			for (int i = 0; i < playerModes.length; i++) {
			playerModes[i] = playerModes[i].toUpperCase();
			System.out.println("jugador de entrada: " + playerModes[i]);
			switch(playerModes[i]){
			case "MANUAL":
				players.add(new ConsolePlayer("Juagador"+(i+1), s));
				System.out.println("Añadido manual");
			break;
			case "RAND":
				players.add(new RandomPlayer("Juagador"+(i+1)));
				System.out.println("Añadido rand");
			break;
			case "SMART":
				players.add(new SmartPlayer("Juagador"+i, 5));
				System.out.println("Añadido smart");
			break;
				
			}
		}
			
		
		new ConsoleView<S,A>(game, players);
		new ConsoleController<S,A>(players,game).run();
		
		
	}
	
	private static GameView<?,?> createGameView  (String gType){
		
		gType = gType.toUpperCase();
		System.out.println("GTYPE " + gType);
		
		GameView<?,?> gView = null;
		
		switch(gType){
		case "TTT":
					gView = new TttView<TttState, TttAction>(3);
		break;			
		case "WAS": 
			System.out.println("GTYPE 1 " + gType);
					gView = new WasView<WolfAndSheepState ,WolfAndSheepAction>(8);
				
		break;
		case "CHESS":
			gView = new ChessView<ChessState ,ChessAction>(8);
		break;
		default: System.out.println("NULO");//throw new ParameterException("Error: juego " + gType + " no definido");
		}
		
		return gView;
	}
	
	private static <S extends GameState<S,A>, A extends GameAction<S,A>>
	void startGUIMode (String gType, GameTable<S,A> game, String playerModes[]){
		
		for(int i = 0; i <game.getState().getPlayerCount(); i++){
			GamePlayer p1 = new RandomPlayer("Juagador"+(i+1));
			ConcurrentAiPlayer p2 = new ConcurrentAiPlayer("Juagador"+ (i+1));
			p1.join(i);
			p2.join(i);
			GameController<S,A> gCtrl = new GameController<S,A>(game);
			@SuppressWarnings("unchecked")
			GameView<S,A> gView = (GameView<S, A>) createGameView(gType);
			new GameWindow<S,A>(i, p1, p2, gView, gCtrl, game);
			
			//Lanzamos el hilo del swing
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					gCtrl.run();
				}
			});
		}
	}
	
	public static void main(String[] args) throws ParameterException {
		if(args.length < 2) {
			System.out.println("ERROR NO HAY ARGUMENTOS SUGICIENTES");
			System.exit(1);
		}
		
		GameTable<?,?> game = createGame(args[0]);
		
		if (game == null) {
			System.out.println("Invalid game");
			System.exit(1);
		}
		
		String[] playerModes = Arrays.copyOfRange(args,  2, args.length);
		
		if(game.getState() == null){
			System.out.println("ESTADO NULO");
		}
		if ( game.getState().getPlayerCount() != playerModes.length) {
			System.out.println("invalid number of players");
			System.exit(1);
		}
		
		switch (args[1]) {
		case "console":
			startConsoleMode(args[0],game, playerModes);
			break;
		case "gui":
			startGUIMode(args[0], game, playerModes);
			break;
			default:
				System.out.println("Invalid view mode: " +args[1]);
				System.exit(1);
		}
	}


}
