package com.projects.questmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<String> list;
    private Context context;
//    private LayoutInflater inflater;

    public MyAdapter(Context context, List<String> partyNameList) {
        this.context = context;
        this.list = partyNameList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        holder.partyName.setText(list.get(position));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;

        TextView partyName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout=itemView.findViewById(R.id.layout);
            linearLayout.setBackgroundResource(R.drawable.abc);
            partyName = itemView.findViewById(R.id.partyName);


        }
    }
}
