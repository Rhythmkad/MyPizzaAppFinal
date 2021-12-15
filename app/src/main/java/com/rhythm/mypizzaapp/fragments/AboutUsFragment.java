package com.rhythm.mypizzaapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rhythm.mypizzaapp.GlobalConstants.GlobalConstants;
import com.rhythm.mypizzaapp.MainActivity;
import com.rhythm.mypizzaapp.R;

/**
 * AboutUs Fragment to show the StudentName, StudentId and CourseCode
 * */

public class AboutUsFragment extends Fragment {

    private TextView mStudentNameTv, mStudentIdTv, mCourseTv;
    private ImageView mStudentNameIcon, mStudentIdIcon, mCourseIcon;
    private Context mContext;

    // constructor
    public AboutUsFragment() {
        // Required empty public constructor
    }

    //fragment lifecycle method
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // fragment lifecycle method
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    // fragment lifecycle method
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initializing views
        initialize(view);

        // some run time changes to be done programmatically
        runTimeChanges();
    }

    // onAttach fragment lifecycle method
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    // onResume fragment lifecycle method
    @Override
    public void onResume() {
        super.onResume();
        // setting toolBar title according to fragment
        ((MainActivity) mContext).setToolbarTitle(GlobalConstants.FRAGMENT.ABOUT_US);
    }

    /*
     * initializing id's
     * */
    private void initialize(View view) {

        View studentNameLayout = view.findViewById(R.id.student_name_layout);
        View studentIdLayout = view.findViewById(R.id.student_id_layout);
        View courseLayout = view.findViewById(R.id.course_code_layout);

        /* textView */
        mStudentNameTv = studentNameLayout.findViewById(R.id.aboutTv);
        mStudentIdTv = studentIdLayout.findViewById(R.id.aboutTv);
        mCourseTv = courseLayout.findViewById(R.id.aboutTv);

        /* imageView */
        mStudentNameIcon = studentNameLayout.findViewById(R.id.aboutIv);
        mStudentIdIcon = studentIdLayout.findViewById(R.id.aboutIv);
        mCourseIcon = courseLayout.findViewById(R.id.aboutIv);
    }

    /*
     * Runtime changes
     * */
    private void runTimeChanges() {

        mStudentNameTv.setText(mContext.getResources().getString(R.string.Rhythm_kad));
        mStudentIdTv.setText(mContext.getResources().getString(R.string.student_id));
        mCourseTv.setText(mContext.getResources().getString(R.string.course_code));

        mStudentNameIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_my_profile));
        mStudentIdIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_student_id));
        mCourseIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.course_icon));
    }
}