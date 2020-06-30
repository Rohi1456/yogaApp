package appliedsyntax.io.yoga.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import appliedsyntax.io.yoga.model.Batch;
import appliedsyntax.io.yoga.R;

public class SelectBatcheLVAdapter extends BaseAdapter {

    private ArrayList<Batch> bathes ;
    private Activity c;
    public SelectBatcheLVAdapter(Activity c, ArrayList<Batch> bathes) {
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
        TextView name;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null) {

            LayoutInflater inflater = c.getLayoutInflater();
            view = inflater.inflate(R.layout.select_batch_layout, viewGroup,false);
            holder=new ViewHolder();
            holder.name = view.findViewById(R.id.select_BatchTVID);

            view.setTag(holder);
        }
        else {
            holder=(ViewHolder) view.getTag();
        }
       holder.name.setText(bathes.get(position).getName());
        return view;
    }

}
