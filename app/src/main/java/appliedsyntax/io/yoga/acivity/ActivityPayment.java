package appliedsyntax.io.yoga.acivity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import appliedsyntax.io.yoga.R;
import appliedsyntax.io.yoga.adapter.AutoCompleteAdapterName;
import appliedsyntax.io.yoga.model.FeePament;
import appliedsyntax.io.yoga.model.Students;

public class ActivityPayment extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    AutoCompleteTextView searchATID;
    TextView studentNameTVID,mobileTVID,classTVID, paidDateTVID, dueDateTVID;
    EditText amountPaidETID;
    RelativeLayout paidRL,dueRL;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    List<String> studentNames = new ArrayList<>();
    List<String> studentBatches = new ArrayList<>();
    List<String> studentMobNumbers = new ArrayList<>();
    String dateDialogue ="";
    int day,month,year;
    int dueDate,dueYear,dueMonth,paidDate,paidMonth,paidYear;
    TextView feeDueDate,feePaidDate;
    boolean selectedStudent = false;
    Button makePaymentBTNID;
    ImageView backAdds;
    boolean getIntent = false;
    Bundle bundle ;
    Students student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pament);
        if(getSupportActionBar()!=null)
        getSupportActionBar().hide();
        init();

    }

    private void init() {
        searchATID = findViewById(R.id.searchATID);
        studentNameTVID = findViewById(R.id.PstudentNameTVID);
        mobileTVID = findViewById(R.id.PstudentMobileTVID);
        classTVID = findViewById(R.id.PstudentClassTVID);
        paidDateTVID = findViewById(R.id.feePaidDateTVID);
        dueDateTVID = findViewById(R.id.feeDueDateTVID);
        feeDueDate = findViewById(R.id.feeDueDate);
        feePaidDate = findViewById(R.id.feePaidDate);
        makePaymentBTNID = findViewById(R.id.makePaymentBTNID); makePaymentBTNID.setOnClickListener(this);
        paidRL = findViewById(R.id.Fee_PaidRL); paidRL.setOnClickListener(this);
        dueRL = findViewById(R.id.FeeDueRL); dueRL.setOnClickListener(this);
        amountPaidETID = findViewById(R.id.PstudentAmountETID);
        autoCompleteTask();
        Calendar c  = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        Format formatter = new SimpleDateFormat("E, dd MMM yyyy ", Locale.getDefault());
        String todayDate = formatter.format(c.getTime());
        paidDateTVID.setText(todayDate);
        backAdds = findViewById(R.id.backADDS);
        paidYear = year;
        paidMonth = month+1;
        paidDate = day;
        backAdds.setOnClickListener(this);
        String date= year+"/"+month+"/"+day;
        feePaidDate.setText(date);
         bundle = getIntent().getExtras();
         if(bundle!=null)
         {
             getIntent = true;
             student = (Students)bundle.get("Student_Data");
             if(student!=null) {
                 studentNameTVID.setText(student.getSname());
                 mobileTVID.setText(student.getSmobileNumber());
                 classTVID.setText(student.getSbatch());
                 amountPaidETID.requestFocus();
             }
         }


    }

    private void autoCompleteTask() {

        firestore.collection("Students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()&& task.getResult()!=null)
                {
                    for(DocumentSnapshot doc:task.getResult())
                    {
                        Students student = doc.toObject(Students.class);
                        if(student!=null) {
                            studentNames.add(student.getSname());
                            studentBatches.add(student.getSbatch());
                            studentMobNumbers.add(student.getSmobileNumber());
                        }
                    }
                    final AutoCompleteAdapterName  adapter = new AutoCompleteAdapterName(ActivityPayment.this,R.layout.layout_student_search ,studentNames , studentBatches, studentMobNumbers,"text");
                    final AutoCompleteAdapterName  adapter_mobile = new AutoCompleteAdapterName(ActivityPayment.this,R.layout.layout_student_search ,studentMobNumbers , studentBatches, studentNames,"mobile");

                    searchATID.setAdapter(adapter);
                    searchATID.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            selectedStudent=true;
                            TextView name = view.findViewById(R.id.srcName);
                            TextView mobile = view.findViewById(R.id.srcMobile);
                            TextView Class = view.findViewById(R.id.srcClass);
                            studentNameTVID.setText(name.getText().toString());
                            mobileTVID.setText(mobile.getText().toString());
                            classTVID.setText(Class.getText().toString());
                        }
                    });

                    searchATID.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if(charSequence.length()==2)
                            {
                                //IF IT IS MOBILE NUMBER CHANGE THE ADAPTER
                                if(Character.isDigit(charSequence.charAt(0))&&Character.isDigit(charSequence.charAt(1)))
                                {
                                    searchATID.setAdapter(adapter_mobile); }
                                else
                                {
                                    searchATID.setAdapter(adapter);
                                }
                            }

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                }

            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.FeeDueRL:
            {
                dateDialogue = "feeDue";
                Show_Date_Picker();
                break;
            }
            case R.id.Fee_PaidRL:
            {
                dateDialogue = "feePaid";
                Show_Date_Picker();
                break;
            }
            case R.id.makePaymentBTNID:
            {
                if(validation())
                {
                    SendToFirebase();
                }
                break;
            }
            case R.id.backADDS:
            {
                finish();
                break;
            }
        }

    }

    private void SendToFirebase() {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS",Locale.getDefault());
            Date parsedDate = dateFormat.parse(dueYear+"-"+dueMonth+"-"+dueDate+" "+"00:00:00.000");
            Timestamp due_timestamp = new java.sql.Timestamp(parsedDate.getTime());

            Date parsedDate2 = dateFormat.parse(paidYear+"-"+paidMonth+"-"+paidDate+" "+"00:00:00.000");
            Timestamp paid_timestamp = new java.sql.Timestamp(parsedDate2.getTime());

//            Toast.makeText(this, feePaidDate.getText().toString()+","+feeDueDate.getText().toString(), Toast.LENGTH_SHORT).show();
            firestore.collection("Fees").add(new FeePament(due_timestamp, paid_timestamp,Integer.parseInt( amountPaidETID.getText().toString()),mobileTVID.getText().toString())).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    Toast.makeText(ActivityPayment.this, "Payment Success", Toast.LENGTH_SHORT).show();
                }
            });

        } catch(Exception e) { //this generic but you can control nother types of exception
            // look the origin of excption
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private boolean validation() {

        if(!selectedStudent&&!getIntent)
        {

            Toast.makeText(this, "Select a Student", Toast.LENGTH_SHORT).show();
            return false;

        }
        else if(amountPaidETID.getText().toString().equals(""))
        {
            Toast.makeText(this, "Please Enter The Amount", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(Integer.parseInt(amountPaidETID.getText().toString())<100)
        {
            Toast.makeText(this, "Min amount is  100", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(dueDateTVID.getText().toString().equals(""))
        {
            Toast.makeText(this, "select payment Due Date", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {

            return true;
        }
    }

    private void Show_Date_Picker() {


        if(dateDialogue.equals("feeDue")&&!dueDateTVID.getText().toString().equals(""))
        {
            new SpinnerDatePickerDialogBuilder()
                    .context(ActivityPayment.this)
                    .callback(this)
                    .spinnerTheme(R.style.NumberPickerStyle)
                    .showTitle(true)
                    .showDaySpinner(true)
                    .defaultDate(dueYear, dueMonth-1, dueDate)
                    .maxDate(2025, 0, 1)
                    .minDate(2000, 0, 1)
                    .build()
                    .show()
            ;

        }
        else {
            new SpinnerDatePickerDialogBuilder()
                    .context(ActivityPayment.this)
                    .callback(this)
                    .spinnerTheme(R.style.NumberPickerStyle)
                    .showTitle(true)
                    .showDaySpinner(true)
                    .defaultDate(year, month, day)
                    .maxDate(2025, 0, 1)
                    .minDate(2000, 0, 1)
                    .build()
                    .show()
            ;
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);
        Format formatter = new SimpleDateFormat("E, dd MMM yyyy ",Locale.getDefault());

        if(dateDialogue.equals("feeDue")) {
            dueDate = dayOfMonth; dueYear = year; dueMonth = monthOfYear+1;
          String  str = formatter.format(c.getTime());
          String date = year+"/"+monthOfYear+"/"+dayOfMonth;
          feeDueDate.setText(date);
          dueDateTVID.setText(str);
//            Toast.makeText(this,  feeDueDate.getText().toString(), Toast.LENGTH_SHORT).show();

        }
        else
        {
            paidDate = dayOfMonth; paidYear = year; paidMonth = monthOfYear+1;
            String str = formatter.format(c.getTime());
            String date = year+"/"+monthOfYear+"/"+dayOfMonth;
            feePaidDate.setText(date);
            paidDateTVID.setText(str);
//            Toast.makeText(this, feePaidDate.getText().toString(), Toast.LENGTH_SHORT).show();
        }



    }
}
