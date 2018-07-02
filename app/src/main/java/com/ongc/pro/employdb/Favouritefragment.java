package com.ongc.pro.employdb;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by Mukul on 6/20/2017.
 */

public class Favouritefragment extends android.support.v4.app.Fragment {
    private TextView no_fav;
    private RecyclerView mFav_list;
    private FirebaseAuth mAuth;
    private DatabaseReference mbDatabase;
    private DatabaseReference uDatabase;
    private Query mcQuery;
    private String user_id;
    private boolean mFavhit = false;
    private ProgressDialog mProgress;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.favourite, container, false);
        mFav_list = (RecyclerView)v.findViewById(R.id.Fav_list);
        return v;

    }


    public void onStart()
    {
        super.onStart();
        mProgress.show();

        FirebaseRecyclerAdapter<Fav,FavViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Fav,FavViewHolder>(
                Fav.class,
                R.layout.fav_row,
                FavViewHolder.class,
                mcQuery
        ) {



            @Override
            protected void populateViewHolder(FavViewHolder viewHolder, Fav model, int position) {
                final String contact_key = getRef(position).getKey();
                final String contact_no = model.getContact_no();
                final String name = model.getName();
                final String age = model.getAge();
                final String cif = model.getCif();
                final String designation = model.getDesignation();
                final String pic = model.getPic();
                mProgress.dismiss();
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
                        mbDatabase.child(contact_key).child("LikedBy").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (mFavhit)
                                {
                                    if(dataSnapshot.hasChild(user_id))
                                    {
                                        uDatabase.child(user_id).child("LikedPost").child(contact_key).removeValue();
                                        mbDatabase.child(contact_key).child("LikedBy").child(user_id).removeValue();
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
                                        mbDatabase.child(contact_key).child("LikedBy").child(user_id).setValue("liked");
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


            }



            @Override
            public int getItemCount() {
                return super.getItemCount();

            }




        };






        mcQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.hasChildren()))
                {
                    no_fav.setVisibility(View.VISIBLE);
                     mProgress.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                no_fav.setVisibility(View.VISIBLE);
            }
        });




        mFav_list.setAdapter(firebaseRecyclerAdapter);






    }


    public void onViewCreated(View view ,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view ,savedInstanceState);


      mProgress = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mbDatabase = FirebaseDatabase.getInstance().getReference().child("Whole_Data");
        uDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mProgress.setMessage("Wait While Gathering Data");
        mcQuery = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("LikedPost");
        no_fav = (TextView)view.findViewById(R.id.no_fav);
        LinearLayoutManager l2 = new LinearLayoutManager(getActivity());
        mFav_list.setLayoutManager(l2);


    }




    public static class FavViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        Button mFavbutton;
        Button mCallbutton;
        FirebaseAuth mAuth;
        DatabaseReference mbDatabase;
        String user_id;
        public FavViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mCallbutton = (Button)mView.findViewById(R.id.bcall1);
            mFavbutton = (Button)mView.findViewById(R.id.bfav1);
            mAuth = FirebaseAuth.getInstance();
            user_id = mAuth.getCurrentUser().getUid();
            mbDatabase = FirebaseDatabase.getInstance().getReference().child("Whole_Data");

        }

        public void setCallbutton(final String contact_no)
        {
            mCallbutton.setText(contact_no);
        }

        public void setFavbutton(final String contact_key)
        {
            mbDatabase.child(contact_key).child("LikedBy").addValueEventListener(new ValueEventListener() {
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
            TextView em_name = (TextView)mView.findViewById(R.id.em_name1);
            em_name.setText(name);
        }

        public void setAge(String age)
        {
            TextView em_age = (TextView)mView.findViewById(R.id.em_age1);
            em_age.setText(age);
        }

        public void setCif(String cif)
        {
            TextView em_cif = (TextView)mView.findViewById(R.id.em_cif1);
            em_cif.setText(cif);
        }

        public void setDesignation(String designation)
        {
            TextView em_designation = (TextView)mView.findViewById(R.id.em_designation1);
            em_designation.setText(designation);
        }

        public void setPic(Context ctx , String pic )
        {
            ImageView mpic =(ImageView)mView.findViewById(R.id.imageView1);
            Picasso.with(ctx).load(pic).into(mpic);
        }
    }



}