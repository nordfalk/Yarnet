package dk.michaelwestergaard.strikkehkleapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeDTO;
import dk.michaelwestergaard.strikkehkleapp.R;

import static android.app.Activity.RESULT_OK;

public class EditRecipeStepFour extends Fragment implements Step, View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE = 4;
    LayoutInflater inflater;
    LinearLayout picContainer;
    Uri uri;
    ImageView pic;
    Button removeBtn, addPic;
    View newView, list;
    List<Uri> imageList = new ArrayList<Uri>();
    private StorageReference storageReference;
    private FirebaseStorage storage;

    private List<String> recipeImageList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle arguments = this.getArguments();
            recipeImageList = arguments.getStringArrayList("imageList");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_recipe_step_four, container, false);

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

    /**
     * Checks if the stepper can go to the next step after this step.<br>
     * <b>This does not mean the user clicked on the Next/Complete button.</b><br>
     * If the user clicked the Next/Complete button and wants to be informed of that error
     * he should handle this in {@link #onError(VerificationError)}.
     *
     * @return the cause of the validation failure or <i>null</i> if step was validated successfully
     */
    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    /**
     * Called when this step gets selected in the the stepper layout.
     */
    @Override
    public void onSelected() {

    }

    /**
     * Called when the user clicked on the Next/Complete button and the step verification failed.
     *
     * @param error the cause of the validation failure
     */
    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
