package com.example.kshitijjaju.inclass08;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class AddExpenseFragment extends Fragment {

    RecyclerView rv_results;
    EditText et_name, et_amount;
    Spinner sp_category;
    Button btn_add_exp, btn_cancel;
    String[] categoryArray;
    String category = null;

    private OnFragmentInteractionListener mListener;

    public AddExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_add_expense, container, false);
        et_name = view.findViewById(R.id.et_name);
        et_amount = view.findViewById(R.id.et_amount);
        sp_category = view.findViewById(R.id.sp_category);
        btn_add_exp = view.findViewById(R.id.btn_add_exp);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        categoryArray = new String[]{"Groceries", "Invoice", "Transportation", "Shopping", "Rent", "Trips", "Utilities", "Other"};

        btn_add_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_name.getText().length()!=0 && et_amount.getText().length()!=0) {
                    category= String.valueOf(sp_category.getSelectedItem());
                    mListener.onAddExpClick(et_name.getText().toString(),"$ "+et_amount.getText().toString(), category);
                   // Log.d("expenseDataArrayList_addfragment",""+et_name.getText().toString()+ et_amount.getText().toString()+ category);
                }else{
                    Toast.makeText(getActivity(), getResources().getString(R.string.expense_error), Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelClick();
            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
        android.R.layout.simple_spinner_item, categoryArray);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_category.setAdapter(dataAdapter);

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

    public interface OnFragmentInteractionListener {

        void onAddExpClick(String name, String amount, String category);
        void onCancelClick();
    }
}
