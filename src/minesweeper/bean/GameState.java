package minesweeper.bean;

import javax.swing.JLabel;

public class GameState {
	
	private int[] field;
	private boolean inGame; 
	private int mines_left;
	private double level; 
	
	private int all_cells; 
	private JLabel statusbar; 
	
	private Player player;
	
	public int[] getField() {
		return field;
	}
	public void setField(int[] field) {
		this.field = field;
	}
	public boolean isInGame() {
		return inGame;
	}
	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}
	public int getMines_left() {
		return mines_left;
	}
	public void setMines_left(int mines_left) {
		this.mines_left = mines_left;
	}
	public double getLevel() {
		return level;
	}
	public void setLevel(double level) {
		this.level = level;
	}
	public int getAll_cells() {
		return all_cells;
	}
	public void setAll_cells(int all_cells) {
		this.all_cells = all_cells;
	}
	public JLabel getStatusbar() {
		return statusbar;
	}
	public void setStatusbar(JLabel statusbar) {
		this.statusbar = statusbar;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}	
	
	
	public GameState() {
		super();
	}
	
	public GameState(int[] field, boolean inGame, int mines_left, double level,
			int all_cells, JLabel statusbar, Player player) {
		super();
		this.field = field;
		this.inGame = inGame;
		this.mines_left = mines_left;
		this.level = level;
		this.all_cells = all_cells;
		this.statusbar = statusbar;
		this.player = player;		
	}
	
	
	
	

}
