package dk.michaelwestergaard.strikkehkleapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeInformationDTO;

public class fragment_recipe_information extends Fragment implements AdapterView.OnItemClickListener {

    RecipeInformationDTO informationDTO;
    ListView materialList, toolsList;
    List<String> materials;
    List<String> tools;


    public static fragment_recipe_information newInstance(RecipeInformationDTO informationDTO) {
        fragment_recipe_information fragment = new fragment_recipe_information();
        fragment.materials = informationDTO.getMaterials();
        fragment.tools = informationDTO.getTools();
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
            materialList = view.findViewById(R.id.materialList);
            toolsList = view.findViewById(R.id.toolsList);

            BaseAdapter materialAdapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return materials.size();
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
                    materialElement.setText("Test"+ materials.get(position));
                    System.out.println("Materialer: " +materials.get(position));
                    return view;
                }
            };

            BaseAdapter toolsAdapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return tools.size();
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
                    toolElement.setText("Test"+ tools.get(position));
                    System.out.println("Redskaber: " + tools.get(position));
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
