package com.hoanglinhsama.readrssnews;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private ListView listViewNews;
    private AdapterNews adapterNews;
    private ArrayList<String> arrayListTitle;
    private ArrayList<String> arrayListLink;
    private ArrayList<News> arrayListNews;
    private ArrayList<String> arrayListPicture;
    private ArrayList<String> arrayListPublishDate;

    private void mapping() {
        this.listViewNews = findViewById(R.id.listViewNews);
    }

    private void initialization() {
        this.arrayListTitle = new ArrayList<String>();
        this.arrayListLink = new ArrayList<String>();
        this.arrayListNews = new ArrayList<News>();
        this.arrayListPicture = new ArrayList<String>();
        this.arrayListPublishDate = new ArrayList<String>();
    }

    private void addElementArrayListNews() {
        for (int i = 0; i < arrayListTitle.size(); i++)
            this.arrayListNews.add(new News(arrayListTitle.get(i), arrayListPublishDate.get(i), arrayListPicture.get(i)));
    }

    public class ReadRSS extends AsyncTask<String, Void, String> { // class ReadRSS la inner class cua class MainActivity de code de doc va co the truy xuat ca field va method private cua class MainActivity
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilderContent = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                URLConnection urlConnection = url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null)
                    stringBuilderContent.append(line);
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilderContent.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            XMLDOMParser xmldomParser = new XMLDOMParser(); // tao object moi de xu ly viec doc xml
            Document document = xmldomParser.getDocument(s); // dua noi dung cua RSS (xml) vao Document, vi Document interface dai dien cho toan bo tai lieu XML va HTMl va Document co chua cac element, textnode (ma day chinh la nhung thu can doc de load duoc web ve may)
            NodeList nodeListItem = document.getElementsByTagName("item"); // moi item la mot node, nodeListItem  co the hieu la 1 List (list cac item), cau lenh nay dung de lay 1 element cua document (chinh la NodeList)
            NodeList nodeListDescription = document.getElementsByTagName("description"); // tuong tu cung lay ra mot NodeList cac description (description nam ben trong item tuy nhien do ben trong description con chua cac phan tu con (du lieu khac cua no) nen phai tao mot NodeListDescription de lay duoc du lieu ben trong no)
            for (int i = 0; i < nodeListItem.getLength(); i++) { // duyet tung item trong NodeList
                Element element = (Element) nodeListItem.item(i); // lay ra mot element trong nodeList (chinh la 1 cai item)
                arrayListTitle.add(xmldomParser.getValue(element, "title"));// tra ve mot cai trong element (title trong mot cai item) theo name truyen vao va them vao arrayListTitle
                arrayListLink.add(xmldomParser.getValue(element, "link"));
                arrayListPublishDate.add(xmldomParser.getValue(element, "pubDate"));
                String CDATA = nodeListDescription.item(i + 1).getTextContent(); // i+1 de bo di description dau tien khong co du lieu ben trong, getTextContent tra ve noi dung van ban cua node nay va cac phan tu con cua no (lay noi dung cua the CDATA trong description). Do du lieu ben trng CDATA hon hop (khong co the ro rang) va minh can lay chinh xac du lieu gi nen khong the lam theo cach getValue nhu tren duoc
                Pattern pattern = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>"); // tao mot mau pattern tu bieu thuc chinh quy bang cach compile bieu thuc chinh quy (bieu thuc chinh quy la dang chuan cua mot dang tai nguyen (img src, mail,...)) roi sau do gan vao variable pattern nay, o day la dang cua img src de co the dung mau nay lay duoc img src tu CDATA
                Matcher matcher = pattern.matcher(CDATA); // tao mot trinh so khop matcher giua dau vao CDATA voi mau pattern nay
                if (matcher.find()) // neu tim thay trong CDATA mot chuoi con co dang khop voi pattern thi
                    arrayListPicture.add(matcher.group(1)); // cac chuoi con tim duoc trong CDATA se duoc gan vao chung 1 group, group(1) o day chinh la group cua img src, va group(1) chi co 1 phan tu do img src trong CDATA la duy nhat
            }
            addElementArrayListNews();
            adapterNews.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mapping();
        this.initialization();
        adapterNews = new AdapterNews(MainActivity.this, R.layout.row_news, this.arrayListNews);
        this.listViewNews.setAdapter(adapterNews);
        new ReadRSS().execute("https://vnexpress.net/rss/so-hoa.rss"); // execute AsyncTask nay la mot Thread rieng voi MainThread (Main Thread co the goi la UI Thread), do do khong phai la no chay xong thi no moi chay tiep tuc dong code duoi no (do code duoi no nam trong MainThread se chay song song voi AsyncTask Thread), duoi no chi nen de code bat event thoi (vi code event chi chay khi ma co event xay ra thoi)
        this.listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("link", arrayListLink.get(position));
                startActivity(intent);
            }
        });
    }
}