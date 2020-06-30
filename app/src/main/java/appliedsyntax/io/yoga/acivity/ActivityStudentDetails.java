package appliedsyntax.io.yoga.acivity;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import appliedsyntax.io.yoga.R;
import appliedsyntax.io.yoga.common.Contact;
import appliedsyntax.io.yoga.model.Students;

public class ActivityStudentDetails extends AppCompatActivity implements View.OnClickListener {

    ImageView StudentIV,back;
    Students student_Data;
    String profile_pic,phoneNum;
    TextView sendSMSTVID,makeCallTVID,moneyPaidTVID,ageTVID,classTVID,locationTVID,historyTVID;
    FloatingActionButton editStudentFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().hide();
        }
        init();

    }

    private void init() {

        CollapsingToolbarLayout collapsingToolbarLayout=findViewById(R.id.collapsin);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.Collpase_Mode_Title);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expand_mode_title);
        sendSMSTVID = findViewById(R.id.sendSMSTVID); sendSMSTVID.setOnClickListener(this);
        back  = findViewById(R.id.backDIVID); back.setOnClickListener(this);
        StudentIV  = findViewById(R.id.student_image);
        makeCallTVID = findViewById(R.id.makeCallTVID);makeCallTVID.setOnClickListener(this);
        moneyPaidTVID = findViewById(R.id.moneyPaidTVID); moneyPaidTVID.setOnClickListener(this);
        editStudentFB = findViewById(R.id.editStudentFBID); editStudentFB.setOnClickListener(this);
        historyTVID = findViewById(R.id.historyDTVID); historyTVID.setOnClickListener(this);
        ageTVID = findViewById(R.id.ageDTVID);
        classTVID = findViewById(R.id.classDTVID);
        locationTVID = findViewById(R.id.locationDTVID);
        Bundle bundle = getIntent().getExtras();
//                                       GET THE PROFILE DATA
        if(bundle!=null)
        student_Data = (Students) bundle.get("Student_Data");
        if(student_Data!=null) {
            profile_pic = student_Data.getSprofilepic();    //set profile pic
            if (profile_pic.equals("")) {
                Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/yoga-5ebef.appspot.com/o/back.png?alt=media&token=88a67e2b-ccc9-49eb-8f2e-0209c93b6e12").into(StudentIV);
            } else {
                Glide.with(this).load(profile_pic).into(StudentIV);
            }
            collapsingToolbarLayout.setTitle(student_Data.getSname()); //set profile name
            phoneNum = student_Data.getSmobileNumber();  // set profile name
            String age = student_Data.getSage() + " years";
            ageTVID.setText(age);
            classTVID.setText(student_Data.getSbatch());
            locationTVID.setText(student_Data.getSlocation());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.backDIVID:
            {
                finish();
                break;
            }
            case R.id.sendSMSTVID:
            {
                Contact.openWhatsApp(phoneNum,this);
                break;
            }
            case R.id.makeCallTVID:
            {
                Contact.makeCall(phoneNum,this);
                break;
            }
            case R.id.moneyPaidTVID:
            {
                Intent intent = new Intent(this,ActivityPayment.class);
                intent.putExtra("Student_Data",student_Data);
                startActivity(intent);
                break;
            }
            case R.id.editStudentFBID:
            {
                Intent intent = new Intent(this,ActivityAddStudent.class);
                intent.putExtra("Student_Data",student_Data);
                startActivity(intent);
                break;
            }
            case R.id.historyDTVID:
            {
                Intent intent = new Intent(this,ActivityFeeHistory.class);
                intent.putExtra("Student_Data",student_Data);
                startActivity(intent);
                break;
            }


        }
    }
}
