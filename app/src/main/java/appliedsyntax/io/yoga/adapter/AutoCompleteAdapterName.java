package appliedsyntax.io.yoga.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import appliedsyntax.io.yoga.R;
@SuppressWarnings("ALL")
public class AutoCompleteAdapterName extends ArrayAdapter {

    private List<String> dataList;
    private int itemLayout;
    private List<String> className;
    private List<String> mobileNumber;
    private ListFilter listFilter = new ListFilter();
    private List<String> dataListAllItems;
    private ArrayList<String> matchClasses =  new ArrayList<>();
    private ArrayList<String> matchMobile =  new ArrayList<>();
    String searchType;
    public AutoCompleteAdapterName(Context context, int resource, List<String> storeDataLst, List<String> className, List<String> mobileNumber,String searchType) {
        super(context, resource, storeDataLst);
        dataList = storeDataLst;
        this.className = className;
        this.mobileNumber = mobileNumber;
        itemLayout = resource;
        this.searchType = searchType;
    }
    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public String getItem(int position) {
        Log.d("CustomListAdapter",
                dataList.get(position));
        return dataList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(itemLayout, parent, false);
        }
        TextView stuName =  view.findViewById(R.id.srcName);
        TextView srcClass =  view.findViewById(R.id.srcClass);
        TextView mobile = view.findViewById(R.id.srcMobile);

        try {
            if(searchType.equals("text")) {
                stuName.setText(getItem(position));
                srcClass.setText(matchClasses.get(position));
                mobile.setText(matchMobile.get(position));
            }
            else
            {
                stuName.setText(matchMobile.get(position));
                srcClass.setText(matchClasses.get(position));
                mobile.setText(getItem(position));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

            return view;

    }
    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    /**
     *  ListFilter class filter the data from database and gives back
     */
    public class ListFilter extends Filter {
        private final Object lock = new Object();

        /**
         *
         * @param prefix It is the text input to search
         * @return  search results from data
         */
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {

            FilterResults results = new FilterResults();
            if (dataListAllItems == null) {
                synchronized (lock) {
                    dataListAllItems = new ArrayList<>(dataList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<String> matchValues = new ArrayList<>();
                matchClasses.clear();
                matchMobile.clear();
                    for(int i=0;i<dataListAllItems.size();i++)
                    {
                        String dataItem = dataListAllItems.get(i);

                    if (dataItem.toLowerCase().startsWith(searchStrLowerCase)) {
                        if(matchClasses.size()>0)
                        {
                            if(className.get(i)!=null && matchClasses.get(matchClasses.size()-1)!=null && matchClasses.get(matchClasses.size()-1).equals(className.get(i)) && mobileNumber.get(i)!=null && matchMobile.get(matchMobile.size()-1)!=null && matchMobile.get(matchMobile.size()-1).equals(mobileNumber.get(i)))
                            {
                                //nothing
                            }
                            else
                            {
                                matchValues.add(dataItem);
                                matchClasses.add(className.get(i));
                                matchMobile.add(mobileNumber.get(i));
                            }
                        }
                        else {

                            matchValues.add(dataItem);
                            matchClasses.add(className.get(i));
                            matchMobile.add(mobileNumber.get(i));
                        }

                    }
                }

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        /**
         *
         * @param constraint it is a edit text context
         * @param results it is context results from database
         */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                dataList = (ArrayList<String>)results.values;
            } else {
                dataList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}