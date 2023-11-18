package Game;

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
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

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

	public ChessBoard() {
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
					squares[row][col].setBackground(Color.LIGHT_GRAY);
				} else {
					squares[row][col].setBackground(Color.DARK_GRAY);
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
	}

	private void initializePieces(int row, int col) {
		String basePath = System.getProperty("user.dir") + "\\src\\pieces\\"; // Replace with the actual path
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
			if (selectedPiece == null && boardState[row][col] != null) {
				if ((isWhiteTurn && boardState[row][col].isWhite())
						|| (!isWhiteTurn && !boardState[row][col].isWhite())) {
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

	private void highlightValidMoves() {
		List<Point> validMoves;
		if (selectedPiece.type == ChessPiece.PieceType.PAWN) { // Assuming PAWN
			validMoves = getValidPawnMoves(selectedRow, selectedCol);
			for (Point move : validMoves) {
				squares[move.x][move.y].setBackground(Color.GREEN); // Highlight square
			}
		}
		// Add similar conditions for other pieces
	}

	private void resetSquareHighlights() {
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				Color squareColor = (row + col) % 2 == 0 ? Color.LIGHT_GRAY : Color.DARK_GRAY;
				squares[row][col].setBackground(squareColor);
			}
		}
	}

	private void movePiece(int newRow, int newCol) {
		if (selectedPiece != null && isValidMove(selectedRow, selectedCol, newRow, newCol)) {
			// Update board state
			boardState[newRow][newCol] = selectedPiece;
			boardState[selectedRow][selectedCol] = null;

			// Update GUI
			squares[newRow][newCol].setIcon(selectedPiece.getIcon());
			squares[selectedRow][selectedCol].setIcon(null);

			// Switch turns and update the turn label
	        isWhiteTurn = !isWhiteTurn;
	        turnLabel.setText(isWhiteTurn ? "White's turn" : "Black's turn");

	        // Update the last move label
	        String move = getChessNotation(selectedRow, selectedCol) + " to " + getChessNotation(newRow, newCol);
	        lastMoveLabel.setText("Last Move: " + move);


			// Reset selection and highlights
			selectedPiece = null;
			selectedRow = -1;
			selectedCol = -1;
			resetSquareHighlights();
		} else {
			selectedPiece = null;
			selectedRow = -1;
			selectedCol = -1;
			resetSquareHighlights();
			indicateWrongMove(newRow, newCol);
		}
	}
	
	private boolean isValidMove(int startX, int startY, int endX, int endY) {
		List<Point> temp = new ArrayList<>();
		temp.add(new Point(endX, endY));
		if (selectedPiece.type == ChessPiece.PieceType.PAWN) {
			System.out.println("selected a pawn");
			List<Point> pawn = new ArrayList<>(getValidPawnMoves(startX, startY));
			for (int i = 0; i < pawn.size(); i++) {
				System.out.println("temp: " + temp.get(0));
				System.out.println(pawn.get(i));
				if (temp.get(0).equals(pawn.get(i))) {
					System.out.println("test");
					return true;
				}
			}

		}
		System.out.println("returning false");
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

	private List<Point> getValidPawnMoves(int row, int col) {
		List<Point> validMoves = new ArrayList<>();
		int direction = boardState[row][col].isWhite() ? -1 : 1; // White moves up, Black moves down
		int startRow = boardState[row][col].isWhite() ? 6 : 1; // Starting rows for White and Black

		// Move one square forward
		if (isSquareEmpty(row + direction, col)) {
			validMoves.add(new Point(row + direction, col));

			// Move two squares forward from the starting position
			if (row == startRow && isSquareEmpty(row + 2 * direction, col)) {
				validMoves.add(new Point(row + 2 * direction, col));
			}
		}

		// Capture diagonally
		if (isWithinBoard(row + direction, col - 1) && !isSquareEmpty(row + direction, col - 1)
				&& boardState[row][col].isWhite() != boardState[row + direction][col - 1].isWhite()) {
			validMoves.add(new Point(row + direction, col - 1));
		}
		if (isWithinBoard(row + direction, col + 1) && !isSquareEmpty(row + direction, col + 1)
				&& boardState[row][col].isWhite() != boardState[row + direction][col + 1].isWhite()) {
			validMoves.add(new Point(row + direction, col + 1));
		}

		return validMoves;
	}

	private boolean isSquareEmpty(int row, int col) {
		return isWithinBoard(row, col) && boardState[row][col] == null;
	}

	private boolean isWithinBoard(int row, int col) {
		return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
	}

	class ChessPiece {
		private ImageIcon icon;
		private boolean isWhite;
		private PieceType type;

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
	}

	
	
	// Methods for storing game information and replaying them
	private String getChessNotation(int row, int col) {
	    return "" + (char)('a' + col) + (SIZE - row);
	}
	
	private void saveMovesToFile(String filename) {		// Call at the end of a game
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
	        for (String move : moveList) {
	            writer.write(move);
	            writer.newLine();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	private void replayMovesFromFile(String filename) {		//Call from lobby???
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
	    // This will involve parsing the string, finding the corresponding squares on the board,
	    // and then moving the pieces accordingly.
		// Possibly include either a timer to slow the movements or a button to go step by step. 
	}

	
	
	
	public static void main(String[] args) throws IOException {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ChessBoard chessBoard = new ChessBoard();
				chessBoard.setVisible(true);
			}
		});
	}
}
