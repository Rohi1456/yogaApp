package appliedsyntax.io.yoga.acivity;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Locale;

import appliedsyntax.io.yoga.adapter.SelectBatcheLVAdapter;
import appliedsyntax.io.yoga.common.MySharedPreference;
import appliedsyntax.io.yoga.model.Batch;
import appliedsyntax.io.yoga.model.Students;
import appliedsyntax.io.yoga.R;

public class ActivityAddStudent extends AppCompatActivity implements View.OnClickListener {

    EditText nameETID, mobileETID, ageETID;
    TextView batchETID, feePayStatusETID,activeStatusTVID,titleTVID;
    ImageView  back,profileIVID;
    Button submit;
    String location;
    ArrayList<Batch> batches;
    ProgressBar checkPBID;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    Students student_Data;
    boolean edit=false;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    LinearLayout studentLLID;
    String oldMobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        init();
        LoadDP();
    }

    private void LoadDP() {
        int dp_110 = MySharedPreference.getDp(this, "dp_110");
        int dp_H20 = MySharedPreference.getDp(this,"dp_H20");
        int dp_20 = MySharedPreference.getDp(this,"dp_20");
        int dp_70 = MySharedPreference.getDp(this,"dp_70");
        int dp_H70 = MySharedPreference.getDp(this,"dp_H70");
        int dp_30 = MySharedPreference.getDp(this,"dp_30");
        int dp_10 = MySharedPreference.getDp(this,"dp_10");
        ViewGroup.MarginLayoutParams paramsSubmit = (ViewGroup.MarginLayoutParams) submit.getLayoutParams();
        paramsSubmit.topMargin=(dp_110); paramsSubmit.bottomMargin = dp_10;
        ViewGroup.MarginLayoutParams paramsLLID = (ViewGroup.MarginLayoutParams) studentLLID.getLayoutParams();
        paramsLLID.topMargin = dp_20; paramsLLID.bottomMargin = dp_20; paramsLLID.rightMargin = dp_H20; paramsLLID.leftMargin=dp_H20;
        ViewGroup.MarginLayoutParams paramsProfile = (ViewGroup.MarginLayoutParams) profileIVID.getLayoutParams();
        paramsProfile.bottomMargin = dp_30; paramsProfile.width = dp_70; paramsProfile.height = dp_70;
        ViewGroup.MarginLayoutParams paramsName = (ViewGroup.MarginLayoutParams) nameETID.getLayoutParams();
        paramsName.topMargin= dp_10;
        ViewGroup.MarginLayoutParams paramsMobile = (ViewGroup.MarginLayoutParams) mobileETID.getLayoutParams();
        paramsMobile.topMargin= dp_10;
        ViewGroup.MarginLayoutParams paramsBatch = (ViewGroup.MarginLayoutParams) batchETID.getLayoutParams();
        paramsBatch.topMargin= dp_10;
        ViewGroup.MarginLayoutParams paramsAge= (ViewGroup.MarginLayoutParams) ageETID.getLayoutParams();
        paramsAge.topMargin= dp_10;
        ViewGroup.MarginLayoutParams paramsActive = (ViewGroup.MarginLayoutParams) activeStatusTVID.getLayoutParams();
        paramsActive.topMargin= dp_10;
        ViewGroup.MarginLayoutParams paramsPB = (ViewGroup.MarginLayoutParams) checkPBID.getLayoutParams();
       paramsPB.topMargin= dp_110; paramsPB.bottomMargin=dp_10;




    }

    private void init() {
        profileIVID = findViewById(R.id.profileIVID);
        studentLLID = findViewById(R.id.studentLLID);
        nameETID = findViewById(R.id.nameETID);
        mobileETID = findViewById(R.id.mobileNumberETID);
        batchETID = findViewById(R.id.batchETID);
        feePayStatusETID = findViewById(R.id.fee_payETID); feePayStatusETID.setOnClickListener(this);
        activeStatusTVID = findViewById(R.id.ActiveTVID); activeStatusTVID.setOnClickListener(this);
        titleTVID = findViewById(R.id.titleTVID);
        submit = findViewById(R.id.add_studentIMVID);
        submit.setOnClickListener(this);
        batchETID.setOnClickListener(this);
        ageETID = findViewById(R.id.ageETID);
        back = findViewById(R.id.backADDS);
        checkPBID = findViewById(R.id.add_studentPBVID);
        back.setOnClickListener(this);
        Bundle b1 = getIntent().getExtras();

        if(getIntent()!=null && b1!=null ) {
            student_Data = (Students) b1.get("Student_Data");
            if (student_Data != null) {
                nameETID.setText(student_Data.getSname());
                mobileETID.setText(student_Data.getSmobileNumber());
                batchETID.setText(student_Data.getSbatch());
                ageETID.setText(String.format(Locale.getDefault(),"%d",student_Data.getSage()));
                oldMobileNumber = student_Data.getSmobileNumber();
                if (student_Data.isSfees_pay_status()) {
                    feePayStatusETID.setText(R.string.yes);
                } else {
                    feePayStatusETID.setText(R.string.no);
                }
                activeStatusTVID.setVisibility(View.VISIBLE);
                if (student_Data.isSactive()) {
                    activeStatusTVID.setText(R.string.make_in_active);
                } else {
                    activeStatusTVID.setText(R.string.make_active);
                }
                submit.setText(R.string.yes);
                titleTVID.setText(R.string.edit_stident);
                edit = true;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_studentIMVID: {
                if(edit)
                {
                    SaveData();
                }
                else {
                    boolean validation = validate();
                    if (validation) {
                        sentToFirebase();
                    }
                }
                break;
            }
            case R.id.backADDS: {
                finish();
                break;
            }
            case R.id.batchETID:
            {
                selectBatch();
                break;
            }
            case R.id.ActiveTVID:
            {
                if(student_Data.isSactive())
                {
                    student_Data.setSactive(false);
                    activeStatusTVID.setText(R.string.make_active);
                }
                else
                {
                    student_Data.setSactive(true);
                    activeStatusTVID.setText(R.string.make_in_active);
                }
                break;
            }
        }
    }

    private void SaveData() {
        checkPBID.setVisibility(View.VISIBLE);
        submit.setVisibility(View.GONE);
        if(validate()) {
            firebaseFirestore.collection("Students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful() && task.getResult()!=null)
                    {
                        boolean unique = true;
                        for(DocumentSnapshot doc: task.getResult())
                        {
                            Students student = doc.toObject(Students.class);
                            if(student!=null && mobileETID.getText().toString().equals(student.getSmobileNumber()) && !mobileETID.getText().toString().equals(oldMobileNumber))
                            {
                                unique = false;
                            }
                        }
                        if(unique)
                        {
                            student_Data.setSname(nameETID.getText().toString());
                            student_Data.setSmobileNumber(mobileETID.getText().toString());
                            student_Data.setSage(Integer.parseInt(ageETID.getText().toString()));
                            student_Data.setSbatch(batchETID.getText().toString());
                            if(oldMobileNumber.equals(mobileETID.getText().toString()))
                            {
                                firestore.collection("Students").document(student_Data.getDocumentID()).set(student_Data, SetOptions.merge());
                                Intent intent = new Intent(ActivityAddStudent.this,ActivityHome.class);
                                startActivity(intent);
                            }
                            else {

                                firestore.collection("Students").document(oldMobileNumber).delete();
                                firestore.collection("Students").document(mobileETID.getText().toString()).set(student_Data, SetOptions.merge());
                                Intent intent = new Intent(ActivityAddStudent.this,ActivityHome.class);
                                startActivity(intent);
                            }

                        }
                        else
                        {
                            submit.setVisibility(View.VISIBLE);
                            checkPBID.setVisibility(View.GONE);
                            Toast.makeText(ActivityAddStudent.this, "user already exist with same mobile number", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });


        }
    }

    private void sentToFirebase() {

        checkPBID.setVisibility(View.VISIBLE);
        submit.setVisibility(View.GONE);
        firebaseFirestore.collection("Students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && task.getResult()!=null)
                {
                    boolean unique = true;
                    for(DocumentSnapshot doc :task.getResult())
                    {

                        if(doc.getId().equals(mobileETID.getText().toString()))
                        {
                            unique = false;
                        }
                    }
                    if(unique)
                    {

                        firebaseFirestore.collection("Students").document(mobileETID.getText().toString()).set(new Students(nameETID.getText().toString(), batchETID.getText().toString(), location, mobileETID.getText().toString(),false, true, Integer.parseInt(ageETID.getText().toString()), "")).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ActivityAddStudent.this, "Student added", Toast.LENGTH_SHORT).show();
                                finish();
                                mobileETID.setText("");
                                nameETID.setText("");
                                ageETID.setText("");
                                batchETID.setText("");
                                feePayStatusETID.setText("");
                            }
                        });
                    }
                    else
                    {
                        submit.setVisibility(View.VISIBLE);
                        checkPBID.setVisibility(View.GONE);
                        Toast.makeText(ActivityAddStudent.this, "user already exist with same mobile number", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    private boolean validate() {

        if (mobileETID.getText().toString().equals("") || mobileETID.length() < 10) {
            Toast.makeText(this, "Enter 10 digit mobile number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (nameETID.getText().toString().equals("") || nameETID.length() < 3) {
            Toast.makeText(this, "Enter Full name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (ageETID.getText().toString().equals("")) {
            Toast.makeText(this, "Enter Age", Toast.LENGTH_SHORT).show();
            return false;
        } else if (batchETID.getText().toString().equals("")) {
            Toast.makeText(this, "Select Class", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {

            return true;
        }

    }

 /*   private boolean uniqueMobileNumber() {
        final boolean[] valid = {true};

        return valid[0];
    }
*/
    private void selectBatch() {
        batches = new ArrayList<>();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.list_batches);
        final ListView listView = dialog.findViewById(R.id.selectBatchLV);
        final ProgressBar load = dialog.findViewById(R.id.load_batches_PBID);
        final TextView close = dialog.findViewById(R.id.close_LVBatch);
        final View top_line = dialog.findViewById(R.id.top_lineVID);

       firebaseFirestore.collection("Classes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful() && task.getResult()!=null) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        Batch batch = doc.toObject(Batch.class);
                        batches.add(batch);
                    }
                    load.setVisibility(View.GONE);
                    SelectBatcheLVAdapter adapter = new SelectBatcheLVAdapter(ActivityAddStudent.this,batches );
                    listView.setAdapter(adapter);
                    top_line.setVisibility(View.VISIBLE);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ActivityAddStudent.this, "Error while getting projects", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                batchETID.setText(batches.get(i).getName());
               location = batches.get(i).getLocation();
                dialog.dismiss();
            }
        });

    }
}
