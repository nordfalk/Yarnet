package dk.michaelwestergaard.strikkehkleapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dk.michaelwestergaard.strikkehkleapp.ListAdapter;
import dk.michaelwestergaard.strikkehkleapp.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DiscoverStartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiscoverStartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverStartFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public DiscoverStartFragment() {
        // Required empty public constructor
    }

    public static DiscoverStartFragment newInstance(String param1, String param2) {
        DiscoverStartFragment fragment = new DiscoverStartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover_start, container, false);

        ListAdapter listAdapter = new ListAdapter();

        RecyclerView recyclerViewNew = view.findViewById(R.id.item_list_new);
        RecyclerView.LayoutManager layoutManagerNew = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewNew.setAdapter(listAdapter);
        recyclerViewNew.setLayoutManager(layoutManagerNew);

        RecyclerView recyclerViewPaid = view.findViewById(R.id.item_list_paid);
        RecyclerView.LayoutManager layoutManagerPaid = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPaid.setAdapter(listAdapter);
        recyclerViewPaid.setLayoutManager(layoutManagerPaid);

        RecyclerView recyclerViewFree = view.findViewById(R.id.item_list_free);
        RecyclerView.LayoutManager layoutManagerFree = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewFree.setAdapter(listAdapter);
        recyclerViewFree.setLayoutManager(layoutManagerFree);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
