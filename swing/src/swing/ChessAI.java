package swing;

import swing.ChessBoard.ChessPiece;
/**
 * 
 * @author cubby(Donghwan)
 *
 */
public interface ChessAI{
	//while
	void Chess(ChessPiece a, ChessBoard d, int difficult, int color);
	//무슨 말을 가지고있고 difficult에 따라 ,n^3
	void playerSet(ChessPiece a, int difficult);
	//경로, 2차원 경로
	void path(int row, int col);
	//못가는 곳
	void blocked(int row, int col);
}

//Node를 만들어서 이웃노드(갈 노드), 즉 길을 노드로 만들어 노드타입 따로 만들어. pathfinding 8방향 검색