package dk.michaelwestergaard.strikkehkleapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DAO.RecipeDAO;
import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeDTO;
import dk.michaelwestergaard.strikkehkleapp.Opskrift;
import dk.michaelwestergaard.strikkehkleapp.R;
import dk.michaelwestergaard.strikkehkleapp.adapters.CreateRecipeAdapter;

public class CreateRecipe extends Fragment implements StepperLayout.StepperListener, BlockingStep {

    private OnFragmentInteractionListener mListener;
    private StepperLayout stepperLayout;

    List<CreateRecipeAdapterStepperInfo> fragments;

    public CreateRecipe() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_recipe, container, false);

        stepperLayout = view.findViewById(R.id.stepperLayout);

        fragments = new ArrayList<CreateRecipeAdapterStepperInfo>();
        fragments.add(new CreateRecipeAdapterStepperInfo(new CreateRecipeStepOne(), "Oplysninger"));
        fragments.add(new CreateRecipeAdapterStepperInfo(new CreateRecipeStepTwo(), "Materialer"));
        fragments.add(new CreateRecipeAdapterStepperInfo(new CreateRecipeStepThree(), "Vejledning"));
        fragments.add(new CreateRecipeAdapterStepperInfo(new createRecipeStepFour(), "Billeder"));

        stepperLayout.setAdapter(new CreateRecipeAdapter(getFragmentManager(), getActivity(), fragments));
        stepperLayout.setListener(this);
        stepperLayout.setNextButtonVerificationFailed(true);
        stepperLayout.setCompleteButtonVerificationFailed(true);


        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCompleted(View completeButton) {

        RecipeDTO recipe = new RecipeDTO();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        Toast.makeText(getActivity(), "onCompleted!", Toast.LENGTH_SHORT).show();
        ((CreateRecipeStepOne) fragments.get(0).getFragment()).getData(recipe);
        ((CreateRecipeStepTwo) fragments.get(1).getFragment()).getData(recipe);
        ((CreateRecipeStepThree) fragments.get(2).getFragment()).getData(recipe);
        ((createRecipeStepFour) fragments.get(3).getFragment()).getData(recipe);

        recipe.setCreatedTimestamp(new Date());

        recipe.setUserID(auth.getCurrentUser().getUid());

        System.out.println(recipe);

        RecipeDAO recipeDAO = new RecipeDAO();

        String recipeID = recipeDAO.insert(recipe);

        if(!recipeID.isEmpty()) {

            Intent intent = new Intent(getContext(), Opskrift.class);
            intent.putExtra("RecipeID", recipeID);
            getContext().startActivity(intent);
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
        Toast.makeText(getActivity(), verificationError.getErrorMessage(), Toast.LENGTH_SHORT).show();
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

    public class CreateRecipeAdapterStepperInfo {
        private Fragment fragment;
        private String title;

        public CreateRecipeAdapterStepperInfo(Fragment fragment, String title) {
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
