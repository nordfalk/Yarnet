package dk.michaelwestergaard.strikkehkleapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_recipe_instruction.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_recipe_instruction#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_recipe_instruction extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recipeInformationList, guideTop;
    TextView headlineElement,underlineElement,instructionPoint;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public fragment_recipe_instruction() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_recipe_instruction.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_recipe_instruction newInstance(String param1, String param2) {
        fragment_recipe_instruction fragment = new fragment_recipe_instruction();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Opskrift opskrift = new Opskrift();
        View view = inflater.inflate(R.layout.fragment_recipe_instruction, container, false);

        if(opskrift.bought = true){
            recipeInformationList = view.findViewById(R.id.recipeInformationList);

            InstructionAdapter ToplistAdapter = new InstructionAdapter();
            //recipeInformationList.setAdapter(ToplistAdapter);
          //  guideTop.setAdapter(ToplistAdapter);
        }

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class InstructionAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.fragment_recipe_instruction_top_element,null);
            TextView instructionPoint,headlineElement;
            RecyclerView ToplineRC;

            ToplineRC = view.findViewById(R.id.underlineCycleView);
            instructionPoint= view.findViewById(R.id.instructionPoint);
            headlineElement = view.findViewById(R.id.headlineElement);

            ToplineRC.setAdapter();


            return null;
        }
    }


}

