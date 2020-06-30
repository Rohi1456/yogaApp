package appliedsyntax.io.yoga.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import appliedsyntax.io.yoga.R;
import appliedsyntax.io.yoga.acivity.ActivityHome;
import appliedsyntax.io.yoga.acivity.ActivityPayment;
import appliedsyntax.io.yoga.common.LoadDimensions;
import appliedsyntax.io.yoga.common.MySharedPreference;
import appliedsyntax.io.yoga.model.Students;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment implements View.OnClickListener {


    public FragmentHome() {
        // Required empty public constructor
    }
    TextView activeStudentsTVID,dateTVID;
    MaterialCardView feepayCardView ;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ProgressBar active_PBID;
    MaterialCardView studentsCVID,dateCVID;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        activeStudentsTVID = view.findViewById(R.id.activeStudentsTVID);
        feepayCardView = view.findViewById(R.id.feeCVID); feepayCardView.setOnClickListener(this);
        active_PBID = view.findViewById(R.id.activePBID);
        studentsCVID = view.findViewById(R.id.activeStudentsCVID); studentsCVID.setOnClickListener(this);
        dateTVID =  view.findViewById(R.id.dateTVID);
        dateCVID = view.findViewById(R.id.dateCVID);dateCVID.setOnClickListener(this);
        Calendar c = Calendar.getInstance();
        Format formatter = new SimpleDateFormat("E, dd MMM yyyy ",Locale.getDefault());
        String todayDate = formatter.format(c.getTime());
        dateTVID.setText(todayDate);
        getStudents();
        LoadDP(view);
        return view;
    }

    private void LoadDP(View view) {

        if(getActivity()!=null) {
            int dp_120 = MySharedPreference.getDp(getActivity(), "dp_120");
            int dp_H10 = MySharedPreference.getDp(getActivity(),"dp_H10");
            feepayCardView.getLayoutParams().height = dp_120;
            studentsCVID.getLayoutParams().height = dp_120;
            ViewGroup.MarginLayoutParams paramsDateCV = (ViewGroup.MarginLayoutParams) dateCVID.getLayoutParams();
            paramsDateCV.height = dp_120; paramsDateCV.leftMargin=dp_H10;paramsDateCV.rightMargin= dp_H10;paramsDateCV.bottomMargin=dp_H10;
            ViewGroup.MarginLayoutParams paramsFeeCV = (ViewGroup.MarginLayoutParams) feepayCardView.getLayoutParams();
            paramsFeeCV.height = dp_120; paramsFeeCV.leftMargin=dp_H10;paramsFeeCV.rightMargin= dp_H10;paramsFeeCV.bottomMargin=dp_H10;
            ViewGroup.MarginLayoutParams paramsStudentCV = (ViewGroup.MarginLayoutParams) studentsCVID.getLayoutParams();
            paramsStudentCV.height = dp_120; paramsStudentCV.leftMargin=dp_H10;paramsStudentCV.rightMargin= dp_H10;paramsStudentCV.bottomMargin=dp_H10;
        }
    }

    private void getStudents() {

        firebaseFirestore.collection("Students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    int i = 0;
                    int active = 0;
                    activeStudentsTVID.setVisibility(View.VISIBLE);
                    for (DocumentSnapshot doc : task.getResult()) {
                        i++;
                        Students student = doc.toObject(Students.class);
                        if (student != null) {
                            if (student.isSactive()) {
                                active++;
                                //Add 0's before number based on length
                                String s = Integer.toString(active);

                                if (s.length() == 1) {
                                    s = "00" + s;
                                }
                                if (s.length() == 2) {
                                    s = "0" + s;

                                }
                                activeStudentsTVID.setText(s);
                            }
                        }
                    }
                    if(i!=0) {
                        float f = (active * 100 / i);
                        active_PBID.setIndeterminate(false);
                        active_PBID.setProgress((int) f);
                    }
                }



            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.feeCVID:
            {
                Intent intent = new Intent(getActivity(),ActivityPayment.class);
                startActivity(intent);
                break;
            }
            case R.id.activeStudentsCVID:
            {
                    Intent intent = new Intent(getActivity(),ActivityHome.class);
                    intent.putExtra("fragment","students");
                    startActivity(intent);
            }
            case R.id.dateCVID:
            {
                Toast.makeText(getActivity(), "Today's Date", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
