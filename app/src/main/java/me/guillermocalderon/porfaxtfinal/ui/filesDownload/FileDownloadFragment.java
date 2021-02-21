package me.guillermocalderon.porfaxtfinal.ui.filesDownload;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import me.guillermocalderon.porfaxtfinal.R;

public class FileDownloadFragment extends Fragment {

    FirebaseFirestore db;
    RecyclerView mRecyclerView;
    ArrayList<FileDownloadViewModel> fileDownloadList = new ArrayList<>();
    FileDownloadAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_filedownload, container, false);

        mRecyclerView = root.findViewById(R.id.mRecycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dataFromFirebase();

        return root;
    }

    /*
      Metodo que permite obtener los datos de Firebase y mostrarlos en un recyclerView
    */
    public void dataFromFirebase() {
        if (fileDownloadList.size() > 0) {
            fileDownloadList.clear();
        }
        db = FirebaseFirestore.getInstance();
        
        db.collection("files")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot documentModel : task.getResult()) {
                            FileDownloadViewModel model = new FileDownloadViewModel(
                                    documentModel.getString("fileName"),
                                    documentModel.getString("downloadLink"));
                            fileDownloadList.add(model);
                        }
                        mAdapter = new FileDownloadAdapter(FileDownloadFragment.this,
                                fileDownloadList);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "ERROR OBTENIENDO LOS DATOS DE FIREBASE",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
