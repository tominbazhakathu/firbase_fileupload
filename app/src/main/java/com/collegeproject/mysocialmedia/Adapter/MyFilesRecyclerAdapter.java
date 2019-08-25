package com.collegeproject.mysocialmedia.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.collegeproject.mysocialmedia.Beans.FileDBEntry;
import com.collegeproject.mysocialmedia.Fragments.myfiles.MyFilesFragment;
import com.collegeproject.mysocialmedia.Interface.HomeFileClickInterface;
import com.collegeproject.mysocialmedia.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * @author Tomin Bijaimon Azhakathu
 * @location Dublin, Ireland
 * @date 25 August 2019, 17:26 Irish Standard Time
 **/
public class MyFilesRecyclerAdapter extends RecyclerView.Adapter<MyFilesRecyclerAdapter.MyViewHolder> {
    private String userId;
    private List<FileDBEntry> files;
    private MyFilesFragment myFilesFragment;
    private HomeFileClickInterface fileClickInterface;
    private static int MY_FILE = 0;
    private static int OTHERS_FILE = 1;
    private SimpleDateFormat dateFormat;

    public MyFilesRecyclerAdapter(MyFilesFragment myFilesFragment, String userId, List<FileDBEntry> files) {
        this.userId = userId;
        this.files = files;
        this.myFilesFragment = myFilesFragment;
        this.fileClickInterface = myFilesFragment;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss", Locale.ENGLISH);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myFilesFragment.getContext()).inflate(R.layout.my_file_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.fileNameText.setText(files.get(position).getFileName());
        holder.fileDescriptionText.setText(files.get(position).getFileDescription());
        holder.dateText.setText(dateFormat.format(files.get(position).getDateOfAddition()));
        holder.textUserSent.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fileNameText, fileDescriptionText, dateText, textUserSent;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameText = itemView.findViewById(R.id.text_file_name);
            fileDescriptionText = itemView.findViewById(R.id.text_file_description);
            dateText = itemView.findViewById(R.id.text_file_date);
            textUserSent = itemView.findViewById(R.id.text_user_sent);

            itemView.setOnClickListener(v -> fileClickInterface.fileClickedForView(files.get(getAdapterPosition())));
        }
    }
}
