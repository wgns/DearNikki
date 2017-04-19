package com.dearnikki.dearnikki.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dearnikki.dearnikki.R;
import com.dearnikki.dearnikki.activities.*;
import com.dearnikki.dearnikki.model.*;
import com.dearnikki.dearnikki.sql.DatabaseHelper;

import java.util.List;

public class EntriesRecyclerAdapter extends RecyclerView.Adapter<EntriesRecyclerAdapter.EntryViewHolder>{

    private List<Entry> listEntries;
    private Context context;
    private DatabaseHelper databaseHelper;

    public EntriesRecyclerAdapter(List<Entry> listEntries, Context context) {
        this.listEntries = listEntries;
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_recycler, parent, false);

        return new EntryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EntryViewHolder holder, final int position) {
        holder.textViewTitle.setText(listEntries.get(position).getTitle());
        holder.textViewDate.setText(listEntries.get(position).getDate());
        holder.textViewOwner.setText(listEntries.get(position).getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(listEntries.get(position).getTitle() + "\n\n" + listEntries.get(position).getContent())
                        .setNegativeButton("Close", null)
                        .create()
                        .show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CharSequence[] options = new CharSequence[] {"Edit", "Delete"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Options");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:     // Edit - HAHAHALOLS delete + add = update      :(
                                if (listEntries.get(position).getEmail().equals(((Activity) context).getIntent().getStringExtra("EMAIL"))) {
                                    Intent intent = new Intent(context, ComposeActivity.class);
                                    intent.putExtra("ENTRY_ID", listEntries.get(position).getId());
                                    intent.putExtra("TITLE", listEntries.get(position).getTitle());
                                    intent.putExtra("CONTENT", listEntries.get(position).getContent());
                                    intent.putExtra("EMAIL", listEntries.get(position).getEmail());

                                    databaseHelper.deleteEntry(listEntries.get(position));
                                    context.startActivity(intent);
                                } else {
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                    builder1.setMessage("You can't edit this entry.")
                                            .setNegativeButton("Okay", null)
                                            .create()
                                            .show();
                                }
                                break;
                            case 1:     // Delete - works fine
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                builder1.setMessage("Are you sure you want to delete this entry?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                databaseHelper.deleteEntry(listEntries.get(position));

                                                Intent intent1 = new Intent(context, EntriesListActivity.class);
                                                intent1.putExtra("EMAIL", ((Activity) context).getIntent().getStringExtra("EMAIL"));

                                                ((Activity) context).finish();
                                                context.startActivity(intent1);
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .create()
                                        .show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", null)
                        .create()
                        .show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.v(EntriesRecyclerAdapter.class.getSimpleName(),"" + listEntries.size());
        return listEntries.size();
    }

    public class EntryViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView textViewTitle;
        public AppCompatTextView textViewDate;
        public AppCompatTextView textViewOwner;

        public EntryViewHolder(View view) {
            super(view);
            textViewTitle = (AppCompatTextView) view.findViewById(R.id.textViewTitle);
            textViewDate = (AppCompatTextView) view.findViewById(R.id.textViewDate);
            textViewOwner = (AppCompatTextView) view.findViewById(R.id.textViewOwner);
        }
    }
}