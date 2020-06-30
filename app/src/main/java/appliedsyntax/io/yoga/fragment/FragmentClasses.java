package appliedsyntax.io.yoga.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import appliedsyntax.io.yoga.acivity.ActivityAddClass;
import appliedsyntax.io.yoga.adapter.BatchesLVAdapter;
import appliedsyntax.io.yoga.model.Batch;
import appliedsyntax.io.yoga.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentClasses extends Fragment implements View.OnClickListener {


    public FragmentClasses() {
        // Required empty public constructor
    }
    ListView listView;
    BatchesLVAdapter batchesLVAdapter;
    ArrayList<Batch> batches = new ArrayList<>();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ProgressBar progressBar;
    FloatingActionButton AddBatch;
    SwipeRefreshLayout loadClasses;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_classes, container, false);
        listView = view.findViewById(R.id.BatchesLVID);
        progressBar = view.findViewById(R.id.loadBatchPBID);
        AddBatch = view.findViewById(R.id.AddBatchFBID);
        loadClasses  = view.findViewById(R.id.loadClassesSRID);
        AddBatch.setOnClickListener(this);
        LoadDataFromFirebase();
        batchesLVAdapter = new BatchesLVAdapter(getActivity(),batches );
        listView.setAdapter(batchesLVAdapter);
        loadClasses.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadDataFromFirebase();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadDataFromFirebase();
    }

    private void LoadDataFromFirebase() {
        firestore.collection("Classes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful() && task.getResult()!=null) {
                    batches.clear();
                    progressBar.setVisibility(View.GONE);
                    for (DocumentSnapshot doc : task.getResult()) {
                        Batch batch = doc.toObject(Batch.class);
                        if (batch != null) {
                            batch.setDocID(doc.getId());
                            batches.add(batch);
                            loadClasses.setRefreshing(false);
                        }
                        batchesLVAdapter.notifyDataSetChanged();
                    }

                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.AddBatchFBID:
            {
                Intent i1 = new Intent(getActivity(),ActivityAddClass.class);
                startActivity(i1);
                break;
            }
        }
    }
}
