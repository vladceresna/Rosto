package com.rula.rosto.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rula.etime.EasyTime;
import com.rula.etime.TimeData;
import com.rula.rosto.R;
import com.rula.rosto.obj.Letter;

import java.util.List;

public class LetterListAdapter extends BaseAdapter {
    private List<Letter> letters;
    private Context context;
    public LetterListAdapter(Context context, List<Letter> letters) {
        this.letters = letters;
        this.context = context;
    }
    @Override
    public int getCount() {
        return letters.size();
    }

    @Override
    public Object getItem(int i) {
        return letters.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = LayoutInflater.from(context).
                    inflate(R.layout.letter_layout, viewGroup, false);
        }
        Letter letter = (Letter) getItem(i);

        TextView titlet = (TextView)
                view.findViewById(R.id.titlet);
        TextView timet = (TextView)
                view.findViewById(R.id.timet);
        TextView textt = (TextView)
                view.findViewById(R.id.textt);
        TextView rid = (TextView)
                view.findViewById(R.id.rid);

        rid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent().setClassName("com.rula.rosto", "com.rula.rosto.UserProfileActivity").putExtra("id", letter.getId()));
            }
        });

        EasyTime et = new EasyTime();
        String s = ".";
        TimeData td = et.getConvTime(Long.parseLong(letter.getTime()));
        timet.setText(td.M() +s+ td.D() +" "+ td.H() +":"+ td.m());

        titlet.setText(letter.getTitle());
        textt.setText(letter.getText());
        rid.setText(letter.getId());

        return view;
    }
}
