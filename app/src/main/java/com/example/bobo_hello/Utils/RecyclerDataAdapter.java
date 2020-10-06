package com.example.bobo_hello.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bobo_hello.R;

import java.util.ArrayList;

public class RecyclerDataAdapter extends RecyclerView.Adapter<RecyclerDataAdapter.ViewHolder> {
    private ArrayList<String> data;
    private IRVOnItemClick onItemClickCallback;

    public RecyclerDataAdapter(ArrayList<String> data, IRVOnItemClick onItemClickCallback) {
        this.data = data;
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setTextToTextView(data.get(position));
        holder.setOnClickForItem(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.cityItemTextView);
        }

        void setTextToTextView(String text) {
            textView.setText(text);
        }

        void setOnClickForItem(final String text) {
            textView.setOnClickListener(view -> {
                if(onItemClickCallback != null) {
                    onItemClickCallback.onItemClicked(text);
                }
            });
        }
    }
}
