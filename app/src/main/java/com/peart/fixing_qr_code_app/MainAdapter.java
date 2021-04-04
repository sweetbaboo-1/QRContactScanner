package com.peart.fixing_qr_code_app;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


/**
 * The MainAdapter helps with displaying the contacts in the Contacts activity
 **/

//Developed by Denzil Museruka
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    Activity activity;
    ArrayList<ContactModel> arrayList;

    public MainAdapter(Activity activity, ArrayList<ContactModel> arrayList){
        this.activity = activity;
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // initialize view
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact,parent,false);

        //return view
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Initialize contact model
        ContactModel model = arrayList.get(position);

        holder.tvNumber.setText(model.getNumber());
        holder.tvName.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvNumber = itemView.findViewById(R.id.tv_number);

        }
    }
}

