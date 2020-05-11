package com.example.kshitijjaju.inclass08;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class EditExpenseFragment extends Fragment {
    RecyclerView rv_results;
    EditText et_name, et_amount;
    Spinner sp_category;
    Button btn_save, btn_cancel;
    String[] categoryArray;
    String category = null;
    ArrayList<ExpenseData> expenseDataArrayList = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("expenseDataArrayList");
    int position;
    ExpenseData expenseData = new ExpenseData();

    private EditExpenseFragment.OnFragmentInteractionListener mListener;

    public EditExpenseFragment() {
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
        View view =inflater.inflate(R.layout.fragment_edit_expense, container, false);
        et_name = view.findViewById(R.id.et_name);
        et_amount = view.findViewById(R.id.et_amount);
        sp_category = view.findViewById(R.id.sp_category);
        btn_save = view.findViewById(R.id.btn_save);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        categoryArray = new String[]{"Groceries", "Invoice", "Transportation", "Shopping", "Rent", "Trips", "Utilities", "Other"};

        expenseData = new ExpenseData(Parcel.obtain());
        if (getArguments() != null) {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                // Log.d("expenseData",""+expenseData.name);
                expenseData = bundle.getParcelable("ExpenseData");
                Log.d("expenseData33",expenseData.toString());
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, categoryArray);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_category.setAdapter(dataAdapter);
                et_name.setText(expenseData.getName());
                et_amount.setText(expenseData.getAmount());
                int spinnerPosition = dataAdapter.getPosition(expenseData.category);
                sp_category.setSelection(spinnerPosition);
            }
        }


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_name.getText().length()!=0 && et_amount.getText().length()!=0) {
                    category= String.valueOf(sp_category.getSelectedItem());
                    expenseData.setName(et_name.getText().toString());
                    expenseData.setAmount(et_amount.getText().toString());
                    expenseData.setCategory(category);
                    Date today = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                    String date = formatter.format(today);
                    expenseData.date = date;
                    mListener.onEditSaveClick(expenseData);
                }else{
                    Toast.makeText(getActivity(), getResources().getString(R.string.expense_error), Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onEditCancelClick();
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
        if (context instanceof AddExpenseFragment.OnFragmentInteractionListener) {
            mListener = (EditExpenseFragment.OnFragmentInteractionListener) context;
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
        void onEditCancelClick();
        void onEditSaveClick(ExpenseData expenseData);
    }
}
