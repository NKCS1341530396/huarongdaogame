package com.example.huarongdao;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class Mission_List extends RecyclerView.Adapter<Mission_List.WordViewHolder> {
    private LayoutInflater mInflater;
    private Context context;
    public static final String Mission_choice = "com.example.huarongdao.Mission_choice";
    public Mission_List(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.mission,
                parent, false);
        return new WordViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        if(Mission.Mission_finish[position] == 1)holder.mission_name.setTextColor(Color.GRAY);
        else holder.mission_name.setTextColor(Color.BLACK);
        holder.mission_name.setText(Mission.Mission_name[position]);
        SharedPreferences read = context.getSharedPreferences("map", MODE_PRIVATE);
        int score = read.getInt("Mission"+position+"score",500);
        holder.mission_score.setTextColor(Color.BLACK);
        holder.mission_score.setText("分数: "+score+"");
    }

    @Override
    public int getItemCount() {
        return Mission.Mission_name.length;
    }

    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mission_name;
        public final TextView mission_score;
        final Mission_List mAdapter;
        public WordViewHolder(@NonNull View itemView, Mission_List adapter) {
            super(itemView);
            mission_name = itemView.findViewById(R.id.mission_name);
            mission_score = itemView.findViewById(R.id.mission_score);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int mPosition = getLayoutPosition();
            Intent intent = new Intent(context,MainActivity.class);
            intent.putExtra(Mission_choice,mPosition);
            context.startActivity(intent);
        }
    }
}
