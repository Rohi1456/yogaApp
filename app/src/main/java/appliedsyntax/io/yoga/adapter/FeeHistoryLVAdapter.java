package appliedsyntax.io.yoga.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import appliedsyntax.io.yoga.R;
import appliedsyntax.io.yoga.model.FeePament;

public class FeeHistoryLVAdapter extends BaseAdapter {


    private  ArrayList<FeePament> feePayments;
    private Activity c;
    public FeeHistoryLVAdapter(Activity c, ArrayList<FeePament> feePayments) {
        this.feePayments = feePayments;
        this.c= c;
    }

    @Override
    public int getCount() {
        return feePayments.size();
    }

    @Override
    public Object getItem(int i) {
        return feePayments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class ViewHolder
    {
        TextView amount,dueDate,paidDate;
    }
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if(view==null) {

            LayoutInflater inflater = c.getLayoutInflater();
            view = inflater.inflate(R.layout.layout_fee_history, viewGroup,false);
            holder=new ViewHolder();
            holder.amount = view.findViewById(R.id.amountTVID);
            holder.dueDate = view.findViewById(R.id.dueDateTVID);
            holder.paidDate = view.findViewById(R.id.paidDateTVID);

            view.setTag(holder);
        }
        else {
            holder=(ViewHolder) view.getTag();
        }
        Date dueTimeStamp = feePayments.get(position).getDueDate();
        Format formatter = new SimpleDateFormat("E, dd MMM yyyy ",Locale.getDefault());
        String dueDate = formatter.format(dueTimeStamp.getTime());
        Date paidTimeStamp = feePayments.get(position).getPaidDate();
        String paidDate = formatter.format(paidTimeStamp.getTime());
        String amount = Integer.toString(feePayments.get(position).getAmount());
        holder.amount.setText(amount);
        holder.dueDate.setText(dueDate);
        holder.paidDate.setText(paidDate);
        return view;
    }

}
