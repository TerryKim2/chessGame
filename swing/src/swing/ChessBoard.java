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
import java.util.ArrayList;
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
import swing.aiNormal;

/**
 * The AI-related ones were created by Donghwan. gameManager too.
 * 
 * @author cubby(Donghwan) && Joshua
 *
 */
public class ChessBoard extends JFrame {

	private static final int SIZE = 8; // Chess board size
	private JButton[][] squares = new JButton[SIZE][SIZE];
	private ChessPiece[][] boardState = new ChessPiece[SIZE][SIZE];
	private ChessPiece selectedPiece = null;
	private int selectedRow = -1, selectedCol = -1;
	private boolean isWhiteTurn = true; // White moves first
	private JLabel turnLabel;
	private JLabel lastMoveLabel;
	private List<String> moveList = new ArrayList<>();
	private MoveGenerator moveGenerator;
	private Color ltan = new Color(227, 193, 111);
	private Color dtan = new Color(184, 139, 74);
	public boolean turn = true;

	public boolean playerColor;// true = white, false = black
	public boolean aiColor;
	public boolean easy;// true = easy, false = normal.

	public aiNormal ai;// normal mode
	public aiEasy aiE;// easy mode

	public ChessPiece[][] state() {
		return boardState;
	}

	
	/**
	 * Initalize the chess board as buttons
	 * @param info
	 * @param difficulty
	 */
	public ChessBoard(boolean info, boolean difficulty) {
		this.playerColor = info;
		this.aiColor = !info;
		this.easy = difficulty;
		System.out.println(easy);
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
		/**Run it for the first time, and then manage it through gameManager.*/
		if (easy == true) {
			aiE = new aiEasy();
			aiE.myColor = aiColor;
			if (turn == aiColor) {
				aiE.controller(this, moveGenerator, boardState);
			}
		} else if (easy == false) {
			ai = new aiNormal();
			ai.myColor = aiColor;
			if (turn == aiColor) {
				ai.controller(this, moveGenerator, boardState);
			}
		}

	}
	/**Use controller whenever it's AI's turn based on the difficulty level.*/
	public void gameManager() {
		// if turn == true, player turn
		kingInCheckHighlight();

		if (easy == true) {
			if (turn == true) {

			} else if (turn == false) {
				aiE.controller(this, moveGenerator, boardState);
			}
		} else if (easy == false) {
			if (turn == true) {

			} else if (turn == false) {
				ai.controller(this, moveGenerator, boardState);
			}
		}
	}

	/**
	 * Place pieces on board in their respective squares while
	 * assigning each piece an icon.
	 * @param row
	 * @param col
	 */
	private void initializePieces(int row, int col) {
		String basePath = System.getProperty("user.dir") + "\\src\\pieces\\"; // Replace with the actual path
		// String basePath ="swing/src/pieces/";
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

	/**
	 * Board is created with buttons and ButtonListener looks for
	 * actions on each button to cause a movement and highlights.
	 */
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
						highlightValidMoves(row, col);
					}
				} else if (selectedPiece != null) {
					// Move piece and reset highlights
					resetSquareHighlights();
					movePiece(row, col);

				}
			}
		}
	}

	/**
	 * Highlights the square that a king currently in check is on
	 */
	public void kingInCheckHighlight() {
		Point[] kingPositions = moveGenerator.findKingPositions();
		boolean whiteKingInCheck = moveGenerator.isKingInCheck(true);
		boolean blackKingInCheck = moveGenerator.isKingInCheck(false);

		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				Color squareColor = (row + col) % 2 == 0 ? ltan : dtan;
				squares[row][col].setBackground(squareColor); // Reset to default color

				if (whiteKingInCheck && kingPositions[0].equals(new Point(row, col))
						|| blackKingInCheck && kingPositions[1].equals(new Point(row, col))) {
					highlightKingInCheck(squares[row][col]); // Highlight the king's square
				}
			}
		}
	}

	/**
	 * Highlight each possible move for a selected piece.
	 * @param row
	 * @param col
	 */
	public void highlightValidMoves(int row, int col) {

		ChessPiece selectedPiece = boardState[row][col];

		boolean kingInCheck = moveGenerator.isKingInCheck(selectedPiece.isWhite());
		List<Point> validMoves;

		if (kingInCheck) {
			validMoves = moveGenerator.getMovesThatResolveCheck(row, col, selectedPiece);
		} else {
			validMoves = moveGenerator.getValidMovesForPiece(row, col);
		}

		// Highlight the valid moves
		for (Point move : validMoves) {
			squares[move.x][move.y].setBackground(Color.GREEN); // Highlight square
		}
	}

	/**
	 * Resets highlighted squares back to their original color
	 */
	private void resetSquareHighlights() {
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				Color squareColor = (row + col) % 2 == 0 ? ltan : dtan;
				squares[row][col].setBackground(squareColor);
			}
		}
	}
	
	/**
	 * Changes background color for square that a king in check is on
	 * @param squares2
	 */
	private void highlightKingInCheck(JButton squares2) {
		squares2.setBackground(Color.RED); // Example: setting the background to red
		// You can also add other visual indicators like a border or icon
	}

	/**
	 * Temporarily flashes a square if an incorrect move is attempted. 
	 * @param row
	 * @param col
	 */
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
	
	/**
	 * Helper method to check if a king is in checkmate to signal the end
	 * of the game.
	 * @param isWhitePlayer
	 * @return
	 */
	private boolean isCheckmate(boolean isWhitePlayer) {
		System.out.print("Is ");
		System.out.print(isWhitePlayer ? "White" : "Black");
		System.out.println(" in check? : " + moveGenerator.isKingInCheck(isWhitePlayer));
		System.out.println();
		if (!moveGenerator.isKingInCheck(isWhitePlayer)) {
			return false; // Not in checkmate if the king is not in check
		}

		// Check if any move can get the king out of check
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				ChessPiece piece = boardState[row][col];
				if (piece != null && piece.isWhite() == isWhitePlayer) {
					List<Point> validMoves = moveGenerator.getValidMovesForPiece(row, col);
					for (Point move : validMoves) {
						if (!moveGenerator.movePutsKingInCheck(row, col, move.x, move.y, isWhitePlayer)) {
							return false; // Found a move that can get the king out of check
						}
					}
				}
			}
		}

		return true; // No moves available to escape check, so it's checkmate
	}

	/**
	 * Helper method to check if there is no available moves for a player
	 * signaling the end of the game in a stalemate.
	 * @param isWhitePlayer
	 * @return
	 */
	public boolean isStalemate(boolean isWhitePlayer) {
		if (moveGenerator.isKingInCheck(isWhitePlayer)) {
			return false; // Not a stalemate if the king is in check
		}

		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				ChessPiece piece = boardState[row][col];
				if (piece != null && piece.isWhite() == isWhitePlayer) {
					List<Point> validMoves = moveGenerator.getValidMovesForPiece(row, col);
					if (!validMoves.isEmpty()) {
						return false; // There is at least one legal move
					}
				}
			}
		}

		return true; // No legal moves available, and the king is not in check
	}

	/**
	 * "Main" method for checking valid moves and pawn promotions for 
	 * moves made by the player. 
	 * @param newRow
	 * @param newCol
	 */
	private void movePiece(int newRow, int newCol) {

		if (selectedPiece != null) {
			if (isValidMove(selectedRow, selectedCol, newRow, newCol) && selectedPiece.isWhite == playerColor) {

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
				if (isStalemate(!isWhiteTurn)) {
					handleStalemate();
				}
				if (isCheckmate(!isWhiteTurn)) {// Check if the opponent is in checkmate
					endGame(isWhiteTurn);
					selectedPiece = null;
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



	/**
	 * aiMove(piece)
	 * 
	 * In order to distinguish the movement of the player and ai, it was modified to
	 * be used in ai control after copying it with the movepiece method.
	 */
	public void aiMovePiece(ChessPiece k, int newRow, int newCol) {

		selectedPiece = k;

		if (selectedPiece != null) {

			if (isValidMove(k.points.x, k.points.y, newRow, newCol) && selectedPiece.isWhite == aiColor) {

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
					ChessPiece.PieceType newType = promptForAIPawnPromotion();
					String imagePath = getImagePathForPieceType(newType, selectedPiece.isWhite());
					selectedPiece = new ChessPiece(imagePath, selectedPiece.isWhite(), newType);
					boardState[newRow][newCol] = selectedPiece;
					squares[newRow][newCol].setIcon(selectedPiece.getIcon());
				}

				if (isStalemate(!isWhiteTurn)) {
					handleStalemate();
				}
				if (isCheckmate(!isWhiteTurn)) {// Check if the opponent is in checkmate
					endGame(isWhiteTurn);
					selectedPiece = null;
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

	/**
	 * Pop up window to prompt a player for pawn promotion as well as
	 * changing the pice type to the desired piece.
	 * @return
	 */
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

	/**
	 * Set to randomly select from the number of possible cases when the pawn goes
	 * all the way.
	 */
	private ChessPiece.PieceType promptForAIPawnPromotion() {
		Object[] possiblePieces = { "Queen", "Rook", "Bishop", "Knight" };
		int temp = (int) (Math.random() * possiblePieces.length);
		Object tp = possiblePieces[temp];
		if (tp == "Rook") {
			return ChessPiece.PieceType.ROOK;
		} else if (tp == "Bishop") {
			return ChessPiece.PieceType.BISHOP;
		} else if (tp == "Knight") {
			return ChessPiece.PieceType.KNIGHT;
		} else {
			return ChessPiece.PieceType.QUEEN;
		}
	}

	/**
	 * Uses directory in files to assign each piece type to a picture. 
	 * @param type
	 * @param isWhite
	 * @return
	 */
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

	/**
	 * Check if the selected piece is a pawn and the move is a valid en passant and capture
	 * @param currentRow
	 * @param currentCol
	 * @param targetRow
	 * @param targetCol
	 * @return
	 */
	private boolean isValidEnPassantMove(int currentRow, int currentCol, int targetRow, int targetCol) {
		return selectedPiece.getType() == ChessPiece.PieceType.PAWN
				&& moveGenerator.isEnPassant(currentRow, currentCol, targetRow, targetCol);
	}

	/**
	 * Resets the flag that a pawn is enpassantable
	 * @param isWhiteTurnNow
	 */
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

	/**
	 * Check if the destination square is a valid move for the selected piece
	 * @param startRow
	 * @param startCol
	 * @param endRow
	 * @param endCol
	 * @return
	 */
	public boolean isValidMove(int startRow, int startCol, int endRow, int endCol) {
		ChessPiece piece = boardState[startRow][startCol];
		if (piece == null) {
			return false; // No piece at the starting position
		}

		List<Point> validMoves = moveGenerator.getValidMovesForPiece(startRow, startCol);
		if (!validMoves.contains(new Point(endRow, endCol))) {
			return false; // Not a valid move for this piece
		}

		// Simulate the move
		ChessPiece tempPiece = boardState[endRow][endCol];
		boardState[endRow][endCol] = piece;
		boardState[startRow][startCol] = null;

		boolean isKingInCheckAfterMove = moveGenerator.isKingInCheck(piece.isWhite());

		// Undo the move
		boardState[startRow][startCol] = piece;
		boardState[endRow][endCol] = tempPiece;

		// If the move results in the king being in check, it's not valid
		return !isKingInCheckAfterMove;
	}

	/**
	 * If a stalemate is detected then the game gives the player an option
	 * to either exit the program or return to lobby
	 */
	private void handleStalemate() {
		// Notify the players of the stalemate
		int option = JOptionPane.showOptionDialog(null, "Stalemate! The game is a draw.\nWould you like to play again?",
				"Game Drawn", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
				new String[] { "Reset Board", "Close Game" }, "Reset Board");

		if (option == JOptionPane.YES_OPTION) {
			dispose();
			ChessLobby lobby = new ChessLobby();
			lobby.setVisible(true);
		} else {
			System.exit(0); // Close the application
		}
	}

	/**
	 * If a checkmate is detected then the game gives the player an option
	 * to either exit the program or return to lobby as well as informing
	 * the player to who won.
	 * @param isWhiteWinner
	 */
	private void endGame(boolean isWhiteWinner) {
		String winner = isWhiteWinner ? "White" : "Black";
		String difficulty = easy ? "easy" : "normal";
		String result = isWhiteWinner ? (playerColor ? "win" : "loss") : (playerColor ? "loss" : "win");
		FileStoreUtility.writeGameResultToFileStore(difficulty, result);
		int option = JOptionPane.showOptionDialog(null,
				"Checkmate! " + winner + " wins!\nWould you like to play again?", "Game Over",
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
				new String[] { "Reset Board", "Close Game" }, "Reset Board");

		if (option == JOptionPane.YES_OPTION) {
			// resetBoard();
			dispose();
			ChessLobby lobby = new ChessLobby();
			lobby.setVisible(true);
		} else {

			System.exit(0); // Close the application
		}
	}
	
	/**
	 * ChessPiece used for each piece on the board
	 */
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

		// I also needed quite a bit of information about the point, so I made it.
		public Point getPoints() {
			return points;
		}
	}

	// Methods for storing game information and replaying them
	private String getChessNotation(int row, int col) {
		return "" + (char) ('a' + col) + (SIZE - row);
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

}