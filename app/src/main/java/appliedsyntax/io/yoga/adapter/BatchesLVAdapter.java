package appliedsyntax.io.yoga.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import appliedsyntax.io.yoga.acivity.ActivityAddClass;
import appliedsyntax.io.yoga.model.Batch;
import appliedsyntax.io.yoga.R;

public class BatchesLVAdapter extends BaseAdapter {


    private  ArrayList<Batch> bathes;
    private  Activity c;
    public BatchesLVAdapter(Activity c, ArrayList<Batch> bathes) {
        this.bathes = bathes;
        this.c= c;
    }

    @Override
    public int getCount() {
        return bathes.size();
    }

    @Override
    public Object getItem(int i) {
        return bathes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class ViewHolder
    {
        TextView name,address,classETVID;
    }
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null) {

            LayoutInflater inflater = c.getLayoutInflater();
            view = inflater.inflate(R.layout.class_item,viewGroup, false);
            holder=new ViewHolder();
            holder.name = view.findViewById(R.id.BatchNameTVID);
            holder.address = view.findViewById(R.id.BatchLocationTVID);
            holder.classETVID = view.findViewById(R.id.classETVID);
            view.setTag(holder);
        }
        else {
            holder=(ViewHolder) view.getTag();
        }
       holder.name.setText(bathes.get(position).getName());
        holder.address.setText(bathes.get(position).getLocation());
/*
        float initialTranslation = (mLastPosition <= position ? 500f : -500f);
        view.setTranslationY(initialTranslation);
        view.animate()
                .setInterpolator(new DecelerateInterpolator(1.0f))
                .translationY(0f)
                .setDuration(300l)
                .setListener(null);

        // Keep track of the last position we loaded
        mLastPosition = position;*/
        holder.classETVID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(c,ActivityAddClass.class);
                intent.putExtra("class_data",bathes.get(position));
                c.startActivity(intent);

            }
        });
        return view;
    }

}
