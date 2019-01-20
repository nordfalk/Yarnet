package dk.michaelwestergaard.strikkehkleapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeDTO;
import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeImagesDTO;
import dk.michaelwestergaard.strikkehkleapp.R;
import dk.michaelwestergaard.strikkehkleapp.activities.SignUpActivity;

import static android.app.Activity.RESULT_OK;

public class createRecipeStepFour extends Fragment implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE = 4;
    LayoutInflater inflater;
    LinearLayout picContainer;
    Uri uri;
    ImageView pic;
    Button removeBtn, addPic;
    View newView, list;
    List<Uri> imageList;
    private StorageReference storageReference;
    private FirebaseStorage storage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_recipe_step_four, container, false);

        addPic          = view.findViewById(R.id.addPic);
        picContainer    = view.findViewById(R.id.picContainer);
        list            = inflater.inflate(R.layout.recipe_add_pic, null);
        removeBtn       = list.findViewById(R.id.create_recipe_instruction_delete_btn);
        pic             = list.findViewById(R.id.pic4);


        removeBtn   .setOnClickListener(this);
        addPic      .setOnClickListener(this);

        this.inflater = inflater;

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        return view;
    }

    @Override
    public void onClick(View view) {

        View newView2;
        Button delete2;


        if (view.equals(addPic)) {

            if (picContainer.getChildCount() < 4) {
                newView2 = inflater.inflate(R.layout.recipe_add_pic, null);

                openGallery();

                newView = newView2;

                delete2 = newView2.findViewById(R.id.create_recipe_instruction_delete_btn);

                delete2.setOnClickListener(this);

                picContainer.addView(newView2, picContainer.getChildCount());

                if (picContainer.getChildCount() == 4) {

                    addPic.setVisibility(View.GONE);
                }
            }

        } else if (view.getId() == R.id.create_recipe_instruction_delete_btn) {
            picContainer.removeView((View) view.getParent());
            pic.setImageURI(null);
            addPic.setVisibility(View.VISIBLE);

        }
    }

     public RecipeDTO getData(RecipeDTO recipeDTO){

        List<String> images = new ArrayList<String>();

        for (Uri image : imageList) {
            UUID picRandomID = UUID.randomUUID();
            images.add(picRandomID.toString());
            StorageReference ref = storageReference.child("recipeImages/" + picRandomID);
            ref.putFile(image);
        }

        recipeDTO.setImageList(images);

        return recipeDTO;
    }

    public void openGallery() {
        Intent galleryIntet = new Intent (Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntet, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageView picture1;
        picture1 = newView.findViewById(R.id.pic4);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {

            uri = data.getData();
            picture1.setImageURI(uri);
            imageList.add(uri);
        }
    }
}