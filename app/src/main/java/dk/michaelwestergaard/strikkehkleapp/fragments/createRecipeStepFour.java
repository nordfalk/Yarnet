package dk.michaelwestergaard.strikkehkleapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import dk.michaelwestergaard.strikkehkleapp.R;

import static android.app.Activity.RESULT_OK;

public class createRecipeStepFour extends Fragment implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE1 = 1;
    private static final int RESULT_LOAD_IMAGE2 = 1;
    private static final int RESULT_LOAD_IMAGE3 = 1;
    private static final int RESULT_LOAD_IMAGE4 = 1;

    ImageView   pic1, pic2, pic3, pic4;
    Button      add1, add2, add3, add4;
    Button      del1, del2, del3, del4;
    Uri         sel1, sel2, sel3, sel4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_recipe_step_four, container, false);

        add1 = view.findViewById(R.id.add1); add2 = view.findViewById(R.id.add2);
        add3 = view.findViewById(R.id.add3); add4 = view.findViewById(R.id.add4);

        pic1 = view.findViewById(R.id.pic1); pic2 = view.findViewById(R.id.pic2);
        pic3 = view.findViewById(R.id.pic3); pic4 = view.findViewById(R.id.pic4);

        del1 = view.findViewById(R.id.del1); del2 = view.findViewById(R.id.del2);
        del3 = view.findViewById(R.id.del3); del4 = view.findViewById(R.id.del4);

        add2.setVisibility(View.GONE); add3.setVisibility(View.GONE);
        add4.setVisibility(View.GONE);

        pic1.setVisibility(View.GONE); pic2.setVisibility(View.GONE);
        pic3.setVisibility(View.GONE); pic4.setVisibility(View.GONE);

        del1.setVisibility(View.GONE); del2.setVisibility(View.GONE);
        del3.setVisibility(View.GONE); del4.setVisibility(View.GONE);

        add1.setOnClickListener(this); add2.setOnClickListener(this);
        add3.setOnClickListener(this); add4.setOnClickListener(this);

        del1.setOnClickListener(this); del2.setOnClickListener(this);
        del3.setOnClickListener(this); del4.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.add1:
                Intent galleryIntet1 = new Intent (Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntet1, RESULT_LOAD_IMAGE1);
                break;
        }

        switch (view.getId()) {
            case R.id.add2:
                Intent galleryIntet2 = new Intent (Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntet2, RESULT_LOAD_IMAGE2);
                break;
        }

        switch (view.getId()) {
            case R.id.add3:
                Intent galleryIntet3 = new Intent (Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntet3, RESULT_LOAD_IMAGE3);
                break;
        }

        switch (view.getId()) {
            case R.id.add4:
                Intent galleryIntet4 = new Intent (Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntet4, RESULT_LOAD_IMAGE4);
                break;
        }

        switch (view.getId()) {
            case R.id.del1:
                pic1.setImageURI(null);
                break;
        }

        switch (view.getId()) {
            case R.id.del2:
                pic2.setImageURI(null);
                break;
        }

        switch (view.getId()) {
            case R.id.del3:
                pic3.setImageURI(null);
                break;
        }

        switch (view.getId()) {
            case R.id.del4:
                pic4.setImageURI(null);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE1 && resultCode == RESULT_OK && data != null) {

            add1.setVisibility(View.GONE);
            pic1.setVisibility(View.VISIBLE);

            sel1 = data.getData();
            pic1.setImageURI(sel1);

            del1.setVisibility(View.VISIBLE);
            add2.setVisibility(View.VISIBLE);

        } else if (requestCode == RESULT_LOAD_IMAGE2 && resultCode == RESULT_OK && data != null) {

            add2.setVisibility(View.GONE);
            pic2.setVisibility(View.VISIBLE);

            sel2 = data.getData();
            pic2.setImageURI(sel2);

            del2.setVisibility(View.VISIBLE);
            add3.setVisibility(View.VISIBLE);

        } else if (requestCode == RESULT_LOAD_IMAGE3 && resultCode == RESULT_OK && data != null) {

            add3.setVisibility(View.GONE);
            pic3.setVisibility(View.VISIBLE);

            sel3 = data.getData();
            pic3.setImageURI(sel3);

            del3.setVisibility(View.VISIBLE);
            add4.setVisibility(View.VISIBLE);

        } else if (requestCode == RESULT_LOAD_IMAGE4 && resultCode == RESULT_OK && data != null) {

            add4.setVisibility(View.GONE);
            pic4.setVisibility(View.VISIBLE);

            sel4 = data.getData();
            pic4.setImageURI(sel4);

            del4.setVisibility(View.VISIBLE);
        }
    }
}
