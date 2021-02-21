package me.guillermocalderon.porfaxtfinal.ui.filesDownload;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import me.guillermocalderon.porfaxtfinal.R;

public class FileDownloadAdapter extends RecyclerView.Adapter<FileDownloadViewHolder> {

    FileDownloadFragment fileDownloadFragment;
    ArrayList<FileDownloadViewModel> fileDownloadViewModels;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FileDownloadAdapter(FileDownloadFragment fileDownloadFragment, ArrayList<FileDownloadViewModel> fileDownloadViewModels) {
        this.fileDownloadFragment = fileDownloadFragment;
        this.fileDownloadViewModels = fileDownloadViewModels;
    }

    @NonNull
    @Override
    public FileDownloadViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(fileDownloadFragment.getContext());
        View view = layoutInflater.inflate(R.layout.item, null, false);

        return new FileDownloadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FileDownloadViewHolder fileDownloadViewHolder, final int i) {
        fileDownloadViewHolder.fileName.setText(fileDownloadViewModels.get(i).getFichName());
        fileDownloadViewHolder.fileLink.setText(fileDownloadViewModels.get(i).getFichLink());
        fileDownloadViewHolder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadFile(fileDownloadViewHolder.fileName.getContext(), fileDownloadViewModels.get(i).getFichName(),
                        Environment.DIRECTORY_DOWNLOADS, fileDownloadViewModels.get(i).getFichLink());

            }
        });
        fileDownloadViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("files").document(fileDownloadViewModels.get(i).getFichName())
                        .delete();
                notifyItemRemoved(i);
            }
        });
    }

    /*
      Metodo que permite iniciar la descarga del fichero
    */
    public void downloadFile(Context context, String fileName, String destinationDirectory, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName);

        downloadManager.enqueue(request);
    }

    @Override
    public int getItemCount() {
        return fileDownloadViewModels.size();
    }
}
