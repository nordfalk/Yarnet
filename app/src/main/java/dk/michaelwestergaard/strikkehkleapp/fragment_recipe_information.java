package dk.michaelwestergaard.strikkehkleapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeInformationDTO;

public class fragment_recipe_information extends Fragment implements AdapterView.OnItemClickListener {

    RecipeInformationDTO informationDTO;
    ListView materialList, toolsList;
    List<String> materials;
    List<String> tools;
    String description = "";


    public static fragment_recipe_information newInstance(RecipeInformationDTO informationDTO) {
        fragment_recipe_information fragment = new fragment_recipe_information();
        fragment.materials = informationDTO.getMaterials();
        fragment.tools = informationDTO.getTools();
        fragment.informationDTO = informationDTO;
        fragment.description = informationDTO.getDescription();
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

        if(true){
            materialList = view.findViewById(R.id.materialList);
            toolsList = view.findViewById(R.id.toolsList);

            TextView descriptionTextView = view.findViewById(R.id.description);
            CardView descriptionCardView = view.findViewById(R.id.description_cardview);

            if(!description.isEmpty()){
                descriptionTextView.setText(description);
            } else {
                descriptionCardView.setVisibility(View.GONE);
            }

            BaseAdapter materialAdapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    if(materials != null) {
                        return materials.size();
                    } else {
                        return 0;
                    }
                }

                @Override
                public Object getItem(int position) {
                    return null;
                } //bruges ikke

                @Override
                public long getItemId(int position) {
                    return 0;
                } //bruges ikke

                @Override
                public View getView(int position, View cachedView, ViewGroup parent) {
                    View view = getLayoutInflater().inflate(R.layout.fragment_recipe_information_matelement, null);

                    TextView materialElement = view.findViewById(R.id.materialListElement);
                    if(materials != null) {
                        materialElement.setText("Test" + materials.get(position));
                    }
                    return view;
                }
            };

            BaseAdapter toolsAdapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    if(tools != null) {
                        return tools.size();
                    } else {
                        return 0;
                    }
                }

                @Override
                public Object getItem(int position) {
                    return null;
                } //bruges ikke

                @Override
                public long getItemId(int position) {
                    return 0;
                } //bruges ikke

                @Override
                public View getView(int position, View cachedView, ViewGroup parent) {
                    //ændre denne
                    View view = getLayoutInflater().inflate(R.layout.fragment_recipe_information_toolelement, null);

                    TextView toolElement = view.findViewById(R.id.toolListElement);
                    if(tools != null) {
                        toolElement.setText("Test" + tools.get(position));
                        System.out.println("Redskaber: " + tools.get(position));
                    }
                    return view;
                }
            };
            materialList.setOnItemClickListener(this);
            toolsList.setOnItemClickListener(this);
            materialList.setAdapter(materialAdapter);
            toolsList.setAdapter(toolsAdapter);
        }
        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
