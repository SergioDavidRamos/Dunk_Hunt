package com.example.duckhunt.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.duckhunt.R;
import com.example.duckhunt.models.User;

import java.util.List;


public class MyUserRecyclerViewAdapter extends RecyclerView.Adapter<MyUserRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValues;

    public MyUserRecyclerViewAdapter(List<User> items) {
        mValues = items;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        int pos = position+1;
        holder.tvPosotion.setText(pos + "°");
        holder.tvDucks.setText(String.valueOf(mValues.get(position).getDucks()));
        holder.tvNick.setText(mValues.get(position).getNick());

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvPosotion;
        public final TextView tvDucks;
        public final TextView tvNick;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvPosotion = view.findViewById(R.id.textViewPosotion);
            tvDucks = view.findViewById(R.id.textViewDuckscheck);
            tvNick= view.findViewById(R.id.textViewNick);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvNick.getText() + "'";
        }
    }
}
