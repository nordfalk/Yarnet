package dk.michaelwestergaard.strikkehkleapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DAO.CategoryDAO;
import dk.michaelwestergaard.strikkehkleapp.DAO.RecipeDAO;
import dk.michaelwestergaard.strikkehkleapp.DTO.CategoryDTO;
import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeDTO;
import dk.michaelwestergaard.strikkehkleapp.DTO.UserDTO;
import dk.michaelwestergaard.strikkehkleapp.MainSingleton;
import dk.michaelwestergaard.strikkehkleapp.R;

public class DiscoverFragment extends Fragment implements ListFragment.OnFragmentInteractionListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingSearchView searchView;
    private RecipeDAO recipeDAO = new RecipeDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    private int lastPosition = 0;
    private int lastNonSearchPosition = 0;
    private String lastSearchValue = "";
    private boolean returnFromRecipe = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        tabLayout = getView().findViewById(R.id.top_menu);
        viewPager = getView().findViewById(R.id.container);
        searchView = getView().findViewById(R.id.floating_search_view);

        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                final List<Suggestion> suggestions = new ArrayList<>();

                recipeDAO.getReference().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserDTO user = MainSingleton.getInstance().getUser();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            if(snapshot.getValue(RecipeDTO.class).getRecipeType().toString().equals(user.getType()) || user.getType().equals("BOTH")) {
                                String recipeDifficulty = snapshot.getValue(RecipeDTO.class).getRecipeDifficulty().toString();
                                String recipeTitle;

                                switch (user.getDifficulty()) {
                                    case "HARD":
                                        recipeTitle = snapshot.getValue(RecipeDTO.class).getTitle();

                                        if (recipeTitle.toLowerCase().contains(newQuery.toLowerCase())) {
                                            suggestions.add(new Suggestion(recipeTitle));
                                        }
                                        break;

                                    case "MEDIUM":
                                        if(recipeDifficulty.equals("MEDIUM") || recipeDifficulty.equals("EASY")) {
                                            recipeTitle = snapshot.getValue(RecipeDTO.class).getTitle();

                                            if (recipeTitle.toLowerCase().contains(newQuery.toLowerCase())) {
                                                suggestions.add(new Suggestion(recipeTitle));
                                            }
                                        }
                                        break;

                                    case "EASY":
                                        if(recipeDifficulty.equals("EASY")) {
                                            recipeTitle = snapshot.getValue(RecipeDTO.class).getTitle();

                                            if (recipeTitle.toLowerCase().contains(newQuery.toLowerCase())) {
                                                suggestions.add(new Suggestion(recipeTitle));
                                            }
                                        }
                                        break;
                                }
                            }
                        }

                        searchView.swapSuggestions(suggestions);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Log.d("Suggestions", suggestions.toString());
            }
        });

        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                String search = searchSuggestion.getBody();
                setupViewPager(viewPager, search);
                lastSearchValue = search;
                searchView.clearSuggestions();
                hideKeyboardFrom(getContext(), getView());
            }

            @Override
            public void onSearchAction(String query) {
                String search = query;
                setupViewPager(viewPager, search);
                lastSearchValue = search;
            }
        });

        setupViewPager(viewPager, null);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(final ViewPager viewPager, String search) {
        final List<CategoryDTO> categories = new ArrayList<CategoryDTO>();
        final String searchValue = search;
        MainSingleton.getInstance().setSearchValue(search);

        categoryDAO.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    categories.add(snapshot.getValue(CategoryDTO.class));
                }

                final TopViewPagerAdapter adapter = new TopViewPagerAdapter(getChildFragmentManager());
                adapter.addFragment(new DiscoverStartFragment(), "Start");

                for(CategoryDTO category : categories) {
                    Bundle arguments = new Bundle();
                    arguments.putString("categoryID", category.getId());

                    DiscoverSubFragment newFragment = new DiscoverSubFragment();
                    newFragment.setArguments(arguments);

                    adapter.addFragment(newFragment, category.getName());
                }

                int adapterCount = 0;
                if(searchValue != null){
                    ListFragment searchFragment = new ListFragment();
                    adapter.addFragment(searchFragment, "\"" + searchValue + "\"");
                    adapterCount = adapter.getCount();
                }

                viewPager.setAdapter(adapter);

                if(adapterCount != 0) {
                    viewPager.setCurrentItem(adapterCount);
                } else if(lastNonSearchPosition != 0 && !returnFromRecipe) {
                    viewPager.setCurrentItem(lastNonSearchPosition);
                }

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        try {

                            boolean track = true;
                            returnFromRecipe = false;

                            if(!(((TopViewPagerAdapter) viewPager.getAdapter()).getmFragmentTitleList().get(position).contains("\""))) {
                                if(lastPosition < ((TopViewPagerAdapter) viewPager.getAdapter()).getmFragmentTitleList().size()) {
                                    if (((TopViewPagerAdapter) viewPager.getAdapter()).getmFragmentTitleList().get(lastPosition).contains("\"")) {
                                        setupViewPager(viewPager, null);
                                        lastPosition = lastNonSearchPosition;
                                        track = false;
                                    }
                                } else {
                                    returnFromRecipe = true;
                                    setupViewPager(viewPager, lastSearchValue);
                                }
                                if(position < categories.size()) {
                                    lastNonSearchPosition = position;
                                }
                            }

                            if(track) {
                                lastPosition = position;
                            }

                            if((((TopViewPagerAdapter) viewPager.getAdapter()).getmFragmentList().get(position)) instanceof DiscoverSubFragment)
                                ((DiscoverSubFragment)((TopViewPagerAdapter) viewPager.getAdapter()).getmFragmentList().get(position)).viewPager.setCurrentItem(0);
                        } catch(NullPointerException e) {
                            e.printStackTrace();
                            FirebaseCrash.logcat(Log.ERROR, "NullPointer", "NPE caught");
                            FirebaseCrash.report(e);
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.d("Recipes", categories.toString());
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class TopViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public TopViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        public List<Fragment> getmFragmentList() {
            return mFragmentList;
        }

        public List<String> getmFragmentTitleList() {
            return mFragmentTitleList;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

class Suggestion implements SearchSuggestion {
    private String suggestion;

    public Suggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getSuggestion() {
        return this.suggestion;
    }

    @Override
    public String getBody() {
        return this.suggestion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final SearchSuggestion.Creator<Suggestion> CREATOR = new SearchSuggestion.Creator<Suggestion>() {

        @Override
        public Suggestion createFromParcel(Parcel in) {
            return new Suggestion(in.toString());
        }

        @Override
        public Suggestion[] newArray(int size) {
            return new Suggestion[size];
        }
    };
}
