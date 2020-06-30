package appliedsyntax.io.yoga.fragment;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.EventListener;
import appliedsyntax.io.yoga.acivity.ActivityAddStudent;
import appliedsyntax.io.yoga.adapter.RecyclerViewAdapter;
import appliedsyntax.io.yoga.common.Contact;
import appliedsyntax.io.yoga.common.KeyboardUtils;
import appliedsyntax.io.yoga.customfonts.EditText_Roboto_Light;
import appliedsyntax.io.yoga.model.Students;
import appliedsyntax.io.yoga.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentStudents extends Fragment implements View.OnClickListener,EventListener {


    public FragmentStudents() {
        // Required empty public constructor
    }


    RecyclerViewAdapter recyclerViewAdapter;
    RecyclerView recyclerView;
    ArrayList<Students> students = new ArrayList<>();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ProgressBar progressBar;
    FloatingActionButton AddStudent;
    private SwipeRefreshLayout swipeRefreshLayout;
    EditText_Roboto_Light searchETID;
    private static final int REQUEST_PHONE_CALL = 123;
    private static final int REQUEST_PHONE_SMS=1456;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_students, container, false);
//        listView = view.findViewById(R.id.StudentsLVID);
        progressBar = view.findViewById(R.id.progress_Bar);
        searchETID = view.findViewById(R.id.searchETID);
        AddStudent = view.findViewById(R.id.AddStudentFBID);
        AddStudent.setOnClickListener(this);
        LoadDataFromFirebase();
        swipeRefreshLayout = view.findViewById(R.id.refreshStudents_swID);
        //recylcer view'
        recyclerView = view.findViewById(R.id.students_RVID);
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), students);
//        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(recyclerViewAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadDataFromFirebase();
            }
        });

        //HIDE FLOATING ACTION BUTTON ON SCROLL Recycler View

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && AddStudent.isShown()) {
                    AddStudent.hide();
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    AddStudent.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        //  HIDE FLOATING ACTION BUTTON ON  CLICK EDIT TEXT
        KeyboardUtils.addKeyboardToggleListener(getActivity(), new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                if (isVisible) {
                    AddStudent.hide();
                } else {
                    AddStudent.show();
                }
            }
        });
        setUpSwipeForRecyclerView();

        searchETID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                recyclerViewAdapter.getFilter().filter(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void setUpSwipeForRecyclerView() {

        ItemTouchHelper.SimpleCallback swipe = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                View itemView = viewHolder.itemView;
                TextView phoneTv = itemView.findViewById(R.id.phone_num);
                String phoneNum =phoneTv.getText().toString();

                if (i == ItemTouchHelper.LEFT)
                {
                    if(getActivity()!=null)
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS},REQUEST_PHONE_SMS);
                        }
                        else
                        {
                            Contact.openWhatsApp(phoneNum,getActivity() );
                        }
                    }
                    else
                    {
                        Contact.openWhatsApp(phoneNum,getActivity() );
                    }
                } else {

                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+ phoneNum));
                    if(getActivity()!=null)

                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                            }
                            else
                            {
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            startActivity(intent);
                        }


                }
                recyclerViewAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildDraw (@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                    Paint p=new Paint();

                    if(dX > 0){
                        p.setColor(Color.parseColor("#49a55a"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        //icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit);

                        float left = (float) itemView.getLeft() + width;
                        float top = (float) itemView.getTop() + width;
                        float right = (float) itemView.getLeft() + 2 * width;
                        float bottom = (float)itemView.getBottom() - width;

                        icon = getBitmapFromVectorDrawable(getActivity(), R.drawable.swipe_call);
                        RectF iconDest = new RectF(left, top, right, bottom);

                        c.drawBitmap(icon,null,iconDest,p);
                    } else if (dX < 0) {
                        p.setColor(Color.parseColor("#49a55a"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        //icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete);
                        icon = getBitmapFromVectorDrawable(getActivity(), R.drawable.swipe_mail);

                        float left = (float) itemView.getRight() - 2*width;
                        float top = (float) itemView.getTop() + width;
                        float right = (float) itemView.getRight() - width;
                        float bottom = (float)itemView.getBottom() - width;
                        RectF iconDest = new RectF(left, top, right,bottom);

                        c.drawBitmap(icon,null,iconDest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }


        };

        // attaching the touch helper (Swipe) to recycler view
        new ItemTouchHelper(swipe).attachToRecyclerView(recyclerView);
    }

    private void LoadDataFromFirebase() {
        firestore.collection("Students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful() && task.getResult()!=null)
                {
                    students.clear();
                    progressBar.setVisibility(View.GONE);
                    for (DocumentSnapshot doc : task.getResult()) {
                        Students Student = doc.toObject(Students.class);
                        if(Student!=null) {
                            Student.setDocumentID(doc.getId());
                            students.add(Student);
                        }
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerViewAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
//                        runLayoutAnimation(recyclerView);
                    }

                }

            }
        });
    }




    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.AddStudentFBID:
            {
                Intent i1 = new Intent(getActivity(),ActivityAddStudent.class);
                startActivity(i1);
                break;
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();

//        LoadDataFromFirebase();
    }
/*    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_from_right);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }*/
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if(drawable!=null)
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = null;
        if (drawable != null) {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas ;
            if (bitmap != null) {
                canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
            }


        }

        return bitmap;
    }



}
