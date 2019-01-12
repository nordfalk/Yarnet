package dk.michaelwestergaard.strikkehkleapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import dk.michaelwestergaard.strikkehkleapp.R;

public class CreateRecipeStepOne extends Fragment implements Step, RadioGroup.OnCheckedChangeListener {

    EditText title, description, price;
    Spinner type, category, subcategory;
    RadioGroup radioGroup;
    RadioButton radioFree, radioNotFree;

    CardView priceContainer;

    private OnFragmentInteractionListener mListener;

    public CreateRecipeStepOne() {
        // Required empty public constructor
    }

    public static CreateRecipeStepOne newInstance(String param1, String param2) {
        CreateRecipeStepOne fragment = new CreateRecipeStepOne();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_recipe_step_one, container, false);

        title = view.findViewById(R.id.create_recipe_title);
        description = view.findViewById(R.id.create_recipe_description);
        price = view.findViewById(R.id.create_recipe_price);

        type = view.findViewById(R.id.create_recipe_type);
        category = view.findViewById(R.id.create_recipe_spinner_category);
        subcategory = view.findViewById(R.id.create_recipe_spinner_subcategory);

        radioGroup = view.findViewById(R.id.create_recipe_price_selection);
        radioFree = view.findViewById(R.id.create_recipe_radio_free);
        radioNotFree = view.findViewById(R.id.create_recipe_radio_not_free);

        radioGroup.setOnCheckedChangeListener(this);

        priceContainer = view.findViewById(R.id.create_recipe_price_container);

        priceContainer.setVisibility(View.GONE);

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

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if(i == radioFree.getId()){
            //IT'S FREE GIMME GIMME
            priceContainer.setVisibility(View.GONE);
        } else if (i == radioNotFree.getId()){
            //NOTHING IS FREE IN LIFE
            priceContainer.setVisibility(View.VISIBLE);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
