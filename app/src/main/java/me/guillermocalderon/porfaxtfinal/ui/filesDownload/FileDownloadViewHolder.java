package me.guillermocalderon.porfaxtfinal.ui.filesDownload;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import me.guillermocalderon.porfaxtfinal.R;

public class FileDownloadViewHolder extends RecyclerView.ViewHolder {

    TextView fileName;
    TextView fileLink;
    ImageButton btnDownload;
    ImageButton btnDelete;

    public FileDownloadViewHolder(@NonNull View itemView) {
        super(itemView);

        fileName = itemView.findViewById(R.id.textViewFileName);
        fileLink = itemView.findViewById(R.id.textViewFileLink);
        btnDownload = itemView.findViewById(R.id.btnDownload);
        btnDelete = itemView.findViewById(R.id.btnDelete);
    }
}
