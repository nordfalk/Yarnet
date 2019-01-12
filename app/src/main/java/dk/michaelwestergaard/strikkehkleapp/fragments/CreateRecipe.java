package dk.michaelwestergaard.strikkehkleapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.R;
import dk.michaelwestergaard.strikkehkleapp.adapters.CreateRecipeAdapter;

public class CreateRecipe extends Fragment implements StepperLayout.StepperListener {

    private OnFragmentInteractionListener mListener;
    private StepperLayout stepperLayout;

    public CreateRecipe() {

    }

    public static CreateRecipe newInstance(String param1, String param2) {
        CreateRecipe fragment = new CreateRecipe();
        return fragment;
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

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new CreateRecipeStepOne());
        fragments.add(new CreateRecipeStepTwo());
        fragments.add(new CreateRecipeStepThree());

        stepperLayout.setAdapter(new CreateRecipeAdapter(getFragmentManager(), getActivity(), fragments));
        stepperLayout.setListener(this);

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
        Toast.makeText(getActivity(), "onCompleted!", Toast.LENGTH_SHORT).show();
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
