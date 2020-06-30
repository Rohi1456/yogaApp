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

import appliedsyntax.io.yoga.acivity.ActivityAddLocation;
import appliedsyntax.io.yoga.adapter.LocationLVAdapter;
import appliedsyntax.io.yoga.model.Location;
import appliedsyntax.io.yoga.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLocations extends Fragment implements View.OnClickListener {


    public FragmentLocations() {
        // Required empty public constructor
    }

    ListView listView;
    LocationLVAdapter locationsLVAdapter;
    ArrayList<Location> locations = new ArrayList<>();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ProgressBar progressBar;
    FloatingActionButton AddLocation;
    SwipeRefreshLayout loadSwipe;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_locations, container, false);

        listView = view.findViewById(R.id.LocationsLVID);
        progressBar = view.findViewById(R.id.loadLocationPBID);
        AddLocation = view.findViewById(R.id.AddLocationFBID);AddLocation.setOnClickListener(this);
        LoadDataFromFirebase();
        locationsLVAdapter = new LocationLVAdapter(getActivity(), locations);
        listView.setAdapter(locationsLVAdapter);
        loadSwipe = view.findViewById(R.id.loadLocationSWRID);
        loadSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
        firestore.collection("Locations").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()&& task.getResult()!=null) {
                    locations.clear();
                    progressBar.setVisibility(View.GONE);
                    for (DocumentSnapshot doc : task.getResult()) {
                        Location location = doc.toObject(Location.class);
                        if (location != null) {
                            location.setDocID(doc.getId());
                            locations.add(location);
                        }
                        locationsLVAdapter.notifyDataSetChanged();
                        loadSwipe.setRefreshing(false);
                    }

                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.AddLocationFBID:
            {
                Intent i1 = new Intent(getActivity(),ActivityAddLocation.class);
                startActivity(i1);
                break;
            }
        }
    }
}
