package com.collegeproject.mysocialmedia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.collegeproject.mysocialmedia.Beans.FileDBEntry;
import com.collegeproject.mysocialmedia.R;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * @author Tomin Bijaimon Azhakathu
 * @location Dublin, Ireland
 * @date 25 August 2019, 17:26 Irish Standard Time
 **/
public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.MyViewHolder> {
    private String userId;
    private List<FileDBEntry> files;
    private Context context;
    private static int MY_FILE = 0;
    private static int OTHERS_FILE = 1;
    private SimpleDateFormat dateFormat;

    HomeRecyclerAdapter(Context context, String userId, List<FileDBEntry> files) {
        this.userId = userId;
        this.files = files;
        this.context = context;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss", Locale.ENGLISH);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MY_FILE) {
            View view = LayoutInflater.from(context).inflate(R.layout.my_file_view, parent, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.others_file_view, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.fileNameText.setText(files.get(position).getFileName());
        holder.fileDescriptionText.setText(files.get(position).getFileDescription());
        holder.dateText.setText(dateFormat.format(files.get(position).getDateOfAddition()));
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (files.get(position).getUserID().equals(userId)) {
            return MY_FILE;
        } else {
            return OTHERS_FILE;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fileNameText, fileDescriptionText, dateText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameText = itemView.findViewById(R.id.text_file_name);
            fileDescriptionText = itemView.findViewById(R.id.text_file_description);
            dateText = itemView.findViewById(R.id.text_file_date);
        }
    }
}
