package me.guillermocalderon.porfaxtfinal.ui.zip;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import me.guillermocalderon.porfaxtfinal.R;
import me.guillermocalderon.porfaxtfinal.databinding.FragmentZipBinding;

public class ZIPFragment extends Fragment {

    private ZIPViewModel ZIPViewModel;
    private final int OPEN_CODE = 100;
    private List<String> listaArchivos = new ArrayList<>();
    private String fileDownloadLink;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ZIPViewModel =
                ViewModelProviders.of(this).get(ZIPViewModel.class);
        View root = inflater.inflate(R.layout.fragment_zip, container, false);
        final TextView textView = root.findViewById(R.id.textViewZIP);
        ZIPViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        // Pedimos permisos al usuario para grabar
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getActivity(), "Sin permisos", Toast.LENGTH_SHORT).show();
            Log.i("Mensaje", "No se tiene permiso para leer.");
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else{
            Toast.makeText(getActivity(), "Permisos recibidos", Toast.LENGTH_SHORT).show();
            Log.i("Mensaje", "Se tiene permiso para leer!");
        }

        Button btnAgregaFich = root.findViewById(R.id.btnAgregaFich);
        btnAgregaFich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("*/*");
                startActivityForResult(intent, OPEN_CODE);
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OPEN_CODE) {
            if (data != null) {
                if (data.getClipData() != null){
                    for (int index = 0; index < data.getClipData().getItemCount(); index++) {
                        String uri = data.getClipData().getItemAt(index).getUri().getPath();
                        if (uri.contains("/document/raw:")) {
                            uri = uri.replaceAll("/document/raw:", "");
                            listaArchivos.add(uri);
                        }else {
                            listaArchivos.add(uri);
                        }
                    }
                    try {
                        multipleFilesCompressZIP(listaArchivos);
                        Toast.makeText(getActivity(), "Archivos comprimidos con exito!",
                                Toast.LENGTH_SHORT).show();
                        uploadFilesToStorage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    String uri = data.getData().getPath();
                    if (uri.contains("/document/raw:")) {
                        uri = uri.replaceAll("/document/raw:", "");
                    }

                    try {
                        singleFileCompressZIP(uri);
                        Toast.makeText(getActivity(), "Archivo comprimido con exito!", Toast.LENGTH_SHORT).show();
                        uploadFilesToStorage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /*
        Metodo que nos permite comprimir el unico archivo seleccionado
    */
    public void singleFileCompressZIP(String filePath) throws IOException {
        int rand = (int) ((Math.random()*10000)+1);
        String zipName = "compressed"+rand+".zip";
        FileOutputStream fos = getContext().openFileOutput(zipName, Context.MODE_PRIVATE);
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        File fileToZip = new File(filePath);
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        zipOut.close();
        fis.close();
        fos.close();
    }

    /*
        Metodo que nos permite comprimir varios archivos seleccionados
     */
    public void multipleFilesCompressZIP(List<String> listaArchivos) throws IOException {
        int rand = (int) ((Math.random()*10000)+1);
        String zipName = "compressed"+rand+".zip";
        FileOutputStream fos = getContext().openFileOutput(zipName, Context.MODE_PRIVATE);
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        for (String srcFile : listaArchivos) {
            File fileToZip = new File(srcFile);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }
        zipOut.close();
        fos.close();
    }

    /*
        Metodo que nos permite subir al Storage los comprimidos, para mostrarlos en un recyclerView y
        que el usuario se los pueda descargar en cualquier momento
     */
    public void uploadFilesToStorage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Obtenemos los archivos zip del directorio donde se guardan al comprimirlos
        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        File dir = getContext().getFilesDir();
        File[] comprimidos = dir.listFiles();
        for (int i = 0; i < comprimidos.length; i++) {
            final StorageReference ficherosRef;
            final String fileName;
            if (comprimidos[i].getName().endsWith(".zip")) {
                Uri uri = Uri.fromFile(comprimidos[i]);
                ficherosRef = storageRef.child(uri.getLastPathSegment());
                fileName = comprimidos[i].getName();
                ficherosRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ficherosRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                uploadToFirestore(fileName, uri.toString());
                            }
                        });
                    }
                });
            }
        }
    }

    public void uploadToFirestore(String fileName, String fileDownloadLink) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Creamos la coleccion
        DocumentReference dR = db.collection("files").document(fileName);
        Map<String, String> data = new HashMap<>();
        data.put("fileName", fileName);
        data.put("downloadLink", fileDownloadLink);
        dR.set(data);
    }
}