package appliedsyntax.io.yoga.acivity;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;

import appliedsyntax.io.yoga.adapter.SelectLocationLVAdapter;
import appliedsyntax.io.yoga.model.Batch;
import appliedsyntax.io.yoga.model.Location;
import appliedsyntax.io.yoga.R;

public class ActivityAddClass extends AppCompatActivity implements View.OnClickListener {

    EditText BatchNameETID;
    TextView BatchLocationETID,titleTVID;
    ImageView back,logoIVID;
    Button submit;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ArrayList<Location> locations;
    Batch batch;
    String oldBatchName;
    boolean edit = false;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ProgressBar loadPBID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__add_class);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().hide();
        }

        init();
    }
    private void init() {
        BatchNameETID = findViewById(R.id.BatchNameETID);
        BatchLocationETID = findViewById(R.id.BatchLocationETID);BatchLocationETID.setOnClickListener(this);
        back = findViewById(R.id.backADDB);
        titleTVID = findViewById(R.id.title_location);
        back.setOnClickListener(this);
        submit = findViewById(R.id.add_BatchIMVID);
        logoIVID = findViewById(R.id.logo);
        submit.setOnClickListener(this);
        loadPBID = findViewById(R.id.BatchPBID);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            batch = (Batch) bundle.get("class_data");
            if (batch != null) {
                BatchNameETID.setText(batch.getName());
                oldBatchName = batch.getName();
                BatchLocationETID.setText(batch.getLocation());
                submit.setText(R.string.save);
                titleTVID.setText(R.string.edit_class);
                edit = true;
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.backADDB:
            {
                finish();
                break;
            }
            case R.id.add_BatchIMVID:
            {

                boolean validation = validate();
                if(validation)
                {
                    if(edit) {
                        SaveData();
                    }
                    else {
                        sentToFirebase();
                    }

                }
                break;
            }
            case R.id.BatchLocationETID:
            {
                    selectBatch();
            }
        }
    }

    private void SaveData() {

        batch.setName(BatchNameETID.getText().toString());
        batch.setLocation(BatchLocationETID.getText().toString());
        submit.setVisibility(View.GONE);
        loadPBID.setVisibility(View.VISIBLE);
        firestore.collection("Classes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                boolean unique = true;
                if(task.isSuccessful() && task.getResult()!=null)
                {
                    for(DocumentSnapshot doc : task.getResult())
                    {
                        Batch batch = doc.toObject(Batch.class);
                        if(batch!=null)
                        if(batch.getName().equals(BatchNameETID.getText().toString()) && ! batch.getName().equals(oldBatchName))
                        {
                            unique = false;
                        }
                    }
                    if(unique)
                    {
                        firestore.collection("Classes").document(batch.getDocID()).set(batch,SetOptions.merge());
                        finish();
                    }
                    else
                    {
                        loadPBID.setVisibility(View.GONE);
                        submit.setVisibility(View.VISIBLE);
                        Toast.makeText(ActivityAddClass.this, "Batch name already exist", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    private void sentToFirebase() {
        submit.setVisibility(View.GONE);
        loadPBID.setVisibility(View.VISIBLE);
        firestore.collection("Classes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                boolean unique = true;
                if(task.isSuccessful() && task.getResult()!=null)
                {
                    for(DocumentSnapshot doc : task.getResult())
                    {
                        Batch batch = doc.toObject(Batch.class);
                        if(batch!=null)
                        if(batch.getName().equals(BatchNameETID.getText().toString()))
                        {
                            unique = false;
                        }
                    }
                    if(unique)
                    {
                        firebaseFirestore.collection("Classes").add(new Batch(BatchNameETID.getText().toString(),BatchLocationETID.getText().toString() )).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(ActivityAddClass.this, " New class added", Toast.LENGTH_SHORT).show();
                                finish();
                                BatchNameETID.setText("");BatchLocationETID.setText("");
                            }
                        });
                    }
                    else
                    {
                        loadPBID.setVisibility(View.GONE);
                        submit.setVisibility(View.VISIBLE);
                        Toast.makeText(ActivityAddClass.this, "Class name already exist", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private boolean validate() {

        if(BatchNameETID.getText().toString().equals("")|| BatchNameETID.getText().toString().length()<3)
        {
            Toast.makeText(this, "Enter class name min length 3", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(BatchLocationETID.getText().toString().equals("") || BatchLocationETID.getText().length()<5)
        {
            Toast.makeText(this, "Select class location", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {

            return true;
        }
    }
    private void selectBatch() {

        locations = new ArrayList<>();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.list_batches);
        final ListView listView = dialog.findViewById(R.id.selectBatchLV);
        final ProgressBar load = dialog.findViewById(R.id.load_batches_PBID);
        final TextView close = dialog.findViewById(R.id.close_LVBatch);
        final TextView title = dialog.findViewById(R.id.title_listTVID);
        title.setText(R.string.locations);
        firebaseFirestore.collection("Locations").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful() && task.getResult()!=null) {
                    for (DocumentSnapshot doc : task.getResult()) {
                       Location location = doc.toObject(Location.class);
                        locations.add(location);
                    }
                    load.setVisibility(View.GONE);
                    SelectLocationLVAdapter adapter =new SelectLocationLVAdapter(ActivityAddClass.this,locations );
                    listView.setAdapter(adapter);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ActivityAddClass.this, "Error while getting the projects", Toast.LENGTH_SHORT).show();
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

                BatchLocationETID.setText(locations.get(i).getName());
                dialog.dismiss();
            }
        });

    }
}
