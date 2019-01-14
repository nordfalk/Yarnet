package dk.michaelwestergaard.strikkehkleapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeInformationDTO;

public class fragment_recipe_information extends Fragment {

    RecipeInformationDTO informationDTO;
    TextView description,headline,materialList;

    public static fragment_recipe_information newInstance(RecipeInformationDTO informationDTO) {
        fragment_recipe_information fragment = new fragment_recipe_information();
        fragment.informationDTO = informationDTO;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Opskrift opskrift = new Opskrift();

        View view = inflater.inflate(R.layout.fragment_recipe_information, container, false);

        if(opskrift.bought == true){
            description=view.findViewById(R.id.description);
            headline=view.findViewById(R.id.headline);
            materialList=view.findViewById(R.id.materialList);
        }
        return view;
    }


}
