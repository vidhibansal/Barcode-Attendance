package com.example.acadly;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    RecyclerView recyclerView;
    Context context;



    ArrayList<String> urls = new ArrayList<>();



    ArrayList<String> items = new ArrayList<>();
    public MyAdapter(RecyclerView recyclerView, Context context, ArrayList<String> urls, ArrayList<String> items) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.urls = urls;
        this.items = items;
    }

    public void update(String filename,String url)
    {
        items.add(filename);
        urls.add(url);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.nameOfFile.setText(items.get(i));


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameOfFile;
        public ViewHolder(View itemView){
            super(itemView);
            nameOfFile = itemView.findViewById(R.id.nameOfFile);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = recyclerView.getChildLayoutPosition(v);
                    //Toast.makeText(getApplicationContext(),"You have clicked!!", Toast.LENGTH_LONG).show();
                    Toast.makeText(context,"After click on downskvkjsdnvjsvbjsdjvnsload",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                   // intent.setType("application/pdf");
                    String ur = urls.get(position);
                    //intent.setData(Uri.parse(urls.get(position)));
                    intent.setData(Uri.parse(ur));
                    context.startActivity(intent);

                }
            });

        }

    }
}
