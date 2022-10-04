package com.hoanglinhsama.readrssnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterNews extends BaseAdapter {
    private Context context;
    private int layout;
    private List<News> listNews;

    public AdapterNews(Context context, int layout, List<News> listNews) {
        this.context = context;
        this.layout = layout;
        this.listNews = listNews;
    }

    @Override
    public int getCount() { // tra ve so item (dong) ma Adapter hient thi
        return listNews.size();
    }

    @Override
    public Object getItem(int position) { // khong can thiet vi da lay duoc du lieu  listIdol
        return null;
    }

    @Override
    public long getItemId(int position) { // khong can thiet vi da lay duoc du lieu  listIdol
        return 0;
    }

    private class ViewHolder {
        ImageView imageViewPicture;
        TextView textViewTitle, textViewPublishDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            viewHolder = new ViewHolder();
            viewHolder.textViewTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
            viewHolder.textViewPublishDate = (TextView) convertView.findViewById(R.id.textViewPublishDate);
            viewHolder.imageViewPicture = (ImageView) convertView.findViewById(R.id.imageViewPicture);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        News news = listNews.get(position);
        viewHolder.textViewTitle.setText(news.getTitle());
        viewHolder.textViewPublishDate.setText(news.getpublishDate());
        Picasso.with(convertView.getContext()).load(news.getPicture()).into(viewHolder.imageViewPicture); // Picasso de load anh tu mang ve may ao, with() de tao ra 1 instance cua Picasso, load () de bat dau mot yeu cau hinh anh bang cach dung duong dan xac định, into() la noi se chua cai anh duoc load tu tren mang ve
        return convertView;
    }
}
