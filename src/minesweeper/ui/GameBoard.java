package minesweeper.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import minesweeper.bean.GameState;
import minesweeper.bean.MinesweeperRecords;
import minesweeper.bean.Player;
import minesweeper.handlers.ReadRecord;
import minesweeper.handlers.WriteRecord;

public class GameBoard extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5368863840670659924L;
	/* images number and cell size */
	private final int NUM_IMAGES = 13;
	private final int CELL_SIZE = 15;

	/* cell state definition */
	private final int COVER_FOR_CELL = 10;
	private final int MARK_FOR_CELL = 10;
	private final int EMPTY_CELL = 0;


	private int MINE_CELL = 9;

	private final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL; // 24 = 14
																		// + 10

	private final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL; // 34
																			// =24+10


	int s = 0;

	/* image name match number */
	private final int DRAW_MINE = 9;
	private final int DRAW_COVER = 10;
	private final int DRAW_MARK = 11;
	private final int DRAW_WRONG_MARK = 12;

	/* mines number,cols, rows  */
	private int N_MINES = 50;
	private final int N_ROWS = 16;
	private final int N_COLS = 16;

	private int[] field; // game field
	private boolean inGame; // in game flag
	private int mines_left;
	private Image[] img; // store images used in this game
	private double level; // game difficulty level

	private int all_cells; //
	private JLabel statusbar; // display information
	private String imagePath ="images/"; // image path

	private Player player;
	private final int LIVES = 3;

	private Stack<GameState> currentState = new Stack<GameState>();

	private Stack<GameState> preState = new Stack<GameState>();;

	private final String title = "Mine Sweeper";

	public Stack<GameState> getCurrentState() {
		return currentState;
	}

	public void setCurrentState(Stack<GameState> currentState) {
		this.currentState = currentState;
	}

	public Stack<GameState> getPreState() {
		return preState;
	}

	public void setPreState(Stack<GameState> preState) {
		this.preState = preState;
	}

	public GameBoard(JLabel statusbar, double level) {

		// player = new Player();
		this.statusbar = statusbar;
		this.setName(title);
		img = new Image[NUM_IMAGES];
		
		//imagePath = this.getClass().getClassLoader().getResource("minesweeper/images/").getFile() +"../../../minesweeper/images/";
		//System.out.println(imagePath);
		//JOptionPane.showMessageDialog(null, imagePath, "mine warning", JOptionPane.ERROR_MESSAGE);
		for (int i = 0; i < NUM_IMAGES; i++) {
			//System.out.println(imagePath + i + ".png");
			java.net.URL imgURL = GameFrame.class.getResource(imagePath + i + ".png");
			img[i] = (new ImageIcon(imgURL)).getImage();
		}

		setDoubleBuffered(true);

		addMouseListener(new MinesAdapter());
		this.level = level;

		newGame();
	}

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


	/* initialize a new game */
	public void newGame() {

		Random random;
		int current_col;

		int i = 0;
		int position = 0; // cell id
		int cell = 0; //

		player = new Player(LIVES);

		random = new Random();
		inGame = true;
		// System.out.println("level = " + level);
		mines_left = (int) (N_MINES * level);

		// ///////////////////////////////////
		// System.out.println("minesleft = " + mines_left);
		all_cells = N_ROWS * N_COLS; // field total number
		field = new int[all_cells];

		for (i = 0; i < all_cells; i++)
			field[i] = COVER_FOR_CELL;

		statusbar.setText("Mines left: " + Integer.toString(mines_left)
				+ "         Lives: " + player.getLives()); // d

		/* put mines in a random position */
		i = 0;
		while (i < (int) (N_MINES * level)) {

			position = (int) (all_cells * random.nextDouble()); // a random cell in 256 cells

			if ((position < all_cells)
					&& (field[position] != COVERED_MINE_CELL)) {

				current_col = position % N_COLS; // current column
				field[position] = COVERED_MINE_CELL;

				i++;
				/* calculate the surrounding mines */
				if (current_col > 0) {// if it is not in left border
					cell = position - 1 - N_COLS;
					if (cell >= 0)
						if (field[cell] != COVERED_MINE_CELL)
							field[cell] += 1;
					cell = position - 1;
					if (cell >= 0)
						if (field[cell] != COVERED_MINE_CELL)
							field[cell] += 1;

					cell = position + N_COLS - 1;
					if (cell < all_cells)
						if (field[cell] != COVERED_MINE_CELL)
							field[cell] += 1;
				}

				cell = position - N_COLS; 
				if (cell >= 0)
					if (field[cell] != COVERED_MINE_CELL)
						field[cell] += 1;
				cell = position + N_COLS;
				if (cell < all_cells)
					if (field[cell] != COVERED_MINE_CELL)
						field[cell] += 1;

				if (current_col < (N_COLS - 1)) {// if it is not in right border
					cell = position - N_COLS + 1;
					if (cell >= 0)
						if (field[cell] != COVERED_MINE_CELL)
							field[cell] += 1;
					cell = position + N_COLS + 1;
					if (cell < all_cells)
						if (field[cell] != COVERED_MINE_CELL)
							field[cell] += 1;
					cell = position + 1;
					if (cell < all_cells)
						if (field[cell] != COVERED_MINE_CELL)
							field[cell] += 1;
				}
			}
		}
		/* clear state stack */
		preState.clear();
		currentState.clear();
		int[] fieldclone = field.clone();
		GameState state = new GameState(fieldclone, inGame, mines_left, level,
				all_cells, statusbar, player);
		currentState.push(state);
		System.out.println("newgame state---" + currentState.size());

	}

	/* finding empty cells method */
	public void find_empty_cells(int j) {

		int current_col = j % N_COLS; // current position
		int cell;
		/* calculate adjacent cell in the preceding column of current position */
		if (current_col > 0) {
			cell = j - N_COLS - 1;
			if (cell >= 0)
				if (field[cell] > MINE_CELL) {
					field[cell] -= COVER_FOR_CELL;
					if (field[cell] == EMPTY_CELL)
						find_empty_cells(cell);
				}

			cell = j - 1;
			if (cell >= 0)
				if (field[cell] > MINE_CELL) {
					field[cell] -= COVER_FOR_CELL;
					if (field[cell] == EMPTY_CELL)
						find_empty_cells(cell);
				}

			cell = j + N_COLS - 1;
			if (cell < all_cells)
				if (field[cell] > MINE_CELL) {
					field[cell] -= COVER_FOR_CELL;
					if (field[cell] == EMPTY_CELL)
						find_empty_cells(cell);
				}
		}

		/* calculate adjacent cell in the current column of current position */
		cell = j - N_COLS;
		if (cell >= 0)
			if (field[cell] > MINE_CELL) {
				field[cell] -= COVER_FOR_CELL;
				if (field[cell] == EMPTY_CELL)
					find_empty_cells(cell);
			}

		cell = j + N_COLS;
		if (cell < all_cells)
			if (field[cell] > MINE_CELL) {
				field[cell] -= COVER_FOR_CELL;
				if (field[cell] == EMPTY_CELL)
					find_empty_cells(cell);
			}

		/* calculate adjacent cell in the after column of current position */
		if (current_col < (N_COLS - 1)) {
			cell = j - N_COLS + 1;
			if (cell >= 0)
				if (field[cell] > MINE_CELL) {
					field[cell] -= COVER_FOR_CELL;
					if (field[cell] == EMPTY_CELL)
						find_empty_cells(cell);
				}

			cell = j + N_COLS + 1;
			if (cell < all_cells)
				if (field[cell] > MINE_CELL) {
					field[cell] -= COVER_FOR_CELL;
					if (field[cell] == EMPTY_CELL)
						find_empty_cells(cell);
				}

			cell = j + 1;
			if (cell < all_cells)
				if (field[cell] > MINE_CELL) {
					field[cell] -= COVER_FOR_CELL;
					if (field[cell] == EMPTY_CELL)
						find_empty_cells(cell);
				}
		}
	}

	@Override
	/* repaint method will call this method */
	public void paintComponent(Graphics g) {
		// System.out.println("paintcomponent");
		int cell = 0;
		int uncover = 0;
		int score = 0;
		// System.out.println("printField:"+ field);
		// Random random = new Random();
		for (int i = 0; i < N_ROWS; i++) {
			for (int j = 0; j < N_COLS; j++) {

				cell = field[(i * N_COLS) + j];

				if (!inGame) {
					if (cell == COVERED_MINE_CELL) {
						cell = DRAW_MINE;
					} else if (cell == MARKED_MINE_CELL) {
						cell = DRAW_MARK;
						score++; // correct marked mines cell

						// System.out.println("score: " + score);
					} else if (cell > COVERED_MINE_CELL) {
						cell = DRAW_WRONG_MARK; // 标记错误的格子
					} else if (cell > MINE_CELL) {
						cell = DRAW_COVER;
					}

				} else {
					if (cell > COVERED_MINE_CELL)
						cell = DRAW_MARK;
					else if (cell > MINE_CELL) {
						cell = DRAW_COVER;
						uncover++; // uncovered cell
					}
				}
				// System.out.println("img[cell]: " + cell +"---- j: "+ j +
				// "----i:" + i);
				g.drawImage(img[cell], (j * CELL_SIZE), (i * CELL_SIZE), this); // match images
			}
			// System.out.println("-------------drawImage------------");
		}

		if (uncover == 0 && inGame) {
			inGame = false;
			statusbar.setText("Game won");
			/* calculate score */
			System.out.println("win score:" + score);
			player.setScore(score);
			// writeScore();

		} else if (!inGame) {
			/* calculate score */
			statusbar.setText("Lives: " + player.getLives() + "   Game lost!");
			System.out.println("lost score:" + score);
			player.setScore(score);

			// System.out.println("s="+s++);

		}
		// System.out.println("paintComponent over!!");
	}

	/* click field action */
	class MinesAdapter extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			// System.out.println("MousePressed()");
			int x = e.getX();
			int y = e.getY();

			int cCol = x / CELL_SIZE; // get the column number of click position
			int cRow = y / CELL_SIZE; // get the row number of click position

			boolean rep = false;

			Random random = new Random();

			if (!inGame) {
				writeScore();
				newGame();
				repaint();
			}

			if ((x < N_COLS * CELL_SIZE) && (y < N_ROWS * CELL_SIZE)) {

				if (e.getButton() == MouseEvent.BUTTON3) {

					if (field[(cRow * N_COLS) + cCol] > MINE_CELL) { // if the N field value larger than 9
						// System.out.println("rep:-------1 "+rep);
						rep = true;

						if (field[(cRow * N_COLS) + cCol] <= COVERED_MINE_CELL) {
							if (mines_left > 0) {
								field[(cRow * N_COLS) + cCol] += MARK_FOR_CELL;
								mines_left--;
								// System.out.println("mines_left--");
								statusbar.setText("Mark left: "
										+ Integer.toString(mines_left)
										+ "         Lives: "
										+ player.getLives());
							} else
								statusbar.setText("No marks left"); // Marks been used out
						} else {

							field[(cRow * N_COLS) + cCol] -= MARK_FOR_CELL;
							mines_left++;
							statusbar.setText("Mines left: "
									+ Integer.toString(mines_left)
									+ "         Lives: " + player.getLives());
						}
					}

				} else {

					if (field[(cRow * N_COLS) + cCol] > COVERED_MINE_CELL) {
						return;
					}

					if ((field[(cRow * N_COLS) + cCol] > MINE_CELL)
							&& (field[(cRow * N_COLS) + cCol] < MARKED_MINE_CELL)) {

						field[(cRow * N_COLS) + cCol] -= COVER_FOR_CELL;
						// System.out.println("rep--2"+rep);
						rep = true;

						if (field[(cRow * N_COLS) + cCol] == MINE_CELL) {
							int r = random.nextInt(2);
							System.out.println("random----" + r);
							System.out.println("playerlives:---"
									+ player.getLives());
							if (r == 0 && player.getLives() - 1 > 0) {
								player.setLives(player.getLives() - 1);
								JOptionPane.showMessageDialog(null, "You triggled a small mine! 1 lives losed!", "mine warning", JOptionPane.ERROR_MESSAGE);
								System.out.println("lives-1");
							} else if (r == 0 && player.getLives() - 1 <= 0) {
								player.setLives(player.getLives() - 1);
								JOptionPane.showMessageDialog(null, "You triggled a small mine! 1 lives losed!", "mine warning", JOptionPane.ERROR_MESSAGE);
								inGame = false;
							}

							if (r == 1 && player.getLives() - 2 > 0) {
								player.setLives(player.getLives() - 2);
								JOptionPane.showMessageDialog(null, "You triggled a medium mine! 2 lives losed!", "mine warning", JOptionPane.ERROR_MESSAGE);
								System.out.println("lives-2");
							} else if (r == 1 && player.getLives() - 2 <= 0) {
								player.setLives(player.getLives() - 2);
								JOptionPane.showMessageDialog(null, "You triggled a medium mine! 2 lives losed!", "mine warning", JOptionPane.ERROR_MESSAGE);
								inGame = false;
							}

							if (r == 2) {
								player.setLives(player.getLives() - 3);
								JOptionPane.showMessageDialog(null, "You triggled a big mine! 3 lives losed!", "mine warning", JOptionPane.ERROR_MESSAGE);
								inGame = false;
							}
							System.out.println("endplayerlives:---"
									+ player.getLives());
							System.out.println("inGame:---" + inGame);
						}
						// inGame = false;
						if (field[(cRow * N_COLS) + cCol] == EMPTY_CELL)
							find_empty_cells((cRow * N_COLS) + cCol);
					}
				}

				if (rep) {
					// System.out.println("repaint()  ------  1");
					statusbar.setText("Mines left: "
							+ Integer.toString(mines_left)
							+ "         Lives: "
							+ player.getLives());
					repaint();
				}

			}

			/* add save GameState action */
			int[] fieldclone = field.clone();
			GameState state = new GameState(fieldclone, inGame, mines_left,
					level, all_cells, statusbar, player);
			// System.out.println("newstate--field"+
			// fieldclone+"---inGame:"+inGame);
			if (!currentState.isEmpty()) {

				preState.push((GameState) currentState.pop());
				// System.out.println("prepush----" + preState.size());
				// System.out.println("currentpop----" + currentState.size());
			}
			currentState.push(state);
			// System.out.println("currentpush----" + currentState.size());

		}
	}

	/* add savegame method */
	public void saveGameDataToFile(File file) {

		try {
			FileOutputStream fileStream = new FileOutputStream(file);
			ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);

			objectStream.writeObject(player);
			// System.out.println("Saving----------------------player: "+
			// player);
			objectStream.writeObject(field);
			// System.out.println("save field: " + field + "-----field[254]:"+
			// field[254]);
			objectStream.writeObject(inGame);
			System.out.println("inGame: " + inGame);
			objectStream.writeObject(mines_left);
			System.out.println("mines_left: " + mines_left);
			// objectStream.writeObject(img);
			// System.out.println("img: "+ img);
			objectStream.writeObject(level);
			// System.out.println("level: "+ level);

			objectStream.writeObject(all_cells);
			// System.out.println("all_cells: "+ all_cells);
			objectStream.writeObject(statusbar);
			// System.out.println("statusbar: " + statusbar);
			objectStream.writeObject(imagePath);
			// System.out.println("imagePath: "+ imagePath);
			objectStream.close();
			fileStream.close();

			JOptionPane.showConfirmDialog(new JFrame(),
					"Save game successfully!", "Minesweeper",
					JOptionPane.DEFAULT_OPTION);
		} catch (Exception e) {
			JOptionPane.showConfirmDialog(new JFrame(), e.toString()
					+ "\nSaving game failed!", "Minesweeper",
					JOptionPane.DEFAULT_OPTION);
		}
	}

	/* add loadgame method */
	public void loadGameDataFromFile(File file) {

		try {
			Player s_player;
			int[] s_field;

			Boolean s_inGame;
			Integer s_mines_left;
			// Image[] s_img;
			Double s_level;

			Integer s_all_cells;
			JLabel s_statusbar;
			String s_imagePath;

			FileInputStream fileStream = new FileInputStream(file);
			ObjectInputStream objectStream = new ObjectInputStream(fileStream);

			s_player = (Player) objectStream.readObject();
			s_field = (int[]) objectStream.readObject();
			// System.out.println("s_field----------------"+s_field +
			// "-----field[254]:"+ s_field[254]);
			s_inGame = (Boolean) objectStream.readObject();
			s_mines_left = (Integer) objectStream.readObject();
			// s_img = (Image[]) objectStream.readObject();

			s_level = (Double) objectStream.readObject();
			s_all_cells = (Integer) objectStream.readObject();
			s_statusbar = (JLabel) objectStream.readObject();
			s_imagePath = (String) objectStream.readObject();

			objectStream.close();
			fileStream.close();

			if (s_player != null && s_field != null && s_inGame != null
					&& s_mines_left != null && s_level != null
					&& s_all_cells != null && s_statusbar != null
					&& s_imagePath != null) {
				player = s_player;
				// System.out.println("Loading----------------------player: "+
				// player);
				field = s_field;
				// System.out.println("loading field: "+
				// field+"----field[254]:"+ field[254]);
				inGame = s_inGame;
				// System.out.println("inGame: "+ inGame);
				mines_left = s_mines_left;
				// System.out.println("mines_left: "+ mines_left);
				// img = s_img;
				level = s_level;
				// System.out.println("level: "+ level);
				all_cells = s_all_cells;
				// System.out.println("all_cells: "+ all_cells);
				statusbar = s_statusbar;
				// System.out.println("statusbar: " + statusbar);
				imagePath = s_imagePath;
				// System.out.println("imagePath: "+ imagePath);
				// updateUI();

				// System.out.println("panel repaint");
				JOptionPane.showConfirmDialog(new JFrame(),
						"loading game successfully!", "Minesweeper",
						JOptionPane.DEFAULT_OPTION);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showConfirmDialog(new JFrame(), e.toString()
					+ "\nLoading game failture!", "Minesweeper",
					JOptionPane.DEFAULT_OPTION);
		}
	}

	private void writeScore() {
		if (player.getScore() == 0) {
			return;
		}
		File file = new File("file.dat");
		MinesweeperRecords records = new ReadRecord().readRecordsFromFile(file);
		if (records == null
				|| records.isEmpty()
				|| !records.isFull()
				|| (records.getLastAvailableRecord().getScore() < player
						.getScore() && records.isFull())) {
			String playerName = JOptionPane
					.showInputDialog("Please input your name");
			if (playerName == null || playerName.length() == 0
					|| playerName.trim().equals("")) {
				playerName = "Nobody";
			}
			Player record = new Player(playerName, player.getScore());
			records.addRecordToTopTen(record);
			new WriteRecord().writeRecordToFile(records, file);
		}

	}
	

}