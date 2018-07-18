package org.hyg.breakoutbyseraph0;

/**
 * Created by shiny on 2018-03-06.
 * 각각의 스테이지에 블록을 지정한 형태로 배치하려면 좌표가 필요한데,
 * 좌표 대신 맵 데이터를 이용해 구현
 * 숫자 1, 2, 3 : 각각의 종류별 블록
 * > : 블록 반 개의 공백
 * . : 블록 한 개의 공백
 * 예)
 *     1 1 1 1 1 1  ■■■■■■■■■■■■
 *     >1 . . . 1    ■■      ■■
 *     1 >1 2 1 >1  ■■ ■■▒▒■■ ■■
 */

public class Stage {

    // 맵 데이터
    static private String[][] m_mapData = {
            new String[] {
                    "   1 1 1 1 1 1 ",
                    "   1 . . . . 1 ",
                    "   1 >2 2 2 >1 ",
                    "   1 >2 3 2 >1 ",
                    "   1 >2 2 2 >1 ",
                    "   1 . . . . 1 ",
                    "   1 1 1 1 1 1 "
            }
            ,
            new String[] {
                    "   1 1 1 1 1 1 ",
                    "   >1 2 2 2 1 ",
                    "   . 1 2 2 1 ",
                    "   . >1 3 1 ",
                    "   . . 3 3 ",
                    "   . >1 3 1 ",
                    "   . 1 2 2 1 ",
                    "   . 1 2 2 1 ",
                    "   >1 2 2 2 1 ",
                    "   1 1 1 1 1 1 "
            }
            ,
            new String[] {
                    "   >. . 1 ",
                    "   . . 1 1 ",
                    "   >. 1 2 1 ",
                    "   . 1 2 2 1 ",
                    "   >1 2 3 2 1 ",
                    "   1 2 3 3 2 1 ",
                    "   >1 2 3 2 1 ",
                    "   . 1 2 2 1 "
            }
            ,
            new String[] {
                    "   >. 1 . 1 ",
                    "   . 1 2 2 1 ",
                    "   >1 2 3 2 1 ",
                    "   1 2 3 3 2 1 ",
                    "   >1 2 3 2 1 ",
                    "   . 1 2 2 1 ",
                    "   . >1 2 2 ",
                    "   . . 1 1 ",
                    "   . . >1 "
            }
    };

    static public void initStage(int stageIdx){

        stageIdx = stageIdx % m_mapData.length;

        int bw = CommonResources.getBlockWidth();
        int bh = CommonResources.getBlockHeight();

        // 위쪽 마진(bh * 2)과 블럭 한 개의 높이(bh * 2)를 고려한 초기 y좌표
        float by = bh * 4;

        String[] map = m_mapData[stageIdx];
        int blockNo = 0;

        for(int i=0; i<map.length; i++){
            String strBlock = map[i].trim();
            float bx = 0;

            for(int j=0; j<strBlock.length(); j++){
                switch(strBlock.charAt(j)){
                    case '.':
                        bx += bw * 2;
                        break;
                    case '>':
                        bx += bw;
                        break;
                    case '1':
                    case '2':
                    case '3':
                        blockNo = Integer.parseInt(strBlock.substring(j, j+1));
                        GameView.BlockList.add(new Block(blockNo, bx + bw, by + bh));
                        bx += bw * 2;
                }
            }

            by += bh * 2;
        }
    }
}
