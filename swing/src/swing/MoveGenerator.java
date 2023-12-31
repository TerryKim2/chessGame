package swing;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import swing.ChessBoard.ChessPiece;

/**
 * Move manager for generating lists of possible moves for each piece. 
 * @author Joshua
 */


public class MoveGenerator {
	private ChessPiece[][] boardState;
	private static final int SIZE = 8;
	public MoveGenerator(ChessPiece[][] boardState) {
		this.boardState = boardState;
	}

	/**
	 * Generates all possible moves for knight
	 * @param row
	 * @param col
	 * @return
	 */
	public List<Point> getValidKnightMoves(int row, int col) {
	    List<Point> validMoves = new ArrayList<>();
	    int[][] moves = {
	        {-2, -1}, {-2, 1}, // Upwards L-shapes
	        {-1, -2}, {-1, 2}, // Left and right L-shapes
	        {1, -2}, {1, 2},   // Left and right L-shapes
	        {2, -1}, {2, 1}    // Downwards L-shapes
	    };

	    for (int[] move : moves) {
	        int newRow = row + move[0];
	        int newCol = col + move[1];

	        if (isWithinBoard(newRow, newCol) && 
	            (isSquareEmpty(newRow, newCol) || isOpponentPiece(newRow, newCol, boardState[row][col].isWhite()))) {
	            validMoves.add(new Point(newRow, newCol));
	        }
	    }

	    return validMoves;
	}

	/**
	 * Generates all possible moves for king while filtering out
	 * moves that would result in a check
	 * @param row
	 * @param col
	 * @return
	 */
	public List<Point> getValidKingMoves(int row, int col) {
		boolean isWhite = boardState[row][col].isWhite();
        List<Point> validMoves = new ArrayList<>();
        int[] rowOffsets = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] colOffsets = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < rowOffsets.length; i++) {
            int newRow = row + rowOffsets[i];
            int newCol = col + colOffsets[i];

            if (isWithinBoard(newRow, newCol) &&
                (isSquareEmpty(newRow, newCol) || isOpponentPiece(newRow, newCol, isWhite)) &&
                !isSquareUnderAttack(newRow, newCol, isWhite)) {
                validMoves.add(new Point(newRow, newCol));
            }
        }

        return validMoves.stream()
                .filter(move -> !isSquareUnderAttack(move.x, move.y, boardState[row][col].isWhite()))
                .collect(Collectors.toList());
    }
	
	/**
	 * Helper method for the getValidKingMoves method to ensure that
	 * the king is not moving into check
	 * @param row
	 * @param col
	 * @param isWhiteKing
	 * @return
	 */
	private boolean isSquareUnderAttack(int row, int col, boolean isWhiteKing) {
	    // Check for threats from knights, rooks, bishops, queens, and king
	    for (int r = 0; r < SIZE; r++) {
	        for (int c = 0; c < SIZE; c++) {
	            ChessPiece piece = boardState[r][c];
	            if (piece != null && piece.isWhite() != isWhiteKing && piece.getType() != ChessPiece.PieceType.KING) {
	                // Exclude pawns from this check; handle them separately
	                if (piece.getType() != ChessPiece.PieceType.PAWN) {
	                    List<Point> opponentMoves = getValidMovesForPiece(r, c);
	                    if (opponentMoves.contains(new Point(row, col))) {
	                        return true; // Square is under attack by non-pawn piece
	                    }
	                }
	            }
	        }
	    }

	    // Check specifically for pawn threats
	    int pawnRowOffset = isWhiteKing ? -1 : 1; // Pawns attack in the opposite direction of their movement
	    int[] pawnColOffsets = {-1, 1};
	    for (int colOffset : pawnColOffsets) {
	        int pawnRow = row + pawnRowOffset;
	        int pawnCol = col + colOffset;
	        if (isWithinBoard(pawnRow, pawnCol)) {
	            ChessPiece pawn = boardState[pawnRow][pawnCol];
	            if (pawn != null && pawn.getType() == ChessPiece.PieceType.PAWN && pawn.isWhite() != isWhiteKing) {
	                return true; // The square is attacked by an opponent's pawn
	            }
	        }
	    }

	    return false; // Square is not under attack
	}


	/**
	 * Generates all possible moves for bishop
	 * @param row
	 * @param col
	 * @return
	 */
	public List<Point> getValidBishopMoves(int row, int col) {
		List<Point> validMoves = new ArrayList<>();

		// Check all four diagonal directions
		// Up-Left
		for (int r = row - 1, c = col - 1; isWithinBoard(r, c); r--, c--) {
			if (!addMoveIfValid(validMoves, r, c, boardState[row][col].isWhite()))
				break;
		}

		// Up-Right
		for (int r = row - 1, c = col + 1; isWithinBoard(r, c); r--, c++) {
			if (!addMoveIfValid(validMoves, r, c, boardState[row][col].isWhite()))
				break;
		}

		// Down-Left
		for (int r = row + 1, c = col - 1; isWithinBoard(r, c); r++, c--) {
			if (!addMoveIfValid(validMoves, r, c, boardState[row][col].isWhite()))
				break;
		}

		// Down-Right
		for (int r = row + 1, c = col + 1; isWithinBoard(r, c); r++, c++) {
			if (!addMoveIfValid(validMoves, r, c, boardState[row][col].isWhite()))
				break;
		}

		return validMoves;
	}

	/**
	 * Generates all possible moves for rook
	 * @param row
	 * @param col
	 * @return
	 */
	public List<Point> getValidRookMoves(int row, int col) {
		List<Point> validMoves = new ArrayList<>();

		for (int r = row - 1; r >= 0; r--) {
			if (!isSquareEmpty(r, col)) {
				if (boardState[r][col].isWhite() != boardState[row][col].isWhite()) {
					validMoves.add(new Point(r, col));
				}
				break;
			}
			validMoves.add(new Point(r, col));
		}

		// Down
		for (int r = row + 1; r < SIZE; r++) {
			if (!isSquareEmpty(r, col)) {
				if (boardState[r][col].isWhite() != boardState[row][col].isWhite()) {
					validMoves.add(new Point(r, col));
				}
				break;
			}
			validMoves.add(new Point(r, col));
		}

		// Left
		for (int c = col - 1; c >= 0; c--) {
			if (!isSquareEmpty(row, c)) {
				if (boardState[row][c].isWhite() != boardState[row][col].isWhite()) {
					validMoves.add(new Point(row, c));
				}
				break;
			}
			validMoves.add(new Point(row, c));
		}

		// Right
		for (int c = col + 1; c < SIZE; c++) {
			if (!isSquareEmpty(row, c)) {
				if (boardState[row][c].isWhite() != boardState[row][col].isWhite()) {
					validMoves.add(new Point(row, c));
				}
				break;
			}
			validMoves.add(new Point(row, c));
		}

		return validMoves;
	}

	/**
	 * Generates all possible moves for queen by calling both the
	 * rook and bishop methods
	 * @param row
	 * @param col
	 * @return
	 */
	public List<Point> getValidQueenMoves(int row, int col) {
		List<Point> validMoves = new ArrayList<>();

		// Add moves as a rook
		validMoves.addAll(getValidRookMoves(row, col));

		// Add moves as a bishop
		validMoves.addAll(getValidBishopMoves(row, col));

		return validMoves;
	}

	/**
	 * Generates all possible moves for pawns
	 * @param row
	 * @param col
	 * @return
	 */
	public List<Point> getValidPawnMoves(int row, int col) {
		List<Point> validMoves = new ArrayList<>();
		int direction = boardState[row][col].isWhite() ? -1 : 1; // White moves up (-1), Black moves down (+1)
		int startRow = boardState[row][col].isWhite() ? 6 : 1; // Starting rows for White and Black
		int nextRow = row + direction;

		// Standard forward move
		if (isSquareEmpty(row + direction, col)) {
			validMoves.add(new Point(row + direction, col));

			// Double step from starting position
			if (row == startRow && isSquareEmpty(row + 2 * direction, col)) {
				validMoves.add(new Point(row + 2 * direction, col));
			}
		}

		for (int newCol : new int[] { col - 1, col + 1 }) {
			if (isWithinBoard(nextRow, newCol)) {
				// Standard capture
				if (isOpponentPiece(nextRow, newCol, boardState[row][col].isWhite())) {
					validMoves.add(new Point(nextRow, newCol));
				}
				// En passant capture
				else if (isEnPassant(row, col, nextRow, newCol)) {
					validMoves.add(new Point(nextRow, newCol));
				}
			}
		}

		return validMoves;
	}

	/**
	 * En passant is possible only if the pawn moves diagonally to an empty square
	 * @param currentRow
	 * @param currentCol
	 * @param targetRow
	 * @param targetCol
	 * @return
	 */
	public boolean isEnPassant(int currentRow, int currentCol, int targetRow, int targetCol) {
		if (isSquareEmpty(targetRow, targetCol) && Math.abs(targetCol - currentCol) == 1) {
			// The row where we expect to find the opponent's pawn
			int opponentPawnRow = currentRow;

			// Check if there is an opponent's pawn in the expected square
			if (isWithinBoard(opponentPawnRow, targetCol) && boardState[opponentPawnRow][targetCol] != null) {
				ChessPiece adjacentPiece = boardState[opponentPawnRow][targetCol];
				return adjacentPiece.getType() == ChessPiece.PieceType.PAWN
						&& adjacentPiece.isWhite() != boardState[currentRow][currentCol].isWhite()
						&& adjacentPiece.doubleMoved();
			}
		}
		return false;
	}

	/**
	 * Helper method to ensure a square contains an enemy piece when taking a piece
	 * @param row
	 * @param col
	 * @param isWhite
	 * @return
	 */
	private boolean isOpponentPiece(int row, int col, boolean isWhite) {
		return !isSquareEmpty(row, col) && boardState[row][col].isWhite() != isWhite;
	}

	/**
	 * Helper method to check if a square contains a piece
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean isSquareEmpty(int row, int col) {
		return isWithinBoard(row, col) && boardState[row][col] == null;
	}

	/**
	 * Ensures that movements outside the scope of the board are not allowed
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean isWithinBoard(int row, int col) {
		return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
	}

	/**
	 * Helper method for diagonal movement for bishops
	 * @param moves
	 * @param row
	 * @param col
	 * @param isWhite
	 * @return
	 */
	private boolean addMoveIfValid(List<Point> moves, int row, int col, boolean isWhite) {
		if (isSquareEmpty(row, col)) {
			moves.add(new Point(row, col));
			return true; // Continue in this direction
		} else if (boardState[row][col].isWhite() != isWhite) {
			moves.add(new Point(row, col)); // Capture
		}
		return false; // Stop in this direction
	}

	/**
	 * Simulates movement to see if a move will land a king in check
	 * @param startRow
	 * @param startCol
	 * @param endRow
	 * @param endCol
	 * @param isWhite
	 * @return
	 */
	public boolean movePutsKingInCheck(int startRow, int startCol, int endRow, int endCol, boolean isWhite) {
	    // Simulate the move
	    ChessPiece tempPiece = boardState[endRow][endCol];
	    boardState[endRow][endCol] = boardState[startRow][startCol];
	    boardState[startRow][startCol] = null;

	    // Check if the king is in check after this move
	    boolean isInCheck = isKingInCheck(isWhite);

	    // Undo the move
	    boardState[startRow][startCol] = boardState[endRow][endCol];
	    boardState[endRow][endCol] = tempPiece;

	    return isInCheck;
	}

	/**
	 * Checks to see if the current players king is in check.
	 * @param isWhiteKing
	 * @return
	 */
	public boolean isKingInCheck(boolean isWhiteKing) {
	    // Find the king's position
	    int kingRow = -1, kingCol = -1;
	    for (int row = 0; row < SIZE; row++) {
	        for (int col = 0; col < SIZE; col++) {
	            ChessPiece piece = boardState[row][col];
	            if (piece != null && piece.isWhite() == isWhiteKing && piece.getType() == ChessPiece.PieceType.KING) {
	                kingRow = row;
	                kingCol = col;
	                break;
	            }
	        }
	        if (kingRow != -1) break;
	    }

	    // Check if any opponent's piece can attack the king's position
	    for (int row = 0; row < SIZE; row++) {
	        for (int col = 0; col < SIZE; col++) {
	            ChessPiece piece = boardState[row][col];
	            if (piece != null && piece.isWhite() != isWhiteKing) {
	                List<Point> moves = getValidMovesForPiece(row, col);
	                for (Point move : moves) {
	                    if (move.x == kingRow && move.y == kingCol) {
	                        return true; // The king is in check
	                    }
	                }
	            }
	        }
	    }

	    return false; // The king is not in check
	}
	
	/**
	 * Generates a list of moves that would get a king out of check. This allows
	 * for the only available moves if a king is in check to be moves that exit
	 * check.
	 * @param row
	 * @param col
	 * @param piece
	 * @return
	 */
	public List<Point> getMovesThatResolveCheck(int row, int col, ChessPiece piece) {
	    List<Point> movesThatResolveCheck = new ArrayList<>();
	    List<Point> validMoves = getValidMovesForPiece(row, col);

	    for (Point move : validMoves) {
	        // Simulate the move
	        ChessPiece tempPiece = boardState[move.x][move.y];
	        boardState[move.x][move.y] = piece;
	        boardState[row][col] = null;

	        if (!isKingInCheck(piece.isWhite())) {
	            movesThatResolveCheck.add(move);
	        }

	        // Undo the move
	        boardState[row][col] = piece;
	        boardState[move.x][move.y] = tempPiece;
	    }

	    return movesThatResolveCheck;
	}

	/**
	 * Helper method to call each piece for their list of moves
	 * @param row
	 * @param col
	 * @return
	 */
	public List<Point> getValidMovesForPiece(int row, int col) {
	    ChessPiece piece = boardState[row][col];
	    if (piece == null) return Collections.emptyList();

	    switch (piece.getType()) {
	        case PAWN: return getValidPawnMoves(row, col);
	        case ROOK: return getValidRookMoves(row, col);
	        case KNIGHT: return getValidKnightMoves(row, col);
	        case BISHOP: return getValidBishopMoves(row, col);
	        case QUEEN: return getValidQueenMoves(row, col);
	        case KING: return getValidKingMoves(row, col);
	        default: return Collections.emptyList();
	    }
	}

	/**
	 * helper method to find position of both kings to ensure kings cannot
	 * enter squares nex to each other.
	 * @return
	 */
	public Point[] findKingPositions() {
        Point whiteKingPosition = null;
        Point blackKingPosition = null;

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                ChessPiece piece = boardState[row][col];
                if (piece != null && piece.getType() == ChessPiece.PieceType.KING) {
                    if (piece.isWhite()) {
                        whiteKingPosition = new Point(row, col);
                    } else {
                        blackKingPosition = new Point(row, col);
                    }
                }
            }
        }

        return new Point[]{whiteKingPosition, blackKingPosition};
    }
	
	
}
