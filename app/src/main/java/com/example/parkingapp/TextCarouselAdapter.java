package com.example.parkingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.viewpager.widget.PagerAdapter;

public class TextCarouselAdapter extends PagerAdapter {
    private Context context;
    private String[] texts; // Array of texts to display in the carousel
    private int backgroundColor; // Custom background color
    private int textColor; // Custom text color

    public TextCarouselAdapter(Context context, String[] texts, int backgroundColor, int textColor) {
        this.context = context;
        this.texts = texts;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.carousel_item, collection, false);
        TextView textView = layout.findViewById(R.id.carousel_text);
        textView.setText(texts[position]);
        textView.setBackgroundColor(backgroundColor);
        textView.setTextColor(textColor);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return texts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
