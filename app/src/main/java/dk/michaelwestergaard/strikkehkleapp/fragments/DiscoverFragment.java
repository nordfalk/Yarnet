package dk.michaelwestergaard.strikkehkleapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DAO.CategoryDAO;
import dk.michaelwestergaard.strikkehkleapp.DTO.CategoryDTO;
import dk.michaelwestergaard.strikkehkleapp.R;

public class DiscoverFragment extends Fragment implements ListFragment.OnFragmentInteractionListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingSearchView searchView;
    private CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        tabLayout = getView().findViewById(R.id.top_menu);
        viewPager = getView().findViewById(R.id.container);
        searchView = getView().findViewById(R.id.floating_search_view)

        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                //get suggestions based on newQuery

                //pass them on to the search view
                searchView.swapSuggestions(suggestions);
            }
        });

        searchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {

            }
        });

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(final ViewPager viewPager) {
        final List<CategoryDTO> categories = new ArrayList<CategoryDTO>();

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

                viewPager.setAdapter(adapter);
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        try {
                            if((((TopViewPagerAdapter) viewPager.getAdapter()).getmFragmentList().get(position)) instanceof DiscoverSubFragment)
                                ((DiscoverSubFragment)((TopViewPagerAdapter) viewPager.getAdapter()).getmFragmentList().get(position)).viewPager.setCurrentItem(0);
                        } catch(NullPointerException e) {
                            e.printStackTrace();
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
