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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DAO.CategoryDAO;
import dk.michaelwestergaard.strikkehkleapp.DTO.CategoryDTO;
import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeDTO;
import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeInformationDTO;
import dk.michaelwestergaard.strikkehkleapp.DTO.SubcategoryDTO;
import dk.michaelwestergaard.strikkehkleapp.R;

public class CreateRecipeStepOne extends Fragment implements Step, RadioGroup.OnCheckedChangeListener {

    CategoryDAO categoryDAO = new CategoryDAO();

    EditText title, description, price;
    Spinner type, category, subcategory;
    RadioGroup radioGroup;
    RadioButton radioFree, radioNotFree;

    final List<CategoryDTO> categories = new ArrayList<CategoryDTO>();
    List<SubcategoryDTO> subcategories;

    CardView priceContainer;

    public CreateRecipeStepOne() {
        // Required empty public constructor
    }

    public CreateRecipeStepOne newInstance(){
        return new CreateRecipeStepOne();
    }

    public static CreateRecipeStepOne newInstance(String param1, String param2) {
        CreateRecipeStepOne fragment = new CreateRecipeStepOne();
        return fragment;
    }

    public RecipeDTO getData(RecipeDTO recipeDTO){
        recipeDTO.setTitle(title.getText().toString());

        RecipeInformationDTO recipeInformationDTO = new RecipeInformationDTO();
        recipeInformationDTO.setDescription(description.getText().toString());
        recipeDTO.setRecipeInformationDTO(recipeInformationDTO);

        if(radioGroup.getCheckedRadioButtonId() == radioNotFree.getId()){
            if(!price.getText().toString().matches("")){
                recipeDTO.setPrice(Double.parseDouble(price.getText().toString()));
            } else {
                recipeDTO.setPrice(0.00);
            }
        } else {
            recipeDTO.setPrice(0.00);
        }

        recipeDTO.setCategoryID(categories.get(category.getSelectedItemPosition()).getId());
        recipeDTO.setSubcategoryID(subcategories.get(subcategory.getSelectedItemPosition()).getId());

        return recipeDTO;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_create_recipe_step_one, container, false);

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

        categoryDAO.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    CategoryDTO categoryDTO = snapshot.getValue(CategoryDTO.class);
                    categories.add(categoryDTO);
                }

                List<String> categoryNames = new ArrayList<String>();

                for(CategoryDTO categoryDTO : categories){
                    categoryNames.add(categoryDTO.getName());
                }

                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_item, R.id.name, categoryNames);
                category.setAdapter(categoryAdapter);
                category.setPrompt("Vælg Kategori");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, final View view, int i, long l) {
                subcategories = categories.get(i).getSubcategoryList();
                List<String> subcategoryNames = new ArrayList<String>();
                for(SubcategoryDTO subcategoryDTO : subcategories){
                    subcategoryNames.add(subcategoryDTO.getName());
                }

                ArrayAdapter<String> subategoryAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_item, R.id.name, subcategoryNames);
                subcategory.setAdapter(subategoryAdapter);
                subcategory.setPrompt("Vælg Underkategori");

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if(!title.getText().toString().matches("")){
            if(!description.getText().toString().matches("")){
                if(radioGroup.getCheckedRadioButtonId() != radioNotFree.getId()){
                    return null;
                } else {
                    if(!price.getText().toString().matches("")){
                        return null;
                    }
                }
            }
        }
        return new VerificationError("Udfyld venligst alle felter!");
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
