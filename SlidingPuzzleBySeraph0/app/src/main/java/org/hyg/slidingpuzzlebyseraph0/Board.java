package org.hyg.slidingpuzzlebyseraph0;

import android.graphics.Canvas;
import android.os.Debug;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by shiny on 2018-03-07.
 *  : 순서를 바꾸어야 할 타일의 쌍은 반드시 짝수 개여야 하므로 타일을 섞은 후 짝수 개인지 확인 필요
 *  : 타일을 터치하면 타일의 상하좌우에 공간이 있는 경우 공간에 가까운 타일부터 차례로 이동한다.
 */

public class Board {

    private Random m_rnd = new Random();

    // 타일 수(빈 타일 빼기 n * n - 1), 타일 번호 배열, 타일 이동방향
    private int m_tileCnt;
    private int[] m_tileGridToLine;
    private int m_direction = 0;

    // 타일 이동횟수 누적(화면 표시용), 이동한 타일 인덱스 저장 버퍼
    private int m_movedCnt;
    public int getMovedTileCount() { return m_movedCnt; }
    private ArrayList<Integer> m_buffer = new ArrayList<Integer>();

    // 터치한 타일 번호, 배열내의 터치한 타일 인덱스, 배열내의 공백 인덱스
    private int m_touchedIdx, m_aryIdx, m_aryBlankIdx;

    // 타일 객체
    public ArrayList<Tile> TileList = new ArrayList<Tile>();


    /**
     * 타일 인덱스 배열을 섞어 타일 인덱스 배열 초기화
     * 기존 타일 배열 초기화하고 섞은 인덱스 배열의 값으로 타일 개체의 좌표 설정
     *  : 마지막 타일은 빈타일(-1)
     */
    public void initBoard(){
        m_tileCnt = Settings.Size * Settings.Size - 1;
        m_tileGridToLine = new int[m_tileCnt + 1];

        for(int i=0; i<m_tileCnt; i++){
            m_tileGridToLine[i] = i;
        }

        m_tileGridToLine[m_tileCnt] = -1;

        setSuffleTile();

        TileList.clear();
        for(int i=0; i<=m_tileCnt; i++){
            TileList.add(new Tile(i));
        }

        for(int i=0; i<=m_tileCnt; i++){
            if(m_tileGridToLine[i] >= 0){
                TileList.get(m_tileGridToLine[i]).setPosition(i);
            }
        }
    }

    /**
     * 타일 인덱스 배열 섞기
     *  : 카드를 일렬로 나열한 후 임의 위 두장을 골라 그 위치를 바꾸는 방법으로 섞는다
     */
    private void setSuffleTile() {
        for(int i=0; i<m_tileCnt; i++){
            int nFirst = m_rnd.nextInt(m_tileCnt + 1);
            int nSecond = m_rnd.nextInt(m_tileCnt + 1);

            int tmp = m_tileGridToLine[nFirst];
            m_tileGridToLine[nFirst] = m_tileGridToLine[nSecond];
            m_tileGridToLine[nSecond] = tmp;
        }

        if(!isIntegrity()){

        }
    }

    /**
     * 무결성 여부
     *  : 다음에 오는 값이 작은 값이면 치환가능
     * @return : 치환할 수 있는 쌍의 갯수가 짝수이면 true, 아니면 false;
     */
    public boolean isIntegrity() {
        int repCnt = 0;

        for(int i=0; i<m_tileCnt; i++){
            if(m_tileGridToLine[i] == -1) continue;

            for(int j=i+1; j<=m_tileCnt; j++){
                if(m_tileGridToLine[j] != -1 && m_tileGridToLine[i] > m_tileGridToLine[j]){
                    repCnt++;
                }
            }
        }

        return repCnt % 2 == 0;
    }

    /**
     * 스테이지 클리어 여부
     *  : 이동중이면 fasle
     *  : 타일이 모두 맞춰졌으면(순서대로 배치되었으면) true, 아니면 false
     * @return
     */
    public boolean getIsClearAll(){
        if(m_direction != 0) { return false; }

        for(int i=0; i<m_tileCnt; i++){
            if(m_tileGridToLine[i] != i) return false;
        }

        return true;
    }

    /**
     * 이동할 타일의 이동이 모두 끝나면 이동방향 초기화(이동중지)
     */
    public void moveToNext(){
        if(m_direction == 0) { return; }

        boolean isMove = false;

        for(Integer idx : m_buffer){
            isMove = TileList.get(idx).moveToNext();
        }

        if(!isMove) { m_direction = 0; }
    }

    /**
     * 터치한 위치에 타일이 있는지 조회
     *  : 이동중이거나 터치한 타일이 없으면 리턴
     * @param tx : 터치한 x좌표
     * @param ty : 터치한 y좌표
     */
    public void hitTest(float tx, float ty){
        if(m_direction != 0 || !hasTouchedTile(tx, ty)) { return; }

        getMovableTileIndex();
        getDirection();

        if(m_direction > 0){
            getMoveTiles();
        }
    }

    /**
     * 타일을 화면에 출력
     *  : translate() 를 이용해서 원래좌표에서 이동하여 원하는 위치에 출력
     * @param canvas : GameView 로부터 전달받음
     */
    public void drawTile(Canvas canvas){
        canvas.drawBitmap(CommonResources.getFrame()
                        , CommonResources.getXFrame()
                        , CommonResources.getYFrame()
                        , null);

        canvas.translate(CommonResources.MarginWidth
                       , CommonResources.MarginHeight);

        for(Tile itm : TileList){
            try {
                canvas.drawBitmap(itm.getTile()
                        , itm.getX()
                        , itm.getY()
                        , null);
            } catch (Exception ex){

            }
        }

        canvas.translate(-CommonResources.MarginWidth
                        , -CommonResources.MarginHeight);
    }

    /**
     * 터치한 타일 존재 여부
     * @param tx : 터치한 x좌표
     * @param ty : 터치한 y좌표
     * @return
     */
    private boolean hasTouchedTile(float tx, float ty){
        for(Tile itm : TileList){
            m_touchedIdx = itm.hitTest(tx, ty);
            if(m_touchedIdx >= 0) { break; }
        }

        return m_touchedIdx >= 0;
    }

    /**
     * 배열에서 터치한 타일 인덱스와 배열에서 공백 타일의 인덱스 조회
     */
    private void getMovableTileIndex(){
        for(int i=0; i<=m_tileCnt; i++){
            if(m_tileGridToLine[i] == m_touchedIdx){
                m_aryIdx = i;
                break;
            }
        }

        for(int i=0; i<=m_tileCnt; i++){
            if(m_tileGridToLine[i] == -1){
                m_aryBlankIdx = i;
                break;
            }
        }
    }

    /**
     * 터치한 타일 이동 방향 설정
     *  : 이동 타일의 배열 인덱스를 직교 좌표로 변환
     *  : 공백 타일의 배열 인덱스를 직교 좌표로 변환
     *  : 타일과 같은 행 또는 열에 공백 타일이 있는지 조회
     *     => 같은 열에 있으면 이동방향은 1(12시) 또는 3(6시)방향
     *     => 같은 행에 있으면 이동방향은 2(9시) 또는 4(3시) 방향
     */
    private void getDirection(){
        int tx = m_aryIdx % Settings.Size;
        int ty = m_aryIdx / Settings.Size;

        int bx = m_aryBlankIdx % Settings.Size;
        int by = m_aryBlankIdx / Settings.Size;

        m_direction = 0;

        if(tx != bx && ty != by) { return; }

        if(tx == bx){
            m_direction = (ty > by) ? 1 : 3;
        }

        if(ty == by){
            m_direction = (tx < bx) ? 2 : 4;
        }
    }

    /**
     * n x n 타일배열의 이동대상 타일의 인덱스배열 위치이동과 이동방향 설정
     *  : 공백 타일에 가까운(공백인덱스 + n) 타일부터 터치한 타일까지
     *    차례로 [이동한 타일 인덱스 저장 버퍼]에 추가하고 인덱스배열 위치 이동
     *    공백타일의 (1: 위쪽, 2: 오른쪽, 3: 아래쪽, 4: 왼쪽)으로 이동
     *  : 이동 후에 터치한 타일은 공백 타일로 설정
     *  : 이동한 타일 객체에 이동방향 설정
     */
    private void getMoveTiles(){
        m_buffer.clear();

        int colCnt = Settings.Size;

        switch(m_direction){
            case 1:
                for(int y=m_aryBlankIdx + colCnt; y<=m_aryIdx; y+=colCnt){
                    m_buffer.add(m_tileGridToLine[y]);
                    m_tileGridToLine[y - colCnt] = m_tileGridToLine[y];
                }
                break;

            case 2:
                for(int x=m_aryBlankIdx - 1; x>=m_aryIdx; x--){
                    m_buffer.add(m_tileGridToLine[x]);
                    m_tileGridToLine[x + 1] = m_tileGridToLine[x];
                }
                break;

            case 3:
                for(int y=m_aryBlankIdx - colCnt; y>=m_aryIdx; y-=colCnt){
                    m_buffer.add(m_tileGridToLine[y]);
                    m_tileGridToLine[y + colCnt] = m_tileGridToLine[y];
                }
                break;

            case 4:
                for(int x=m_aryBlankIdx + 1; x<=m_aryIdx; x++){
                    m_buffer.add(m_tileGridToLine[x]);
                    m_tileGridToLine[x - 1] = m_tileGridToLine[x];
                }
                break;
        }

        m_tileGridToLine[m_aryIdx] = -1;

        for(Integer idx : m_buffer){
            TileList.get(idx).setDirection(m_direction);
            m_movedCnt++;
        }
    }
}
