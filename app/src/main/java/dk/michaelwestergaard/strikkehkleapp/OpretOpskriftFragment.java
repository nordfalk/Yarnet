package dk.michaelwestergaard.strikkehkleapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class OpretOpskriftFragment extends Fragment implements View.OnClickListener {
    Button opret;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_opretopskrift, container, false);

        opret = (Button) ;
    }

    @Override
    public void onClick(View v) {

    }
}
