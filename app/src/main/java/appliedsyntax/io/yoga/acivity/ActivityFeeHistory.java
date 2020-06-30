package appliedsyntax.io.yoga.acivity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import appliedsyntax.io.yoga.R;
import appliedsyntax.io.yoga.adapter.FeeHistoryLVAdapter;
import appliedsyntax.io.yoga.model.FeePament;
import appliedsyntax.io.yoga.model.Students;

public class ActivityFeeHistory extends AppCompatActivity implements View.OnClickListener {

    ListView listView ;
    ProgressBar historyPBID;
    ArrayList<FeePament> feePayments = new ArrayList<>();
    FirebaseFirestore firestore =  FirebaseFirestore.getInstance();
    Students students;
    TextView titleTVID;
    ImageView backIVID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_history);
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
        listView = findViewById(R.id.feeHistoryLVID);
        historyPBID = findViewById(R.id.historyPBID);
        titleTVID = findViewById(R.id.titleTVID);
        titleTVID.setText(R.string.payment_history);
        backIVID = findViewById(R.id.backIVID); backIVID.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            students = (Students) bundle.get("Student_Data");
            LoadDataFromFirebase();
        }


    }

    private void LoadDataFromFirebase() {

        firestore.collection("Fees").whereEqualTo("studentID", students.getSmobileNumber()).orderBy("paidDate",Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()&& task.getResult()!=null)
                {
                    for(DocumentSnapshot doc: task.getResult())
                    {
                        FeePament feePayment = doc.toObject(FeePament.class);
                        feePayments.add(feePayment);
                    }
                    FeeHistoryLVAdapter adapter = new FeeHistoryLVAdapter(ActivityFeeHistory.this,feePayments);
                    listView.setAdapter(adapter);
                    historyPBID.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.backIVID:
            {
                finish();
                break;
            }
        }
    }
}
