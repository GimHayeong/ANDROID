package org.hyg.rssfeederbyseraph0;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by shiny on 2018-03-27.
 * DOM 파서를 이용해 XML 데이터 파일을 파싱하는 클래스
 *  - DocumentBuilderFactory.newInstance() : DOM 파서 객체 생성
 *  - DocumentBuilder.newDocumentBuilder() : Builder 객체 생성
 *  - DocumentBuilder.parse() : 빌터를 이용해 Docoment 객체 생성
 *  - 도큐먼트 객체에서 <item>..</item> 태그를 찾아서 RSSNewsItem 객체로 변환
 */

public class RSSRefreshThread extends Thread {
    private String mUrl;
    private final String TAG = "RSSFeeder";
    private List<RSSNewsItem> mNewsItemList = new ArrayList<RSSNewsItem>();
    private Handler mHandler;
    private Runnable mUpdateRunnable;
    public RSSRefreshThread(String url) {
        mUrl = url;
    }


    @Override
    public void run() {
        try{
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();

            URL url = new URL(mUrl);

            InputStream inStream = getInputStreamUsingHttp(url);
            Document document = builder.parse(inStream);

            int itmCount = processDocument(document);
            Log.d(TAG, itmCount + " news item processed.");

            //mHandler.post(mUpdateRunnable);
        } catch (ParserConfigurationException e) {
            // newDocumentBuilder() 메서드를 위한 exception
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // URL() 메서드를 위한 exception
            e.printStackTrace();
        } catch (SAXException e) {
            // parse() 메서드를 위한 exception
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * [파싱 처리]
     * <item>...</item> 태그를 찾아서 RSSNewsItem 객체로 변환
     * @param document
     * @return
     */
    private int processDocument(Document document) {
        int cnt = 0;
        mNewsItemList.clear();

        Element element = document.getDocumentElement();
        NodeList nodeList = element.getElementsByTagName("item");
        if(nodeList != null && nodeList.getLength() > 0){
            for(int i=0; i<nodeList.getLength(); i++){
                RSSNewsItem itm = dissectNode(nodeList, i);
                if(itm != null){
                    mNewsItemList.add(itm);
                    cnt++;
                }
            }
        }

        return cnt;
    }

    /**
     * 파서를 통해 해당 태그의 데이터를 RSSNewsItem 형태로 변환하여 반환
     * @param nodeList : <item> ... </item> 태그의 하나의 뉴스정보 노드
     * @param i : 인덱스
     * @return : 변환된 RSSNewsItem
     */
    private RSSNewsItem dissectNode(NodeList nodeList, int i) {
        RSSNewsItem itm = null;

        try{

            Element eItem = (Element)nodeList.item(i);
            Element eTitle = (Element)eItem.getElementsByTagName("title").item(0);
            Element eLink = (Element)eItem.getElementsByTagName("link").item(0);
            Element eDescription = (Element)eItem.getElementsByTagName("description").item(0);
            NodeList lstPubDateNode = eItem.getElementsByTagName("pubDate");
            if(lstPubDateNode == null){
                lstPubDateNode = eItem.getElementsByTagName("dc:date");
            }
            Element ePubDate = (Element)lstPubDateNode.item(0);
            Element eAuthor = (Element)eItem.getElementsByTagName("author").item(0);
            Element eCategory = (Element)eItem.getElementsByTagName("category").item(0);

            String title = getNodeValue(eTitle);
            String link = getNodeValue(eLink);
            String description = getNodeValue(eDescription);
            String pubDate = getNodeValue(ePubDate);
            String author = getNodeValue(eAuthor);
            String category = getNodeValue(eCategory);

            itm = new RSSNewsItem(title, link, description, pubDate, author, category);

        } catch (Exception ex) { }

        return itm;
    }

    private String getNodeValue(@Nullable Element element) {
        String elementValue = null;
        if(element != null){
            Node firstChild = element.getFirstChild();
            if(firstChild != null) {
                elementValue = firstChild.getNodeValue();
            }
        }

        return elementValue;
    }

    private InputStream getInputStreamUsingHttp(URL url) {
        // TODO:
        return null;
    }
}