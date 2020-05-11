package com.example.kshitijjaju.inclass08;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ShowExpenseFragment extends Fragment {

    TextView tv_sh_name, tv_category, tv_amount, tv_date;
    Button btn_close, btn_edit;
    ExpenseData expenseData;
    ArrayList<ExpenseData> expenseDataArrayList = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("expenseDataArrayList");
    int position;

    private OnFragmentInteractionListener mListener;

    public ShowExpenseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_show_expense, container, false);

        tv_sh_name = view.findViewById(R.id.tv_sh_name);
        tv_category = view.findViewById(R.id.tv_category);
        tv_amount = view.findViewById(R.id.tv_amount);
        tv_date = view.findViewById(R.id.tv_date);
        btn_close = view.findViewById(R.id.btn_close);
        btn_edit = view.findViewById(R.id.btn_edit);

        expenseData = new ExpenseData(Parcel.obtain());
        if (getArguments() != null) {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                // Log.d("expenseData",""+expenseData.name);
                expenseData = bundle.getParcelable("ExpenseData");
                expenseDataResult(expenseData);
            }
        }

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("expenseData11",expenseData.toString());
                mListener.onEditButtonClick(expenseData);

            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCloseButtonClick();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void expenseDataResult (ExpenseData expenseData){

        tv_sh_name.setText(expenseData.name);
        tv_amount.setText(expenseData.amount);
        tv_category.setText(expenseData.category);
        tv_date.setText(expenseData.date);
    }

    public interface OnFragmentInteractionListener {
        void onCloseButtonClick();
        void onEditButtonClick(ExpenseData expenseData);
    }
}
