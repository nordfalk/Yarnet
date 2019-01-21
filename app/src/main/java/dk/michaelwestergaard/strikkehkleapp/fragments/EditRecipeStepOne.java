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

public class EditRecipeStepOne extends Fragment implements Step, RadioGroup.OnCheckedChangeListener {

    private CategoryDAO categoryDAO = new CategoryDAO();

    private EditText title, description, price;
    private Spinner type, category, subcategory, difficulty;
    private RadioGroup radioGroup;
    private RadioButton radioFree, radioNotFree;

    private final List<CategoryDTO> categories = new ArrayList<CategoryDTO>();
    private List<SubcategoryDTO> subcategories;

    private CardView priceContainer;

    private String recipeType, recipeTitle, recipeDescription, recipeCategoryID, recipeSubCategoryID, recipeDifficulty;
    private Double recipePrice;

    public EditRecipeStepOne() {
        // Required empty public constructor
    }

    public EditRecipeStepOne newInstance(){
        return new EditRecipeStepOne();
    }

    public static EditRecipeStepOne newInstance(String param1, String param2) {
        EditRecipeStepOne fragment = new EditRecipeStepOne();
        return fragment;
    }



    public RecipeDTO getData(RecipeDTO recipeDTO){

        if(type.getSelectedItemPosition() == 0){
            recipeDTO.setRecipeType(RecipeDTO.RecipeType.KNITTING);
        } else if(type.getSelectedItemPosition() == 1){
            recipeDTO.setRecipeType(RecipeDTO.RecipeType.CROCHETING);
        }

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

        RecipeDTO.RecipeDifficulty recipeDifficulty;
        switch(difficulty.getSelectedItemPosition()){

            case 0:
                recipeDifficulty = RecipeDTO.RecipeDifficulty.EASY;
                break;
            case 1:
                recipeDifficulty = RecipeDTO.RecipeDifficulty.MEDIUM;
                break;
            case 2:
                recipeDifficulty = RecipeDTO.RecipeDifficulty.HARD;
                break;

            default:
                recipeDifficulty = RecipeDTO.RecipeDifficulty.EASY;
                break;
        }
        recipeDTO.setRecipeDifficulty(recipeDifficulty);

        recipeDTO.setCategoryID(categories.get(category.getSelectedItemPosition()).getId());
        recipeDTO.setSubcategoryID(subcategories.get(subcategory.getSelectedItemPosition()).getId());

        return recipeDTO;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle arguments = this.getArguments();
            recipeType = arguments.getString("recipeType");
            recipeTitle = arguments.getString("title");
            recipeDescription = arguments.getString("description");
            recipeCategoryID = arguments.getString("categoryID");
            recipeSubCategoryID = arguments.getString("subCategoryID");
            recipeDifficulty = arguments.getString("recipeDifficulty");
            recipePrice = arguments.getDouble("price");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_edit_recipe_step_one, container, false);

        title = view.findViewById(R.id.edit_recipe_title);
        title.setText(recipeTitle);

        description = view.findViewById(R.id.edit_recipe_description);
        description.setText(recipeDescription);

        price = view.findViewById(R.id.edit_recipe_price);

        type = view.findViewById(R.id.edit_recipe_type);
        category = view.findViewById(R.id.edit_recipe_spinner_category);
        subcategory = view.findViewById(R.id.edit_recipe_spinner_subcategory);
        difficulty = view.findViewById(R.id.edit_recipe_spinner_difficulty);

        radioGroup = view.findViewById(R.id.edit_recipe_price_selection);
        radioFree = view.findViewById(R.id.edit_recipe_radio_free);
        radioNotFree = view.findViewById(R.id.edit_recipe_radio_not_free);

        radioGroup.setOnCheckedChangeListener(this);

        priceContainer = view.findViewById(R.id.edit_recipe_price_container);

        if(recipePrice == 0.0) {
            priceContainer.setVisibility(View.GONE);
        } else {
            radioGroup.check(R.id.edit_recipe_radio_not_free);
            priceContainer.setVisibility(View.VISIBLE);
            price.setText(recipePrice.toString().replace(".0", ""));
        }

        ArrayAdapter<String> difficulties = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_item, R.id.name, getResources().getStringArray(R.array.NewRecipeDifficulty));
        difficulty.setAdapter(difficulties);
        difficulty.setPrompt("Vælg Sværhedsgrad");
        if(recipeDifficulty.equals("EASY")) {
            difficulty.setSelection(0);
        } else if(recipeDifficulty.equals("MEDIUM")) {
            difficulty.setSelection(1);
        } else if(recipeDifficulty.equals("HARD")) {
            difficulty.setSelection(2);
        }

        categoryDAO.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categories.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    CategoryDTO categoryDTO = snapshot.getValue(CategoryDTO.class);
                    categories.add(categoryDTO);
                }

                List<String> categoryNames = new ArrayList<String>();
                int previousCategoryIndex = 0;

                for(CategoryDTO categoryDTO : categories){
                    categoryNames.add(categoryDTO.getName());

                    if(categoryDTO.getId().equals(recipeCategoryID)) {
                        previousCategoryIndex = categoryNames.size() - 1;
                    }
                }

                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_item, R.id.name, categoryNames);
                category.setAdapter(categoryAdapter);
                category.setPrompt("Vælg Kategori");
                category.setSelection(previousCategoryIndex);
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
                int previousSubCategoryIndex = 0;

                for(SubcategoryDTO subcategoryDTO : subcategories){
                    subcategoryNames.add(subcategoryDTO.getName());

                    if(subcategoryDTO.getId().equals(recipeSubCategoryID)) {
                        previousSubCategoryIndex = subcategoryNames.size() - 1;
                    }
                }

                ArrayAdapter<String> subCategoryAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_item, R.id.name, subcategoryNames);
                subcategory.setAdapter(subCategoryAdapter);
                subcategory.setPrompt("Vælg Underkategori");
                subcategory.setSelection(previousSubCategoryIndex);
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
            if(radioGroup.getCheckedRadioButtonId() != radioNotFree.getId()){
                return null;
            } else {
                if(!price.getText().toString().matches("")){
                    return null;
                } else {
                    price.setError("Indtast venligst en pris!");
                }
            }
        } else {
            title.setError("Opskriften skal have en overskrift!");
        }
        return new VerificationError("Udfyld venligst overstående felter!");
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
