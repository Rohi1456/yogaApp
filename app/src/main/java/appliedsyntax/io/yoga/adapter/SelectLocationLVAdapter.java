package appliedsyntax.io.yoga.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import appliedsyntax.io.yoga.R;

public class SelectLocationLVAdapter extends BaseAdapter {

    private ArrayList<appliedsyntax.io.yoga.model.Location> locations ;
    private Activity c;
    public SelectLocationLVAdapter(Activity c, ArrayList<appliedsyntax.io.yoga.model.Location> locations) {
        this.locations = locations;
        this.c= c;
    }

    @Override
    public int getCount() {
        return locations.size();
    }

    @Override
    public Object getItem(int i) {
        return locations.get(i);
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
       holder.name.setText(locations.get(position).getName());
        return view;
    }

}
