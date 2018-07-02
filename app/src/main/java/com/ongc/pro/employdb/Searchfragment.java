package com.ongc.pro.employdb;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by Mukul on 6/20/2017.
 */

public class Searchfragment extends android.support.v4.app.Fragment {


    private Button b1;
    private EditText e1;
    private RadioGroup r1;
    public  int search_type;
    public String qry;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.search, container, false);



        return v;

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {


        b1 = (Button)view.findViewById(R.id.search_button);
        e1 = (EditText)view.findViewById(R.id.input);
        r1 = (RadioGroup)view.findViewById(R.id.G_1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                qry = e1.getText().toString();
                if (qry.matches("")) {
                    Toast.makeText(getActivity(), "Feild is Empty", Toast.LENGTH_LONG).show();
                }

                else
                {
                    // get selected radio button from radioGroup
                    search_type = r1.getCheckedRadioButtonId();
                    Resultfragment rf = new Resultfragment();



                        //for search by name

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Resultfragment newDialog = Resultfragment.newInstance();
                        Bundle bundle = new Bundle();
                        bundle.putInt("search_type",search_type);
                        bundle.putString("qry",qry);
                        newDialog.setArguments(bundle);
                        newDialog.show(ft,"search result");



                }

            }
        });


    }

}


