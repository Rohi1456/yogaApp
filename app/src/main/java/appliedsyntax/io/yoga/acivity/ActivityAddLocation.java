package appliedsyntax.io.yoga.acivity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import appliedsyntax.io.yoga.model.Location;
import appliedsyntax.io.yoga.R;

public class ActivityAddLocation extends AppCompatActivity implements View.OnClickListener {

    EditText LocationNameETID,LocationAddressETID;
    ImageView back;
    Button submit;
    TextView titleLocation;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    boolean edit = false;
    Location location;
    ProgressBar locationPBID;
    String oldLocationName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__add_location);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().hide();
        }

        init();
    }

    private void init() {
        LocationNameETID = findViewById(R.id.locationNameETID);
        LocationAddressETID = findViewById(R.id.LocationAddressETID);
        back = findViewById(R.id.backADDL);
        back.setOnClickListener(this);
        submit = findViewById(R.id.add_LocationIMVID);
        submit.setOnClickListener(this);
        titleLocation = findViewById(R.id.title_location);
        locationPBID = findViewById(R.id.locationPBID);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            location = (Location)bundle.get("location_data");
            if(location!=null) {
                LocationNameETID.setText(location.getName());
                oldLocationName = location.getName();
                LocationAddressETID.setText(location.getAddress());
                titleLocation.setText(R.string.edit_location);
                submit.setText(R.string.save);
                edit = true;
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.backADDL:
            {
                finish();
                break;
            }
            case R.id.add_LocationIMVID:
            {
                boolean validation = validate();
                if(validation)
                {
                    if(edit)
                    {
                        saveData();
                    }
                    else {
                        sentToFirebase();
                    }
                }
                break;
            }
        }
    }

    private void saveData() {

        location.setName(LocationNameETID.getText().toString());
        location.setAddress(LocationAddressETID.getText().toString());
        submit.setVisibility(View.GONE);
        locationPBID.setVisibility(View.VISIBLE);
        // CHECK FOR UNIQUE
        firebaseFirestore.collection("Locations").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                boolean unique = true;
                if(task.isSuccessful()&&task.getResult()!=null)
                {
                    for(DocumentSnapshot doc : task.getResult())
                    {
                        Location location = doc.toObject(Location.class);
                        if(location!=null && LocationNameETID.getText().toString().equals(location.getName()) && !LocationNameETID.getText().toString().equals(oldLocationName))
                        {
                            unique =false;
                        }
                    }
                    if(unique)
                    { firebaseFirestore.collection("Locations").document(location.getDocID()).set(location,SetOptions.merge());
                        finish();
                    }
                    else
                    {
                        submit.setVisibility(View.VISIBLE);
                        locationPBID.setVisibility(View.GONE);
                        Toast.makeText(ActivityAddLocation.this, "Location name already exist", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    private void sentToFirebase() {
        submit.setVisibility(View.GONE);
        locationPBID.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("Locations").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                boolean unique = true;
                if(task.isSuccessful() && task.getResult()!=null)
                {
                    for(DocumentSnapshot doc : task.getResult())
                    {
                        Location location = doc.toObject(Location.class);
                        if(location!=null && LocationNameETID.getText().toString().equals(location.getName()))
                        {
                            unique =false;
                        }
                    }
                    if(unique)
                    {
                        firebaseFirestore.collection("Locations").add(new Location(LocationNameETID.getText().toString(),LocationAddressETID.getText().toString() )).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(ActivityAddLocation.this, " New Location Added", Toast.LENGTH_SHORT).show();
                                finish();
                                LocationAddressETID.setText("");LocationNameETID.setText("");
                            }
                        });
                    }
                    else
                    {
                        submit.setVisibility(View.VISIBLE);
                        locationPBID.setVisibility(View.GONE);
                        Toast.makeText(ActivityAddLocation.this, "Location name already exist", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private boolean validate() {

        if(LocationNameETID.getText().toString().equals("")|| LocationNameETID.getText().toString().length()<3)
        {
            Toast.makeText(this, "Enter Location name min length 3", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(LocationAddressETID.getText().toString().equals("") || LocationAddressETID.getText().length()<20)
        {
            Toast.makeText(this, "Enter Location Address min Length 20", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {

            return true;
        }
    }
}
