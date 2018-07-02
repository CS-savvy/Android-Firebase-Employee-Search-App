package com.ongc.pro.employdb;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class Resultfragment extends DialogFragment
{    private TextView no_result;
     private RecyclerView mResult_list;
     private FirebaseAuth mAuth;
     private DatabaseReference mDatabase;
     private DatabaseReference uDatabase;
     private Query mQuery;
    private String user_id;
     private ProgressDialog mProgress;

    private boolean mFavhit = false;
      static Resultfragment newInstance()
    {
        Resultfragment r = new Resultfragment();
        return r;
    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.result , container ,false);
        return v;

    }
    public void onStart()
    {
        super.onStart();
        mProgress.show();
        FirebaseRecyclerAdapter<Emp,ResultViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Emp, ResultViewHolder>(
                Emp.class,
                R.layout.result_row,
                ResultViewHolder.class,
                mQuery
        ) {



            @Override
            protected void populateViewHolder(ResultViewHolder viewHolder, Emp model, int position) {
                final String contact_key = getRef(position).getKey();
                final String contact_no = model.getContact_no();
                final String name = model.getName();
                final String age = model.getAge();
                final String cif = model.getCif();
                final String designation = model.getDesignation();
                final String pic = model.getPic();
                viewHolder.setName(model.getName());
                viewHolder.setCif(model.getCif());
                viewHolder.setAge(model.getAge());
                viewHolder.setDesignation(model.getDesignation());
                viewHolder.setPic(getContext(),model.getPic());
                viewHolder.setFavbutton(contact_key);
                viewHolder.setCallbutton(model.getContact_no());
                viewHolder.mCallbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + contact_no));
                        startActivity(intent);
                    }
                });
                viewHolder.mFavbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       mFavhit = true;
                        mDatabase.child(contact_key).child("LikedBy").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (mFavhit)
                                {
                                    if(dataSnapshot.hasChild(user_id))
                                    {
                                        uDatabase.child(user_id).child("LikedPost").child(contact_key).removeValue();
                                        mDatabase.child(contact_key).child("LikedBy").child(user_id).removeValue();
                                        mFavhit = false;
                                    }
                                    else
                                    {
                                        uDatabase.child(user_id).child("LikedPost").child(contact_key).child("name").setValue(name);
                                        uDatabase.child(user_id).child("LikedPost").child(contact_key).child("age").setValue(age);
                                        uDatabase.child(user_id).child("LikedPost").child(contact_key).child("cif").setValue(cif);
                                        uDatabase.child(user_id).child("LikedPost").child(contact_key).child("pic").setValue(pic);
                                        uDatabase.child(user_id).child("LikedPost").child(contact_key).child("designation").setValue(designation);
                                        uDatabase.child(user_id).child("LikedPost").child(contact_key).child("contact_no").setValue(contact_no);
                                        mDatabase.child(contact_key).child("LikedBy").child(user_id).setValue("liked");
                                        mFavhit = false;
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                });
                mProgress.dismiss();

            }



            @Override
            public int getItemCount() {
                return super.getItemCount();

            }




        };

        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.hasChildren()))
                {
                    no_result.setVisibility(View.VISIBLE);
                    mProgress.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mResult_list.setAdapter(firebaseRecyclerAdapter);

        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    public void onViewCreated(View view ,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view ,savedInstanceState);

        getDialog().setTitle("Search Results");
        Bundle bundle = this.getArguments();
        int search_type = bundle.getInt("search_type");
        String qry = bundle.getString("qry");
        mProgress = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Whole_Data");
        uDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mProgress.setMessage("Wait While Searching");
        if(search_type == R.id.name_search)
        {
            mQuery = mDatabase.orderByChild("name").equalTo(qry);
        }

        else
        {
            mQuery = mDatabase.orderByChild("cif").equalTo(qry);
        }
        no_result = (TextView)view.findViewById(R.id.no_result);
        mResult_list = (RecyclerView)view.findViewById(R.id.result_list);
        mResult_list.setHasFixedSize(true);

        LinearLayoutManager l1 = new LinearLayoutManager(getActivity());
        mResult_list.setLayoutManager(l1);



    }



    public static class ResultViewHolder extends RecyclerView.ViewHolder
    {
         View mView;
        Button mFavbutton;
        Button mCallbutton;
        FirebaseAuth mAuth;
        DatabaseReference mDatabase;
        String user_id;
        public ResultViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mCallbutton = (Button)mView.findViewById(R.id.bcall);
            mFavbutton = (Button)mView.findViewById(R.id.bfav);
            mAuth = FirebaseAuth.getInstance();
            user_id = mAuth.getCurrentUser().getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Whole_Data");
            mDatabase.keepSynced(true);

        }
        public void setCallbutton(final String contact_no)
        {
            mCallbutton.setText(contact_no);
        }

       public void setFavbutton(final String contact_key)
        {
              mDatabase.child(contact_key).child("LikedBy").addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {

                      if(dataSnapshot.hasChild(user_id))
                      {
                          mFavbutton.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_favorite_black_24dp,0,0,0);
                      }
                      else
                      {
                          mFavbutton.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_favorite_border_black_24dp,0,0,0);

                      }

                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {

                  }
              });
        }
        public void setName(String name)
        {
            TextView em_name = (TextView)mView.findViewById(R.id.em_name);
            em_name.setText(name);
        }

        public void setAge(String age)
        {
            TextView em_age = (TextView)mView.findViewById(R.id.em_age);
            em_age.setText(age);
        }

        public void setCif(String cif)
        {
            TextView em_cif = (TextView)mView.findViewById(R.id.em_cif);
            em_cif.setText(cif);
        }

        public void setDesignation(String designation)
        {
            TextView em_designation = (TextView)mView.findViewById(R.id.em_designation);
            em_designation.setText(designation);
        }

        public void setPic(Context ctx , String pic )
        {
            ImageView mpic =(ImageView)mView.findViewById(R.id.imageView);
            Picasso.with(ctx).load(pic).into(mpic);
        }
    }

}
