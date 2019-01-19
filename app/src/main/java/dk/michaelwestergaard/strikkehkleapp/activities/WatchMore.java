package dk.michaelwestergaard.strikkehkleapp.activities;


import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;
import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeDTO;
import dk.michaelwestergaard.strikkehkleapp.R;
import dk.michaelwestergaard.strikkehkleapp.ViewPagerAdapter;
import dk.michaelwestergaard.strikkehkleapp.adapters.RecipeAdapter;

public class WatchMore extends AppCompatActivity {

    ViewPager viewPager;
    ImageView backBtn;
    ImageView drawerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_more);

        drawerBtn = findViewById(R.id.drawerBtn);
        backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } ));

        backBtn.setVisibility(View.VISIBLE);
        drawerBtn.setVisibility(View.GONE);

        ViewPager viewPager = findViewById(R.id.watchMoreViewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new WatchMoreListFragment(), "RecipeList");
        viewPager.setAdapter(adapter);
    }

    public static class WatchMoreSingleton {
        private List<RecipeDTO> recipes;
        private static WatchMoreSingleton instance = null;
        public static WatchMoreSingleton getInstance() {
            if (instance == null) {
                instance = new WatchMoreSingleton();
            }
            return instance;
        }

        public List<RecipeDTO> getRecipes() {
            return recipes;
        }

        public void setRecipes(List<RecipeDTO> recipes) {
            this.recipes = recipes;
        }
    }

    public static class WatchMoreListFragment extends Fragment {

        public WatchMoreListFragment() {

    }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            }

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.fragment_list, container, false);

            RecyclerView recyclerView = view.findViewById(R.id.recyclerViewGrid);

            RecipeAdapter adapter = new RecipeAdapter(WatchMoreSingleton.getInstance().getRecipes());
            recyclerView.setAdapter(adapter);

            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
            recyclerView.setLayoutManager(layoutManager);

            return view;
        }
    }
}
