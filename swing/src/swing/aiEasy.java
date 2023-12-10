package swing;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import swing.ChessBoard.ChessPiece;
import swing.ChessBoard.ChessPiece.PieceType;

public class aiEasy {

	public boolean myColor = false;
	public MoveGenerator generator;// similer with moveGenerator
	private ChessPiece[][] state;// similar with boardstate in chessBoard
	private ChessPiece st;// similar with slectedPiece in chessBoard
	private boolean cmove;// can not move true->can not move.
	
	
	public int a = 0;

	// for selectedPiece
	// AI chess pieces that can be moved make the highest-scoring moves after
	// checking the places where they can move.
	public List<Point> select(ChessBoard board) {
		int bestMoveScore = Integer.MIN_VALUE;
		List<Point> bestMove = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			for (int m = 0; m < 8; m++) {
				if (board.d()[i][m] != null && board.d()[i][m].isWhite() == myColor) {

					calculate(i, m, board.d()[i][m]);
					if (cmove == true) {
						continue;
					} else if (cmove != true) {
						bestMove = calculate(i, m, board.d()[i][m]);
						st = board.d()[i][m];
						st.points = new Point(i, m);
						
					}
				}
			}
		}
		return bestMove;
	}
	// Determine the type of chess piece based on the current point and move to a
	// higher score within the range where the corresponding chess piece can move.
	public List<Point> calculate(int startX, int startY, ChessPiece chess) {

		int betterScore = 0;

		List<Point> bestMoves = new ArrayList<>();

		switch (chess.getType()) {
		case PAWN:
			bestMoves = find(startX, startY, betterScore, bestMoves, generator.getValidPawnMoves(startX, startY));
			break;
		case ROOK:
			bestMoves = find(startX, startY, betterScore, bestMoves, generator.getValidRookMoves(startX, startY));
			break;
		case BISHOP:
			bestMoves = find(startX, startY, betterScore, bestMoves, generator.getValidBishopMoves(startX, startY));
			break;
		case QUEEN:
			bestMoves = find(startX, startY, betterScore, bestMoves, generator.getValidQueenMoves(startX, startY));
			break;
		case KING:
			bestMoves = find(startX, startY, betterScore, bestMoves, generator.getValidKingMoves(startX, startY));
			break;
		case KNIGHT:
			bestMoves = find(startX, startY, betterScore, bestMoves, generator.getValidKnightMoves(startX, startY));
			break;
		default:
			bestMoves = find(startX, startY, betterScore, bestMoves, generator.getValidPawnMoves(startX, startY));
			break;
		}
		return bestMoves;
	}

	/*
	 * A method of receiving information from a chess board, sending information to
	 * methods existing in acontrol, and moving AI's chess pieces based on this.
	 */
	public void controller(ChessBoard board, MoveGenerator mv, ChessPiece[][] bd) {
		List<Point> bestMove1 = null;
		generator = mv;
		state = bd;
		// ChessPiece selectPiece;
		bestMove1 = select(board);
		a = (int) (Math.random()*bestMove1.size());
		
		if (bestMove1.isEmpty() != true) {
			board.aiMovePiece(st, bestMove1.get(a).x, bestMove1.get(a).y);
		} else {
			board.aiMovePiece(st, bestMove1.get(a).x, bestMove1.get(a).y);
		}

	}

	// How to find the highest score. It's also added to prevent the selected piece
	// from being unable to move.
	public List<Point> find(int startX, int startY, int betterScore, List<Point> bestMoves, List<Point> moveGenerator) {
		List<Point> validMoves = new ArrayList<>();
		if (!moveGenerator.isEmpty()) {
			validMoves = moveGenerator;
			for (Point move : validMoves) {
				if (state[move.x][move.y] != null) {
					if (betterScore < sc(state[move.x][move.y])) {
						betterScore = sc(state[move.x][move.y]);
						bestMoves.clear();
						bestMoves.add(new Point(move.x, move.y));
						
					} else if (betterScore == sc(state[move.x][move.y])) {
						bestMoves.add(new Point(move.x, move.y));
						
					} else if (betterScore > sc(state[move.x][move.y])) {
						continue;
					}
				}
			}

			if (betterScore == 0) {
				if (validMoves.isEmpty() != true) {
					Point abc = validMoves.get((int) (Math.random() * validMoves.size()));
					bestMoves.add(abc);
				}
			}
			cmove = false;
			return bestMoves;
		} else {
			cmove = true;
			return bestMoves;
		}
	}

	// Scores are given according to the type of piece that exists in the movable
	// compartment.
	public int sc(ChessPiece chess) {
		int score = 0;
		switch (chess.getType()) {
		case PAWN:
			score = 1;
			break;
		case ROOK:
			score = 5;
			break;
		case BISHOP:
			score = 3;
			break;
		case QUEEN:
			score = 9;
			break;
		case KING:
			score = 1000;
			break;
		case KNIGHT:
			score = 3;
			break;
		default:
			score = 0;
			break;
		}
		return score;
	}

}