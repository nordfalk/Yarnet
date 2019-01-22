package dk.michaelwestergaard.strikkehkleapp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DAO.RecipeDAO;
import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeDTO;
import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeInformationDTO;
import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeInstructionDTO;
import dk.michaelwestergaard.strikkehkleapp.Opskrift;
import dk.michaelwestergaard.strikkehkleapp.R;
import dk.michaelwestergaard.strikkehkleapp.adapters.EditRecipeAdapter;
import dk.michaelwestergaard.strikkehkleapp.fragments.EditRecipeStepFour;
import dk.michaelwestergaard.strikkehkleapp.fragments.EditRecipeStepOne;
import dk.michaelwestergaard.strikkehkleapp.fragments.EditRecipeStepThree;
import dk.michaelwestergaard.strikkehkleapp.fragments.EditRecipeStepTwo;

public class EditRecipe extends AppCompatActivity implements StepperLayout.StepperListener, BlockingStep {

    private ImageView backBtn;
    private ImageView drawerBtn;

    private StepperLayout stepperLayout;

    private String recipeID;
    private RecipeDTO recipe = null;
    private RecipeInformationDTO recipeInformation = null;
    private ArrayList<RecipeInstructionDTO> recipeInstructions = null;
    private RecipeDAO recipeDAO = new RecipeDAO();

    private List<EditRecipeAdapterStepperInfo> fragments;
    private Context myContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        drawerBtn = findViewById(R.id.drawerBtn);
        backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }));

        backBtn.setVisibility(View.VISIBLE);
        drawerBtn.setVisibility(View.GONE);

        stepperLayout = findViewById(R.id.stepperLayout);
        stepperLayout.setListener(this);
        stepperLayout.setNextButtonVerificationFailed(true);
        stepperLayout.setCompleteButtonVerificationFailed(true);

        recipeID = getIntent().getStringExtra("recipeID");

        recipeDAO.getReference().child(recipeID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipe = dataSnapshot.getValue(RecipeDTO.class);
                recipeInformation = recipe.getRecipeInformationDTO();
                recipeInstructions = (ArrayList) recipe.getRecipeInstructionDTO();

                Bundle stepOneArgs = new Bundle();
                stepOneArgs.putString("recipeType", recipe.getRecipeType().toString());
                stepOneArgs.putString("recipeDifficulty", recipe.getRecipeDifficulty().toString());
                stepOneArgs.putString("categoryID", recipe.getCategoryID());
                stepOneArgs.putString("subCategoryID", recipe.getSubcategoryID());
                stepOneArgs.putString("title", recipe.getTitle());
                stepOneArgs.putString("description", recipeInformation.getDescription());
                stepOneArgs.putDouble("price", recipe.getPrice());

                Bundle stepTwoArgs = new Bundle();
                stepTwoArgs.putStringArrayList("materials", (ArrayList) recipeInformation.getMaterials());
                stepTwoArgs.putStringArrayList("tools", (ArrayList) recipeInformation.getTools());

                Bundle stepThreeArgs = new Bundle();
                stepThreeArgs.putParcelableArrayList("instructions", recipeInstructions);

                Bundle stepFourArgs = new Bundle();
                stepFourArgs.putStringArrayList("imageList", (ArrayList) recipe.getImageList());

                EditRecipeStepOne stepOneFrag = new EditRecipeStepOne();
                EditRecipeStepTwo stepTwoFrag = new EditRecipeStepTwo();
                EditRecipeStepThree stepThreeFrag = new EditRecipeStepThree();
                EditRecipeStepFour stepFourFrag = new EditRecipeStepFour();

                stepOneFrag.setArguments(stepOneArgs);
                stepTwoFrag.setArguments(stepTwoArgs);
                stepThreeFrag.setArguments(stepThreeArgs);
                stepFourFrag.setArguments(stepFourArgs);

                fragments = new ArrayList<>();
                fragments.add(new EditRecipeAdapterStepperInfo(stepOneFrag, "Oplysninger"));
                fragments.add(new EditRecipeAdapterStepperInfo(stepTwoFrag, "Materialer"));
                fragments.add(new EditRecipeAdapterStepperInfo(stepThreeFrag, "Vejledning"));
                fragments.add(new EditRecipeAdapterStepperInfo(stepFourFrag, "Billeder"));

                stepperLayout.setAdapter(new EditRecipeAdapter(getSupportFragmentManager(), myContext, fragments));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCompleted(View completeButton) {

        Toast.makeText(this, "onCompleted!", Toast.LENGTH_SHORT).show();
        ((EditRecipeStepOne) fragments.get(0).getFragment()).getData(recipe);
        ((EditRecipeStepTwo) fragments.get(1).getFragment()).getData(recipe);
        ((EditRecipeStepThree) fragments.get(2).getFragment()).getData(recipe);
        ((EditRecipeStepFour) fragments.get(3).getFragment()).getData(recipe);

        recipe.setUpdatedTimestamp(new Date());

        System.out.println(recipe);

        RecipeDAO recipeDAO = new RecipeDAO();

        boolean updated = false;
        updated = recipeDAO.update(recipe);

        if(updated) {
            Intent intent = new Intent(this, Opskrift.class);
            intent.putExtra("RecipeID", recipe.getRecipeID());
            startActivity(intent);
        }

    }

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

    @Override
    public void onError(VerificationError verificationError) {
        Toast.makeText(this, verificationError.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStepSelected(int newStepPosition) {
    }
    @Override
    public void onReturn() {

    }

    /**
     * Notifies this step that the next button/tab was clicked, the step was verified
     * and the user can go to the next step. This is so that the current step might perform
     * some last minute operations e.g. a network call before switching to the next step.
     * {@link StepperLayout.OnNextClickedCallback#goToNextStep()} must be called once these operations finish.
     *
     * @param callback callback to call once the user wishes to finally switch to the next step
     */
    @Override
    public void onNextClicked(final StepperLayout.OnNextClickedCallback callback) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.goToNextStep();
            }
        }, 2000L);
    }

    /**
     * Notifies this step that the complete button/tab was clicked, the step was verified
     * and the user can complete the flow. This is so that the current step might perform
     * some last minute operations e.g. a network call before completing the flow.
     * {@link StepperLayout.OnCompleteClickedCallback#complete()} must be called once these operations finish.
     *
     * @param callback callback to call once the user wishes to complete the flow
     */
    @Override
    public void onCompleteClicked(final StepperLayout.OnCompleteClickedCallback callback) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.complete();
            }
        }, 2000L);
    }

    /**
     * Notifies this step that the previous button/tab was clicked. This is so that the current step might perform
     * some last minute operations e.g. a network call before switching to previous step.
     * {@link StepperLayout.OnBackClickedCallback#goToPrevStep()} must be called once these operations finish.
     *
     * @param callback callback to call once the user wishes to finally switch to the previous step
     */
    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public class EditRecipeAdapterStepperInfo {
        private Fragment fragment;
        private String title;

        public EditRecipeAdapterStepperInfo(Fragment fragment, String title) {
            this.fragment = fragment;
            this.title = title;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
