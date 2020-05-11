package com.example.kshitijjaju.inclass08;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ExpenseAppFragment extends Fragment {

    ListView rv_results;
    TextView tv_no_list;
    Button btn_add;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter mAdapter;
    ArrayList<ExpenseData> expenseDataArrayList = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("expenseDataArrayList");
    Map<String, ExpenseData> firebaseHashMap = new HashMap<>();

    private OnFragmentInteractionListener mListener;

    public ExpenseAppFragment() {
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
        View view =inflater.inflate(R.layout.fragment_expense_app, container, false);

        btn_add = view.findViewById(R.id.btn_add);
        rv_results = view.findViewById(R.id.rv_results);
        tv_no_list = view.findViewById(R.id.tv_no_list);
        tv_no_list.setVisibility(View.VISIBLE);
        rv_results.setVisibility(View.INVISIBLE);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                expenseDataArrayList.clear();
                firebaseHashMap.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    expenseDataArrayList.add( dataSnapshot1.getValue(ExpenseData.class));
                }
                if(arrayListSizeCheck(expenseDataArrayList)) {
                    expenseListResult(expenseDataArrayList);
                }
                //Log.d("firebase_frag_expense:", "expenseDataArrayList is: " + expenseDataArrayList.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("firebase:", "Failed to read value.", error.toException());
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.addButtonClick(true);
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

    public boolean arrayListSizeCheck(ArrayList<ExpenseData> expenseDataArrayList){

        if(null!=expenseDataArrayList && expenseDataArrayList.size()>0) {

            tv_no_list.setVisibility(View.INVISIBLE);
            rv_results.setVisibility(View.VISIBLE);
            return true;
        }else {
            tv_no_list.setVisibility(View.VISIBLE);
            rv_results.setVisibility(View.INVISIBLE);
            return false;
        }
    }

    public void expenseListResult (final ArrayList<ExpenseData> expenseDataArrayList){

        final ExpenseAdapter expenseAdapter = new ExpenseAdapter(getContext(),R.layout.row_item,expenseDataArrayList);
        rv_results.setAdapter(expenseAdapter);
        expenseAdapter.setNotifyOnChange(true);

        rv_results.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ExpenseData expenseData= new ExpenseData();
                expenseData = expenseAdapter.getItem(position);
                mListener.itemLongClick(expenseData, expenseData.getFirbaseKey());
                expenseDataArrayList.remove(expenseData);
                expenseAdapter.notifyDataSetChanged();
                arrayListSizeCheck(expenseDataArrayList);

                //Log.d("expenseDataArrayList3:",""+expenseDataArrayList.toString());
                Toast.makeText(getContext(),getResources().getString(R.string.expense_deleted),Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        rv_results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExpenseData expenseData=new ExpenseData();
                expenseData= expenseAdapter.getItem(position);
                mListener.itemClick(expenseData);
            }
        });

        this.expenseDataArrayList = expenseDataArrayList;

    }

    public interface OnFragmentInteractionListener {
        void addButtonClick(boolean clicked);
        void itemLongClick(ExpenseData expenseData, String firebaseKey);
        void itemClick(ExpenseData expenseData);
    }
}
