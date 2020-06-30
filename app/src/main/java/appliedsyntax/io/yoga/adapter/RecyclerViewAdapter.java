package appliedsyntax.io.yoga.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

import appliedsyntax.io.yoga.R;
import appliedsyntax.io.yoga.acivity.ActivityStudentDetails;
import appliedsyntax.io.yoga.model.Students;

public class RecyclerViewAdapter extends android.support.v7.widget.RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Students> students;
    private Context context;
    private ArrayList<Students> filteredStudents;

    public RecyclerViewAdapter(Context context, ArrayList<Students> students) {
        this.students = students;
        this.filteredStudents = students;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        holder.Sname.setText(filteredStudents.get(position).getSname());
//        holder.Smobile.setText(students.get(position).getSmobileNumber());
//        holder.Slocation.setText(", "+students.get(position).getSlocation());
        holder.Sbatch.setText(filteredStudents.get(position).getSbatch());
        holder.Smobile.setText(filteredStudents.get(position).getSmobileNumber());
        if(students.get(position).isSfees_pay_status())
    {
        holder.Sfee_status.setText("Yes");
    }
        else
    {
        holder.Sfee_status.setText("No");
    }
    if(!filteredStudents.get(position).getSprofilepic().equals("")) {
        Glide.with(context).load(filteredStudents.get(position).getSprofilepic()).into(holder.SprofilePic);
    }
    else
    {
        Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/yoga-5ebef.appspot.com/o/l60Hf.png?alt=media&token=47e31651-a1ee-4840-93dd-3af6b7140c78").into(holder.SprofilePic);

    }
        if(filteredStudents.get(position).isSactive())
    {
        holder.active_line.setVisibility(View.GONE);

    }
        else
    {
        holder.active_line.setVisibility(View.VISIBLE);
    }

    holder.studentCard.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context,ActivityStudentDetails.class);
            intent.putExtra("Student_Data",filteredStudents.get(position));
            context.startActivity(intent);
        }
    });
    }

    @Override
    public int getItemCount() {
        return filteredStudents.size();
    }


    class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        TextView Sname,Smobile,Sbatch;
        CircularImageView SprofilePic;
        TextView Sfee_status,SactiveStatus;
        View active_line;
        CardView studentCard;


        ViewHolder(View itemView) {
            super(itemView);
            Smobile = itemView.findViewById(R.id.phone_num);
            Sname = itemView.findViewById(R.id.SnameTVID);
            Sbatch = itemView.findViewById(R.id.SbatchTVID);
//            Slocation = itemView.findViewById(R.id.SlocationTVID);
            SprofilePic = itemView.findViewById(R.id.profileIVID);
            Sfee_status = itemView.findViewById(R.id.SfeeStatus);
            active_line = itemView.findViewById(R.id.active_line);
//            SactiveStatus = itemView.findViewById(R.id.SactiveStatus);
            studentCard = itemView.findViewById(R.id.student_card);

        }
    }


    public Filter getFilter()
    {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredStudents = students;
                } else {
                    ArrayList<Students> filteredList = new ArrayList<>();
                    for (Students row : students) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match or class match
                        if (row.getSname().toLowerCase().contains(charString.toLowerCase()) || row.getSmobileNumber().contains(charSequence) || row.getSbatch().contains(charSequence) ) {
                            filteredList.add(row);
                        }
                    }

                    filteredStudents = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredStudents;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
              filteredStudents = (ArrayList<Students>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}


