package me.guillermocalderon.porfaxtfinal.ui.xml;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import me.guillermocalderon.porfaxtfinal.R;

public class XMLFragment extends Fragment {

    private XMLViewModel XMLViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        XMLViewModel =
                ViewModelProviders.of(this).get(XMLViewModel.class);
        View root = inflater.inflate(R.layout.fragment_xml, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        XMLViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}