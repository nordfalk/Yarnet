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
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DAO.CategoryDAO;
import dk.michaelwestergaard.strikkehkleapp.DTO.CategoryDTO;
import dk.michaelwestergaard.strikkehkleapp.DTO.SubcategoryDTO;
import dk.michaelwestergaard.strikkehkleapp.R;

public class DiscoverSubFragment extends Fragment implements DiscoverStartFragment.OnFragmentInteractionListener, ListFragment.OnFragmentInteractionListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private CategoryDAO categoryDAO = new CategoryDAO();
    String categoryID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_sub_discover, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        tabLayout = getView().findViewById(R.id.top_undermenu);
        viewPager = getView().findViewById(R.id.container);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        if (getArguments() != null) {
            Bundle arguments = getArguments();
            categoryID = arguments.getString("categoryID");
        }
    }

    private void setupViewPager(final ViewPager viewPager) {
        final List<CategoryDTO> categories = new ArrayList<CategoryDTO>();

        categoryDAO.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    categories.add(snapshot.getValue(CategoryDTO.class));
                }

                List<SubcategoryDTO> subCategories = new ArrayList<>();

                for(CategoryDTO category : categories) {
                    if(category.getId().equals(categoryID)) {
                        subCategories = category.getSubcategoryList();
                        break;
                    }
                }

                TopViewPagerAdapter adapter = new TopViewPagerAdapter(getChildFragmentManager());

                Bundle startArguments = new Bundle();
                startArguments.putString("categoryID", categoryID);

                ListFragment startFragment = new ListFragment();
                startFragment.setArguments(startArguments);

                adapter.addFragment(startFragment, "Alle");

                for(SubcategoryDTO subCategory : subCategories) {
                    Bundle arguments = new Bundle();
                    arguments.putString("categoryID", categoryID);
                    arguments.putString("subCategoryID", subCategory.getId());

                    ListFragment newFragment = new ListFragment();
                    newFragment.setArguments(arguments);

                    adapter.addFragment(newFragment, subCategory.getName());
                }

                viewPager.setAdapter(adapter);

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
