package swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import swing.ChessLobby;
import swing.aiControl;

public class ChessBoard extends JFrame {

	private static final int SIZE = 8; // Chess board size
	private JButton[][] squares = new JButton[SIZE][SIZE];
	private ChessPiece[][] boardState = new ChessPiece[SIZE][SIZE];
	private ChessPiece selectedPiece = null;
	private int selectedRow = -1, selectedCol = -1;
	private boolean isWhiteTurn = true; // White moves first
	private JLabel statusLabel;
	private JLabel turnLabel;
	private JLabel lastMoveLabel;
	private List<String> moveList = new ArrayList<>();

	private MoveGenerator moveGenerator;
	private Color ltan = new Color(227, 193, 111);
	private Color dtan = new Color(184, 139, 74);
	public boolean turn = true;

	public boolean playerColor;// true = white, false = black
	public boolean aiColor;

	public aiControl ai;

	public ChessPiece[][] d() {
		return boardState;
	}

	public ChessBoard(boolean info) {
		this.playerColor = info;
		this.aiColor = !info;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 500);
		setLayout(new BorderLayout());

		JPanel chessBoardPanel = new JPanel(new GridLayout(SIZE, SIZE));
		add(chessBoardPanel, BorderLayout.CENTER);

		// Initialize buttons and board state
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				squares[row][col] = new JButton();
				squares[row][col].addActionListener(new ButtonListener(row, col));
				if ((row + col) % 2 == 0) {
					squares[row][col].setBackground(ltan);
				} else {
					squares[row][col].setBackground(dtan);
				}
				chessBoardPanel.add(squares[row][col]);

				// Initialize pieces with images
				initializePieces(row, col);
			}
		}

		// Status Bar
		JPanel statusPanel = new JPanel(new BorderLayout());
		turnLabel = new JLabel("White's turn", SwingConstants.CENTER);
		lastMoveLabel = new JLabel("Last Move: None", SwingConstants.LEFT);

		statusPanel.add(turnLabel, BorderLayout.CENTER);
		statusPanel.add(lastMoveLabel, BorderLayout.WEST);

		add(statusPanel, BorderLayout.SOUTH);

		moveGenerator = new MoveGenerator(boardState);
		ai = new aiControl();
		ai.myColor = aiColor;
		if (turn == aiColor) {
			ai.controller(this, moveGenerator, boardState);
		}

	}


	
	public void gameManager() {
		//turn true면 플레이어 턴
		if(turn == true) {
			
		} else if (turn == false) {
			ai.controller(this, moveGenerator, boardState);
		}
	}

	private void initializePieces(int row, int col) {
		String basePath = "swing/src/pieces/"; // Replace with the actual path
		boolean isWhite = !(row < 2); // White pieces are in the first two rows
		String colorPrefix = isWhite ? "w" : "b"; // 'w' for white, 'b' for black

		if (row == 1 || row == 6) {
			boardState[row][col] = new ChessPiece(basePath + colorPrefix + "pawn.png", isWhite,
					ChessPiece.PieceType.PAWN);
		} else if (row == 0 || row == 7) {
			if (col == 0 || col == 7) {
				boardState[row][col] = new ChessPiece(basePath + colorPrefix + "rook.png", isWhite,
						ChessPiece.PieceType.ROOK);
			} else if (col == 1 || col == 6) {
				boardState[row][col] = new ChessPiece(basePath + colorPrefix + "knight.png", isWhite,
						ChessPiece.PieceType.KNIGHT);
			} else if (col == 2 || col == 5) {
				boardState[row][col] = new ChessPiece(basePath + colorPrefix + "bishop.png", isWhite,
						ChessPiece.PieceType.BISHOP);
			} else if (col == 3) {
				boardState[row][col] = new ChessPiece(basePath + colorPrefix + "queen.png", isWhite,
						ChessPiece.PieceType.QUEEN);
			} else if (col == 4) {
				boardState[row][col] = new ChessPiece(basePath + colorPrefix + "king.png", isWhite,
						ChessPiece.PieceType.KING);
			}
		}

		if (boardState[row][col] != null) {
			squares[row][col].setIcon(boardState[row][col].getIcon());
		}
	}

	private class ButtonListener implements ActionListener {
		private int row, col;

		public ButtonListener(int row, int col) {
			this.row = row;
			this.col = col;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (turn == true) {
				if (selectedPiece == null && boardState[row][col] != null) {
					if ((isWhiteTurn && boardState[row][col].isWhite() && playerColor == true)
							|| (!isWhiteTurn && !boardState[row][col].isWhite() && playerColor == false)) {
						selectedPiece = boardState[row][col];
						selectedRow = row;
						selectedCol = col;
						highlightValidMoves();
					}
				} else if (selectedPiece != null) {
					// Move piece and reset highlights
					resetSquareHighlights();
					movePiece(row, col);
					
				}
			}
		}
	}

	private void highlightValidMoves() {
		List<Point> validMoves;
		switch (selectedPiece.getType()) {
		case PAWN:
			validMoves = moveGenerator.getValidPawnMoves(selectedRow, selectedCol);
			break;
		case ROOK:
			validMoves = moveGenerator.getValidRookMoves(selectedRow, selectedCol);
			if(validMoves.isEmpty()) {
			System.out.println("empty");}
			break;
		case BISHOP:
			validMoves = moveGenerator.getValidBishopMoves(selectedRow, selectedCol);
			break;
		case QUEEN:
			validMoves = moveGenerator.getValidQueenMoves(selectedRow, selectedCol);
			break;
		case KING:
			validMoves = moveGenerator.getValidKingMoves(selectedRow, selectedCol);
			break;
		case KNIGHT:
			validMoves = moveGenerator.getValidKnightMoves(selectedRow, selectedCol);
			break;
		default:
			validMoves = new ArrayList<>();
			break;
		}

		for (Point move : validMoves) {
			squares[move.x][move.y].setBackground(Color.GREEN); // Highlight square
		}
		// Add similar conditions for other pieces
	}

	private void resetSquareHighlights() {
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				Color squareColor = (row + col) % 2 == 0 ? ltan : dtan;
				squares[row][col].setBackground(squareColor);
			}
		}
	}

	private void movePiece(int newRow, int newCol) {
		if (selectedPiece != null) {
			if (isValidMove(selectedRow, selectedCol, newRow, newCol, selectedPiece)
					&& selectedPiece.isWhite == playerColor) {

				if (selectedPiece.getType() == ChessPiece.PieceType.PAWN && Math.abs(newRow - selectedRow) == 2) {
					selectedPiece.pawnPassant(true);
				}

				if (isValidEnPassantMove(selectedRow, selectedCol, newRow, newCol)) {
					// Remove the captured pawn in en passant
					int capturedPawnRow = selectedRow; // The row where the captured pawn is
					int capturedPawnCol = newCol; // The column to which the capturing pawn moved
					boardState[capturedPawnRow][capturedPawnCol] = null;
					squares[capturedPawnRow][capturedPawnCol].setIcon(null);
				}

				// Move the pawn
				boardState[newRow][newCol] = selectedPiece;
				boardState[selectedRow][selectedCol] = null;
				squares[newRow][newCol].setIcon(selectedPiece.getIcon());
				squares[selectedRow][selectedCol].setIcon(null);

				if (selectedPiece.getType() == ChessPiece.PieceType.PAWN && (newRow == 0 || newRow == SIZE - 1)) {
					ChessPiece.PieceType newType = promptForPawnPromotion();
					String imagePath = getImagePathForPieceType(newType, selectedPiece.isWhite());
					selectedPiece = new ChessPiece(imagePath, selectedPiece.isWhite(), newType);
					boardState[newRow][newCol] = selectedPiece;
					squares[newRow][newCol].setIcon(selectedPiece.getIcon());
				}

				// Switch turns and update the turn label
				isWhiteTurn = !isWhiteTurn;
				turnLabel.setText(isWhiteTurn ? "White's turn" : "Black's turn");

				resetDoubleStepFlags(isWhiteTurn);

				// Update the last move label
				String move = getChessNotation(selectedRow, selectedCol) + " to " + getChessNotation(newRow, newCol);
				lastMoveLabel.setText("Last Move: " + move);

				// Reset selection and highlights
				selectedPiece = null;
				selectedRow = -1;
				selectedCol = -1;
				resetSquareHighlights();
				turn = false;
				gameManager();
			} else {
				selectedPiece = null;
				selectedRow = -1;
				selectedCol = -1;
				resetSquareHighlights();
				indicateWrongMove(newRow, newCol);
				gameManager();
			}
		}
	}
	
	//aiMove(piece)
	/*In order to distinguish the movement of the player and ai, 
	 * it was modified to be used in ai control after copying it with the movepiece method.
	 */
	public void aiMovePiece(ChessPiece k, int newRow, int newCol) {
		selectedPiece = k;

		if (selectedPiece != null) {
			
			if (isValidMove(k.points.x, k.points.y, newRow, newCol, selectedPiece)
					&& selectedPiece.isWhite == aiColor) {

				if (selectedPiece.getType() == ChessPiece.PieceType.PAWN && Math.abs(newRow - k.points.y) == 2) {
					selectedPiece.pawnPassant(true);
				}

				if (isValidEnPassantMove(k.points.x, k.points.y, newRow, newCol)) {
					// Remove the captured pawn in en passant
					int capturedPawnRow = k.points.x; // The row where the captured pawn is
					int capturedPawnCol = newCol; // The column to which the capturing pawn moved
					boardState[capturedPawnRow][capturedPawnCol] = null;
					squares[capturedPawnRow][capturedPawnCol].setIcon(null);
				}
				

				// Move the pawn
				boardState[newRow][newCol] = selectedPiece;
				boardState[k.points.x][k.points.y] = null;
				squares[newRow][newCol].setIcon(selectedPiece.getIcon());
				squares[k.points.x][k.points.y].setIcon(null);
				
				if (selectedPiece.getType() == ChessPiece.PieceType.PAWN && (newRow == 0 || newRow == SIZE - 1)) {
					ChessPiece.PieceType newType = promptForPawnPromotion();
					String imagePath = getImagePathForPieceType(newType, selectedPiece.isWhite());
					selectedPiece = new ChessPiece(imagePath, selectedPiece.isWhite(), newType);
					boardState[newRow][newCol] = selectedPiece;
					squares[newRow][newCol].setIcon(selectedPiece.getIcon());
				}

				// Switch turns and update the turn label
				isWhiteTurn = !isWhiteTurn;
				turnLabel.setText(isWhiteTurn ? "White's turn" : "Black's turn");

				resetDoubleStepFlags(isWhiteTurn);

				// Update the last move label
				String move = getChessNotation(k.points.x, k.points.y) + " to " + getChessNotation(newRow, newCol);
				lastMoveLabel.setText("Last Move: " + move);

				// Reset selection and highlights
				selectedPiece = null;
				k.points.x = -1;
				k.points.y = -1;
				resetSquareHighlights();
				turn = true;
				gameManager();
			} else {
				selectedPiece = null;
				k.points.x = -1;
				k.points.y = -1;
				resetSquareHighlights();
				indicateWrongMove(newRow, newCol);
				gameManager();
			}
		}
	}

	private ChessPiece.PieceType promptForPawnPromotion() {
		Object[] possiblePieces = { "Queen", "Rook", "Bishop", "Knight" };
		String chosenPiece = (String) JOptionPane.showInputDialog(this, "Choose piece for pawn promotion:",
				"Pawn Promotion", JOptionPane.PLAIN_MESSAGE, null, possiblePieces, "Queen");

		switch (chosenPiece) {
		case "Rook":
			return ChessPiece.PieceType.ROOK;
		case "Bishop":
			return ChessPiece.PieceType.BISHOP;
		case "Knight":
			return ChessPiece.PieceType.KNIGHT;
		default:
			return ChessPiece.PieceType.QUEEN;
		}
	}

	private String getImagePathForPieceType(ChessPiece.PieceType type, boolean isWhite) {
		String basePath = System.getProperty("user.dir") + "\\src\\pieces\\";
		String colorPrefix = isWhite ? "w" : "b";
		switch (type) {
		case ROOK:
			return basePath + colorPrefix + "rook.png";
		case BISHOP:
			return basePath + colorPrefix + "bishop.png";
		case KNIGHT:
			return basePath + colorPrefix + "knight.png";
		case QUEEN:
		default:
			return basePath + colorPrefix + "queen.png";
		}
	}

	private boolean isValidEnPassantMove(int currentRow, int currentCol, int targetRow, int targetCol) {
		// Check if the selected piece is a pawn and the move is a valid en passant
		// capture
		return selectedPiece.getType() == ChessPiece.PieceType.PAWN
				&& moveGenerator.isEnPassant(currentRow, currentCol, targetRow, targetCol);
	}

	private void resetDoubleStepFlags(boolean isWhiteTurnNow) {
		for (int r = 0; r < SIZE; r++) {
			for (int c = 0; c < SIZE; c++) {
				ChessPiece piece = boardState[r][c];
				if (piece != null && piece.getType() == ChessPiece.PieceType.PAWN
						&& piece.isWhite() == isWhiteTurnNow) {
					piece.pawnPassant(false);
				}
			}
		}
	}

	public boolean isValidMove(int startX, int startY, int endX, int endY, ChessPiece piece) {

		Point temp = new Point(endX, endY);
		System.out.println("Input Move: (" + temp.x + "," + temp.y + ")");

		if (selectedPiece.type == ChessPiece.PieceType.PAWN) {
			System.out.println("Selected a pawn.");
			List<Point> pawn = new ArrayList<>(moveGenerator.getValidPawnMoves(startX, startY));
			System.out.print("Possible Moves: ");
			for (Point p : pawn) {
				System.out.print("(" + p.x + "," + p.y + ")");
				if (temp.equals(p)) {
					System.out.println("\nReturning True.");
					return true;
				}
			}
		} else if (selectedPiece.type == ChessPiece.PieceType.ROOK) {
			System.out.println("Selected a rook.");
			List<Point> rook = new ArrayList<>(moveGenerator.getValidRookMoves(startX, startY));
			System.out.print("Possible Moves: ");
			for (Point p : rook) {
				System.out.print("(" + p.x + "," + p.y + ")");
				if (temp.equals(p)) {
					System.out.println("\nReturning True.");
					return true;
				}
			}

		} else if (selectedPiece.type == ChessPiece.PieceType.BISHOP) {
			System.out.println("Selected a bishop.");
			List<Point> bishop = new ArrayList<>(moveGenerator.getValidBishopMoves(startX, startY));
			System.out.print("Possible Moves: ");
			for (Point p : bishop) {
				System.out.print("(" + p.x + "," + p.y + ")");
				if (temp.equals(p)) {
					System.out.println("\nReturning True.");
					return true;
				}
			}
		} else if (selectedPiece.type == ChessPiece.PieceType.QUEEN) {
			System.out.println("Selected a queen.");
			List<Point> queen = new ArrayList<>(moveGenerator.getValidQueenMoves(startX, startY));
			System.out.print("Possible Moves: ");
			for (Point p : queen) {
				System.out.print("(" + p.x + "," + p.y + ")");
				if (temp.equals(p)) {
					System.out.println("\nReturning True.");
					return true;
				}
			}
		} else if (selectedPiece.type == ChessPiece.PieceType.KING) {
			System.out.println("Selected a king.");
			List<Point> king = new ArrayList<>(moveGenerator.getValidKingMoves(startX, startY));
			System.out.print("Possible Moves: ");
			for (Point p : king) {
				System.out.print("(" + p.x + "," + p.y + ")");
				if (temp.equals(p)) {
					System.out.println("\nReturning True.");
					return true;
				}
			}
		} else if (selectedPiece.type == ChessPiece.PieceType.KNIGHT) {
			System.out.println("Selected a knight.");
			List<Point> knight = new ArrayList<>(moveGenerator.getValidKnightMoves(startX, startY));
			System.out.print("Possible Moves: ");
			for (Point p : knight) {
				System.out.print("(" + p.x + "," + p.y + ")");
				if (temp.equals(p)) {
					System.out.println("\nReturning True.");
					return true;
				}
			}
		}
		System.out.println("\nReturning false.");
		return false;
	}

	private void indicateWrongMove(int row, int col) {
		final int flashCount = 2; // Total number of flashes
		final int delay = 35; // Delay in milliseconds for each flash

		Color originalColor = squares[row][col].getBackground();
		Timer timer = new Timer(delay, null);
		timer.addActionListener(new ActionListener() {
			private int count = 0;
			private boolean isRed = false;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (count >= flashCount * 2) {
					squares[row][col].setBackground(originalColor);
					timer.stop();
				} else {
					squares[row][col].setBackground(isRed ? originalColor : Color.RED);
					isRed = !isRed;
					count++;
				}
			}
		});
		timer.start();
	}

	public class ChessPiece {
		private ImageIcon icon;
		private boolean isWhite;
		private PieceType type;
		private boolean madeDoubleMove = false;
		public Point points;

		public enum PieceType {
			PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING
		}

		public ChessPiece(String imagePath, boolean isWhite, PieceType type) {
			this.isWhite = isWhite;
			this.icon = new ImageIcon(imagePath);
			this.type = type;
		}

		public ImageIcon getIcon() {
			return icon;
		}

		public boolean isWhite() {
			return isWhite;
		}

		public PieceType getType() {
			return type;
		}

		public void pawnPassant(boolean doubleMove) {
			this.madeDoubleMove = doubleMove;
		}

		public boolean doubleMoved() {
			return madeDoubleMove;
		}
		//I also needed quite a bit of information about the point, so I made it.
		public Point getPoints() {
			return points;
		}
	}

	// Methods for storing game information and replaying them
	private String getChessNotation(int row, int col) {
		return "" + (char) ('a' + col) + (SIZE - row);
	}


	//TODO call at the end of the game
	private void saveGameResult(String result) throws SQLException {
		ResultSet rs = DBManager.executeSQLquery("INSERT INTO games VALUES(DATE(\'now\'), TIME(\'now\'),"+difficulty+","+result);
	}

	private void saveMovesToFile(String filename) { // Call at the end of a game
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
			for (String move : moveList) {
				writer.write(move);
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void replayMovesFromFile(String filename) { // Call from lobby???
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = reader.readLine()) != null) {
				// Process and simulate each move
				simulateMove(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void simulateMove(String moveNotation) {
		// Extract starting and ending positions from moveNotation and simulate the move
		// This will involve parsing the string, finding the corresponding squares on
		// the board,
		// and then moving the pieces accordingly.
		// Possibly include either a timer to slow the movements or a button to go step
		// by step.
	}


	/*
	 * public static void main(String[] args) throws IOException {
	 * 
	 * SwingUtilities.invokeLater(new Runnable() {
	 * 
	 * @Override public void run() { ChessBoard chessBoard = new ChessBoard();
	 * chessBoard.setVisible(true); } }); }
	 */

}
