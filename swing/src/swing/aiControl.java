package swing;

import swing.ChessBoard.ChessPiece;
import swing.ChessBoard.ChessPiece.PieceType;

public class aiControl {

	
	public boolean myColor = false;
	
	
	public int[] select(ChessBoard board) {
        //선택된 말의 최선의 수를 계산하기 위한 것임.
		//ai의 색상과 동일한 말을 선택 후, calculate메소드를 통해 탐색, 탐색된 결과를 return.
        
        int bestMoveScore = Integer.MIN_VALUE;
        int[] bestMove = null;
        for(int i = 0; i < 8; i++) {
        	for(int m = 0; m < 8; m++) {
        		if(board.d()[i][m] != null && board.d()[i][m].isWhite() == myColor) {
        		//if문은 해당 칸의 말을 조사, 아닌 경우에는 다른 말을 조사. 만약 if문이 발동됬는데 길이 안나온 경우에는 다른 말을 탐색한다.
        		//선택한 말을 계속 가지고 가게 해야함.  return이 된 경우
        			//움직일 수 없는 경우에는 패배처리를 하던가 해야하는데 동료랑 상의 먼저해야함.
        			bestMove = calculate(i, m, board.d()[i][m]);
        			if(bestMove != null) {
        				break;
        			}
        		}
        	}
        }
        // 보드를 순회하면서 가능한 모든 움직임을 검사하고 가장 높은 점수를 가진 움직임을 선택합니다.
        //while문으로 2개정도만 넣을 수 있도록.
        //그냥 여기서 바로 이동을 해도 되긴함 return하지말고
        return bestMove;
    }
	//특정 칸에 있는 말이 이동 가능한 곳들 계산
	//최선의 수를 찾는 동안 와일 계속 돌려서 최선의 수 찾는거임 사실상 n^2, 반복문 2개
	public int[] calculate(int startX, int startY, ChessPiece chess) {
		int[] bestMove = null;
		int bestMoveScore = Integer.MIN_VALUE;
        int x, y;
		while(true) {
			//첫번쨰 와일문 bestMove를 저장하는 용도(2번쨰 와일문의 값을 저장하는 용도)
			//닫힌리스트 필요. 탐색한걸 또 탐색하지않기 위해서임.
			while(true) {
			//2번쨰 와일문 bestMove가 나올 때마다 1번째 와일문에게 넘기는 용도 
				//닫힌 리스트에 만들 리스트가 있다면, 다시 조사
					if(chess.getType() == PieceType.PAWN) {
						//각말의 움직임 넣어주기
						//MoveGenerator.getValidPawnMoves(startX, startY);
					}
				}
			}
		

		return bestMove;
	}
	//메소드가 체스보드에서 턴 끝날때마다 ai반복하는 방법 1
	//나머지는 중간 관리자를 하나 만들어 양쪽이 끝나는걸 확인한 후 하는 방법 2
	//바로 말 이동
	public void controller(ChessBoard board) {
		int[] bestMove1 = null;
		
			bestMove1 = select(board);
			//select메소드가 반환하는 값이 모든 공간을 탐색했을 때, 이동할 수 없는 말에 대한 경우면(true면 탐색 성공, false면 실패)
			/*즉, bestMove1이 boolean값인 경우
			 if(bestMove1 == false){
			  defeat}
			  */
		
	}
    
    // 각 움직임에 대한 점수 평가 로직을 작성합니다.
    private int evaluateMove(ChessBoard board, int fromRow, int fromCol, int toRow, int toCol) {
        // 여기에서 움직임을 평가하는 로직을 작성합니다.
        // 예를 들어, 각 조각의 가치를 고려하여 평가하거나 체크 여부를 검사할 수 있습니다.
        
        // 간단한 예시로 일단은 랜덤하게 점수를 반환하는 것으로 대체하겠습니다.
        return (int) (Math.random() * 100);
    }

}