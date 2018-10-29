package dk.michaelwestergaard.strikkehkleapp.fragments;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.R;

public class DiscoverFragment extends Fragment implements DiscoverStartFragment.OnFragmentInteractionListener, ListFragment.OnFragmentInteractionListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        tabLayout = getView().findViewById(R.id.top_menu);
        viewPager = getView().findViewById(R.id.container);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        TopViewPagerAdapter adapter = new TopViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new DiscoverStartFragment(), "Start");
        adapter.addFragment(new ListFragment(), "Trøjer");
        adapter.addFragment(new ListFragment(), "Huer");
        adapter.addFragment(new ListFragment(), "Handsker");
        adapter.addFragment(new ListFragment(), "Sokker");
        adapter.addFragment(new ListFragment(), "w/e");
        viewPager.setAdapter(adapter);
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
