package com.example.kshitijjaju.inclass07_group02;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class ShowExpenseFragment extends Fragment {

    TextView tv_sh_name, tv_category, tv_amount, tv_date;
    Button btn_close;
    ExpenseData expenseData;

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

        expenseData = new ExpenseData(Parcel.obtain());
        if (getArguments() != null) {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
               // Log.d("expenseData",""+expenseData.name);
                expenseData = bundle.getParcelable("ExpenseData");
                expenseDataResult(expenseData);
            }

        }

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
    }
}
