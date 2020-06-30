package appliedsyntax.io.yoga.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import appliedsyntax.io.yoga.acivity.ActivityAddLocation;
import appliedsyntax.io.yoga.model.Location;
import appliedsyntax.io.yoga.R;

public class LocationLVAdapter extends BaseAdapter {

    private ArrayList<Location> locations ;
   private Activity c;
    public LocationLVAdapter(Activity c, ArrayList<Location> locations) {
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
        TextView name,address;
    }
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if(view==null) {

            LayoutInflater inflater = c.getLayoutInflater();
            view = inflater.inflate(R.layout.location_item, viewGroup,false);
            holder=new ViewHolder();
            holder.name = view.findViewById(R.id.locationNameTVID);
            holder.address = view.findViewById(R.id.locationAddressETID);

            view.setTag(holder);
        }
        else {
            holder=(ViewHolder) view.getTag();
        }
       holder.name.setText(locations.get(position).getName());
        holder.address.setText(locations.get(position).getAddress());
//        ON CLICK SHOW ADDRESS

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.address.isShown())
                {
                    holder.name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pin, 0, R.drawable.ic_down_arrow, 0);

                    holder.address.setVisibility(View.GONE);
                }
                else {
                    holder.name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pin, 0, R.drawable.ic_up_arrow, 0);
                    holder.address.setVisibility(View.VISIBLE);
                }
            }
        });
        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(c,ActivityAddLocation.class);
                intent.putExtra("location_data",locations.get(position));
                c.startActivity(intent);

            }
        });

////        ONSCROLL LIST VIEW ANIMATION
/*        float initialTranslation = (mLastPosition <= position ? 500f : -500f);
        view.setTranslationY(initialTranslation);
        view.animate()
                .setInterpolator(new DecelerateInterpolator(1.0f))
                .translationY(0f)
                .setDuration(300l)
                .setListener(null);

        // Keep track of the last position we loaded
        mLastPosition = position;*/
        return view;
    }

}
