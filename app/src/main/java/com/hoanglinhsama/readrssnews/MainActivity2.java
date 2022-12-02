package com.hoanglinhsama.readrssnews;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
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

public class MainActivity2 extends AppCompatActivity {
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

            /* reset lai cac ArrayList de chua phan tu voi moi link tuong ung */
            arrayListTitle.clear();
            arrayListLink.clear();
            arrayListPublishDate.clear();
            arrayListPicture.clear();
            arrayListNews.clear();

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
                else // truong hop khong tim thay link hinh anh trong item do co the co mot so item khong co src img thi gan bang mot hinh mac dinh theo link cung cap
                    arrayListPicture.add("https://i0.wp.com/sebuka.com/wp-content/themes/myx/assets/images/no-image/No-Image-Found-400x264.png");
            }
            addElementArrayListNews();
            adapterNews.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.mapping();
        this.initialization();
        adapterNews = new AdapterNews(MainActivity2.this, R.layout.row_news, this.arrayListNews);
        this.listViewNews.setAdapter(adapterNews);
        new ReadRSS().execute("https://vnexpress.net/rss/tin-moi-nhat.rss"); // mac dinh mo len se la tin moi nhat
        this.listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                intent.putExtra("link", arrayListLink.get(position));
                startActivity(intent);
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_categories, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuWorld:
                new ReadRSS().execute("https://vnexpress.net/rss/the-gioi.rss");
                break;
            case R.id.menuNews:
                new ReadRSS().execute("https://vnexpress.net/rss/thoi-su.rss");
                break;
            case R.id.menuBusiness:
                new ReadRSS().execute("https://vnexpress.net/rss/kinh-doanh.rss");
                break;
            case R.id.menuEntertainment:
                new ReadRSS().execute("https://vnexpress.net/rss/giai-tri.rss");
                break;
            case R.id.menuSport:
                new ReadRSS().execute("https://vnexpress.net/rss/the-thao.rss");
                break;
            case R.id.menuLaw:
                new ReadRSS().execute("https://vnexpress.net/rss/phap-luat.rss");
                break;
            case R.id.menuEducation:
                new ReadRSS().execute("https://vnexpress.net/rss/giao-duc.rss");
                break;
            case R.id.menuHotNews:
                new ReadRSS().execute("https://vnexpress.net/rss/tin-noi-bat.rss");
                break;
            case R.id.menuHealth:
                new ReadRSS().execute("https://vnexpress.net/rss/suc-khoe.rss");
                break;
            case R.id.menuLife:
                new ReadRSS().execute("https://vnexpress.net/rss/gia-dinh.rss");
                break;
            case R.id.menuTravel:
                new ReadRSS().execute("https://vnexpress.net/rss/du-lich.rss");
                break;
            case R.id.menuScience:
                new ReadRSS().execute("https://vnexpress.net/rss/khoa-hoc.rss");
                break;
            case R.id.menuDigitizing:
                new ReadRSS().execute("https://vnexpress.net/rss/so-hoa.rss");
                break;
            case R.id.menuCar:
                new ReadRSS().execute("https://vnexpress.net/rss/oto-xe-may.rss");
                break;
            case R.id.menuIdea:
                new ReadRSS().execute("https://vnexpress.net/rss/y-kien.rss");
                break;
            case R.id.menuTalk:
                new ReadRSS().execute("https://vnexpress.net/rss/tam-su.rss");
                break;
            case R.id.menuLaugh:
                new ReadRSS().execute("https://vnexpress.net/rss/cuoi.rss");
                break;
            case R.id.menuSeeMore:
                new ReadRSS().execute("https://vnexpress.net/rss/tin-xem-nhieu.rss");
                break;
            case R.id.menuLoadLatestNews:
                new ReadRSS().execute("https://vnexpress.net/rss/tin-moi-nhat.rss");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}