package minesweeper.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import minesweeper.bean.GameState;
import minesweeper.bean.MinesweeperRecords;
import minesweeper.bean.Player;
import minesweeper.handlers.ReadRecord;

public class GameFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6935485228446313338L;
	private final int FRAME_WIDTH = 250;
	private final int FRAME_HEIGHT = 315;

	private final JLabel statusbar;

	private GameBoard board;

	// private GameFrame panel;

	// private Container contentPane;

	private JMenuItem startMI = new JMenuItem("Start");

	// private JMenuItem loadMI = new JMenuItem("Open");

	public JMenuItem saveMI = new JMenuItem("Save");
	public JMenuItem loadMI = new JMenuItem("load");

	private JMenu levelMenu = new JMenu("level");

	private JMenuItem exitMI = new JMenuItem("Exit");

	// private JMenuItem aboutMI = new JMenuItem("About");

	private JMenu recordMenu = new JMenu("Rank");
	private JMenuItem rankMI = new JMenuItem("Rank");

	private JMenu editMenu = new JMenu("Edit");
	public JMenuItem UndoMI = new JMenuItem("Undo");
	public JMenuItem RedoMI = new JMenuItem("Redo");
	public JMenuItem SolveMI = new JMenuItem("Solve");

	private JRadioButtonMenuItem levelEasy = new JRadioButtonMenuItem("easy",
			true); // 100%

	private JRadioButtonMenuItem levelNormal = new JRadioButtonMenuItem(
			"normal", false); // 150%

	private JRadioButtonMenuItem levelHard = new JRadioButtonMenuItem("hard",
			false); // 200%

	public GameFrame(double level) {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLocationRelativeTo(null);
		setTitle("Minesweeper");

		// setTitle(Board.SNAKE_GAME);

		setResizable(false);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu setMenu = new JMenu("Set");

		setMenu.setMnemonic('S');
		setMenu.setMnemonic('R');

		menuBar.add(setMenu);
		menuBar.add(recordMenu);

		recordMenu.add(rankMI);
		rankMI.addActionListener(new RecordAction());

		menuBar.add(editMenu);
		editMenu.add(UndoMI);
		editMenu.addSeparator();
		editMenu.add(RedoMI);
		editMenu.addSeparator();
		editMenu.add(SolveMI);
		editMenu.addSeparator();

		setMenu.add(startMI);
		setMenu.addSeparator();

		setMenu.add(levelMenu);
		setMenu.addSeparator();
		setMenu.add(saveMI);
		setMenu.addSeparator();
		setMenu.add(loadMI);

		setMenu.addSeparator();
		setMenu.add(exitMI);

		ButtonGroup group = new ButtonGroup();
		group.add(levelEasy);
		group.add(levelNormal);
		group.add(levelHard);

		/* Start actionlisteners */
		startMI.addActionListener(new StartAction());

		/* save & load actionlisteners */
		saveMI.addActionListener(new SaveAction());
		loadMI.addActionListener(new LoadAction());

		levelEasy.addActionListener(new LevelAction());
		levelMenu.add(levelEasy);
		levelNormal.addActionListener(new LevelAction());
		levelMenu.add(levelNormal);
		levelHard.addActionListener(new LevelAction());
		levelMenu.add(levelHard);

		UndoMI.addActionListener(new UndoAction());
		RedoMI.addActionListener(new RedoAction());
		SolveMI.addActionListener(new SolveAction());
		

		statusbar = new JLabel("");
		add(statusbar, BorderLayout.SOUTH);
		board = new GameBoard(statusbar, level);
		add(board);
		// System.out.println(statusbar.getText()+"  "+ level);

		setResizable(false);
	}

	/* reset action */
	private class StartAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			board.newGame();
			repaint();
		}

	}

	/* changing difficulty action */
	private class LevelAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			double level = 1.0;
			// System.out.println("1111") ;
			if (levelEasy.isSelected()) {
				// System.out.println("2222") ;
				dispose();
				// setVisible(false);
				level = 1;
				GameFrame gf = new GameFrame(level);
				gf.setVisible(true);

			} else if (levelNormal.isSelected()) {
				// System.out.println("3333") ;
				dispose();
				// setVisible(false);
				level = 1.5;

				GameFrame gf = new GameFrame(level);
				// gb.newGame();
				gf.setVisible(true);
			} else if (levelHard.isSelected()) {
				// System.out.println("4444") ;
				dispose();
				// setVisible(false);
				level = 2;
				GameFrame gf = new GameFrame(level);
				// gb.newGame();
				gf.setVisible(true);

			}

		}
	}

  /*  loading game action  */
	private class LoadAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			FileDialog dialog = new FileDialog(GameFrame.this, "Load",
					FileDialog.LOAD);
			dialog.setVisible(true);
			String dir = dialog.getDirectory();
			String fileName = dialog.getFile();
			String filePath = dir + fileName;

			if (fileName != null && fileName.trim().length() != 0) {
				File file = new File(filePath);
				board.loadGameDataFromFile(file);
				// board.setVisible(true);
				repaint();
				System.out.println("repaint");
			} else {
				JOptionPane.showConfirmDialog(GameFrame.this,
						"File name is empty\nLoading game failed", "Minesweeper",
						JOptionPane.DEFAULT_OPTION);
			}

		}
	}
	
	
	  /*  Saving game action  */
	private class SaveAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			FileDialog dialog = new FileDialog(GameFrame.this, "Save",
					FileDialog.SAVE);
			dialog.setVisible(true);
			String dir = dialog.getDirectory();
			String fileName = dialog.getFile();
			String filePath = dir + fileName;
			if (fileName != null && fileName.trim().length() != 0) {
				File file = new File(filePath);
				board.saveGameDataToFile(file);
			} else {
				JOptionPane.showConfirmDialog(GameFrame.this,
						"File name is empty\nSaving game failed!", "Minesweeper",
						JOptionPane.DEFAULT_OPTION);
			}

		}
	}

	private class RecordAction implements ActionListener {

		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent event) {
			File file = new File("file.dat");
			MinesweeperRecords records = new ReadRecord()
					.readRecordsFromFile(file);
			List<Player> p = records.sortRecords();
			records.setRecords((Player[]) p.toArray());// set sorted Player[]
														// into
														// MinesweeperRecords
														// object
			JScrollPane panel = new RecordScrollPane().getReadScrollPane(
					records, file);

			JDialog recordDialog = new JDialog(GameFrame.this, "Minesweeper");
			recordDialog.setBounds(300, 300, 300, 219);

			Container container = recordDialog.getContentPane();
			container.add(panel);
			recordDialog.show();
		}
	}

	/* inner class Undoaction */
	private class UndoAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			

			if (!board.getPreState().isEmpty()) {
				System.out.println("undo");
				GameState state = (GameState) board.getPreState().pop();
				// System.out.println("Undo--prestate--field"+
				// state.getField()+"---inGame:"+state.isInGame());
				board.setField(state.getField());
				board.setInGame(state.isInGame());
				board.setMines_left(state.getMines_left());
				board.setPlayer(state.getPlayer());
				board.setLevel(state.getLevel());
				board.setStatusbar(state.getStatusbar());

				board.getCurrentState().push(state);

				repaint();
			}

		}

	}

	/* inner class Redoaction */
	private class RedoAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (!board.getCurrentState().isEmpty()) {
				GameState state = (GameState) board.getCurrentState().pop();

				board.setField(state.getField());
				board.setInGame(state.isInGame());
				board.setMines_left(state.getMines_left());
				board.setPlayer(state.getPlayer());
				board.setLevel(state.getLevel());
				board.setStatusbar(state.getStatusbar());

				board.getPreState().push(state);
				repaint();
			}

		}

	}
	
	/* inner class Solveaction */
	private class SolveAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			board.setInGame(false);
			repaint();
			}

		}

	

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				JFrame ex = new GameFrame(1);
				ex.setVisible(true);
			}
		});
	}
	//test

}