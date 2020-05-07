package es.ucm.fdi.tp.was;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.base.model.GameState;


public class WolfAndSheepState extends GameState <WolfAndSheepState, WolfAndSheepAction>	 {

	private static final long serialVersionUID = -2311666526914984440L;
	
	private final int turn;
    private final boolean finished;
    private final int[][] board;
    private final int winner;
    
    private final int dim;

    final static int EMPTY = -1;
    
    public WolfAndSheepState(int dim) {
		super(2);
		if (dim != 8) {
            throw new IllegalArgumentException("Expected dim to be 8");
        }

        this.dim = dim;
        board = new int[dim][];
       
        for (int i=0; i<dim; i++) {
            board[i] = new int[dim];
            for (int j=0; j<dim; j++) {
            	if ((i == 0) && ( j%2 != 0 ))
            		board[i][j] = 1;
            	else if (i == 7 && j == 0 )
            		board[i][j] = 0;
            	else
            		board[i][j] = EMPTY;
            }
        }
        
        
        this.turn = 0;
        this.winner = -1;
        this.finished = false;
	}
    
    public WolfAndSheepState(WolfAndSheepState prev, int[][] board, boolean finished, int winner) {
    	super(2);
    	this.dim = prev.dim;
        this.board = board;
        this.turn = (prev.turn + 1) % 2;
        this.finished = finished;
        this.winner = winner;
    }    
    
    //Pregunta si las filas y columnas de la nueva accion generada coinciden
    //con la filas y columnas de alguna de las acciones del array de acciones validas.
    //si coincide devuelve true y la accion es valida.
    public boolean isValid(WolfAndSheepAction action) {
    	
    	boolean valida = false;
    	List<WolfAndSheepAction> valid = validActions(turn);
    
    	for(WolfAndSheepAction a: valid){
    		if(a.getACol() == action.getACol()&&
    				a.getARow() == action.getARow() &&
    				a.getCol() == action.getCol() &&
    				a.getRow() == action.getRow())
    			valida = true;
    	}
    	
    	return valida;
    }
    
    @Override
	public List<WolfAndSheepAction> validActions(int playerNumber) {
    	//Creamos la lista de acciones llamada valid.
    	ArrayList<WolfAndSheepAction> valid = new ArrayList<>();
    	//Si el juego ha terminado se devuelve una lista valid vacia, ya que si ha terminado no existen
    	//acciones validas.
        if (finished) {
            return valid;
        }
        
        //Si no ha terminado recorre el tablero buscando las acciones validas para el jugador que se le pase.
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if(board[i][j] == 0 && playerNumber == 0){
                	//System.out.println("Lobo en pos " + i +" "+ j);
                	//Mueve abajo derecha.
                	if(j+1 < dim && i+1 < dim && board[i+1][j+1] == EMPTY){
                		valid.add(new WolfAndSheepAction(playerNumber, i+1, j+1, i, j));
                		//System.out.println("MUEVE ABAJO DERECHA");
                	}
                	
                	//Mueve arriba derecha
            		if(j+1 < dim && i-1 >= 0 && board[i-1][j+1] == EMPTY){
            			valid.add(new WolfAndSheepAction(playerNumber, i-1, j+1, i ,j));
            			//System.out.println("Valida: " + playerNumber + " pos "+ (i-1) + " "+ (j+1) + " " + "nueva: " + i + " " + j);
            			//System.out.println("MUEVE ARRIBA DERECHA");
            		}
            		
        			//Mueve abajo izquierda
        			if(j-1 >= 0 && i+1 < dim && board[i+1][j-1] == EMPTY){
            			valid.add(new WolfAndSheepAction(playerNumber, i+1, j-1, i, j));
            			//System.out.println("MUEVE ABAJO IZQUIERDA");
            		}
        			
    				//Mueve arriba izquierda.
    				if(j-1 >= 0 && i-1 >= 0 && board[i-1][j-1] == EMPTY){
            			valid.add(new WolfAndSheepAction(playerNumber, i-1, j-1, i , j));
            			//System.out.println("MUEVE ARRIBA IZQUIERDA");
            		}
    				
    				//if(!valid.isEmpty()){
    					//board[i][j] = EMPTY;
    				//}
                }
           
                //Si no comprobamos si es una oveja.
                else{
                	if(board[i][j] == 1 && playerNumber == 1){
                		//System.out.println("La oveja " + i + " " + j +" puede mover:");
                		//Mueve abajo izquierda
            			if(j-1 >= 0 && i+1 < dim && board[i+1][j-1] == EMPTY){
                			valid.add(new WolfAndSheepAction(playerNumber, i+1, j-1, i, j));
                			//System.out.println("MUEVE ABAJO IZQUIERDA");
                		}
            			
            			//Mueve abajo derecha.1
                        if(j+1 < dim && i+1 < dim && board[i+1][j+1] == EMPTY){
                        	valid.add(new WolfAndSheepAction(playerNumber, i+1, j+1, i ,j));
                        	//System.out.println("MUEVE ABAJO DERECHA");
                        }
                	}
                }
            }
        }
    
        return valid;
    }
    
	public static boolean isDraw(int[][] board) {
        boolean empty = false;
        for (int i=0; ! empty && i<board.length; i++) {
            for (int j=0; ! empty && j<board.length; j++) {
                if (board[i][j] == EMPTY) {
                    empty = true;
                }
            }
        }
        return ! empty;
    }
    
    public static boolean isWinner(int[][] board, int playerNumber,WolfAndSheepState state) {
        boolean won = false;
        
        if(playerNumber == 0){
        	for (int j = 0; j < board.length; j++) {
            	if(board[0][j] == 0){
            		won = true;
            	}
        	}
        }
    	else{
			if(playerNumber == 1){
				List<WolfAndSheepAction> v =  state.validActions(0);
        		if(v.isEmpty()){
        			won = true;
        		}
			}
		}
        return won;
    }

    public int at(int row, int col) {
        return board[row][col];
    }
    
	@Override
	public int getTurn() {
		return turn;
	}
	

	@Override
	public boolean isFinished() {
		return finished;
	}

	@Override
	public int getWinner() {
		return winner;
	}
	
	/**
     * @return a copy of the board
     */
	
    public int[][] getBoard() {
        int[][] copy = new int[board.length][];
        for (int i=0; i<board.length; i++) {
        	copy[i] = board[i].clone();
        }
        return copy;
    }
    
    @Override
    public int getDim(){
    	return this.dim;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--------------------------------- \n");
        for (int i=0; i<board.length; i++) {
            sb.append("|");
            for (int j=0; j<board.length; j++) {
                sb.append(board[i][j] == EMPTY ? "   |" : board[i][j] == 0 ? " O |" : " X |");
                
            }
            sb.append("\n---------------------------------");
            sb.append("\n");
        }
        return sb.toString();
    }

	@Override
	public String getGameDescription() {
		
		return "Wolf And Sheep";
	}


}
