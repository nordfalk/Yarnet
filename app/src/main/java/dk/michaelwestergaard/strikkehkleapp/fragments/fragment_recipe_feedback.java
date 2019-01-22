package dk.michaelwestergaard.strikkehkleapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DAO.RecipeDAO;
import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeDTO;
import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeFeedbackDTO;
import dk.michaelwestergaard.strikkehkleapp.DTO.UserDTO;
import dk.michaelwestergaard.strikkehkleapp.MainSingleton;
import dk.michaelwestergaard.strikkehkleapp.R;

public class fragment_recipe_feedback extends Fragment {

    private UserDTO userBrowsing = MainSingleton.getInstance().getUser();
    RecyclerView recyclerView;
    private View feedbackContainer;
    private Spinner difficultySpinner;
    private EditText comment;
    private Button feedbackBtn;
    RecipeDTO recipe;
    RecipeDAO recipeDAO = new RecipeDAO();

    public fragment_recipe_feedback() {
        // Required empty public constructor
    }

    public static fragment_recipe_feedback newInstance(RecipeDTO recipe) {
        fragment_recipe_feedback fragment = new fragment_recipe_feedback();
        fragment.recipe = recipe;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_feedback, container, false);

        recyclerView = view.findViewById(R.id.recipe_feedback_recyclerview);

        feedbackContainer = view.findViewById(R.id.feedback_container);
        feedbackBtn = view.findViewById(R.id.recipe_feedback_submit);
        difficultySpinner = view.findViewById(R.id.recipe_feedback_spinner_difficulty);
        comment = view.findViewById(R.id.comment);

        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String difficultyTxt;
                switch(difficultySpinner.getSelectedItemPosition()){

                    case 0:
                        difficultyTxt = "let";
                        break;
                    case 1:
                        difficultyTxt = "middel";
                        break;
                    case 2:
                        difficultyTxt = "svær";
                        break;

                    default:
                        difficultyTxt = "let";
                        break;
                }

                List<RecipeFeedbackDTO> feedbackList;
                if(recipe.getFeedbackList() != null) {
                    feedbackList = recipe.getFeedbackList();
                } else {
                    feedbackList = new ArrayList<RecipeFeedbackDTO>();
                }
                feedbackList.add(new RecipeFeedbackDTO(userBrowsing.getUserID(), userBrowsing.getFirst_name() + " " + userBrowsing.getLast_name(), userBrowsing.getAvatar(), userBrowsing.getFirst_name() + " fandt denne opskrift " + difficultyTxt, comment.getText().toString()));

                recipe.setFeedbackList(feedbackList);
                recipeDAO.update(recipe);
                feedbackContainer.setVisibility(View.GONE);
                ((FeedbackAdapter)recyclerView.getAdapter()).setFeedbackList(feedbackList);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        FeedbackAdapter instructionAdapter = new FeedbackAdapter(recipe.getFeedbackList());
        recyclerView.setAdapter(instructionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false));

        return view;
    }

    private class FeedbackAdapter extends RecyclerView.Adapter {

        List<RecipeFeedbackDTO> feedbackList;

        public void setFeedbackList(List<RecipeFeedbackDTO> feedbackList) {
            this.feedbackList = feedbackList;
        }

        public FeedbackAdapter(List<RecipeFeedbackDTO> feedbackList) {
            this.feedbackList = feedbackList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_listing_element, parent, false);
            return new FeedbackViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((FeedbackViewHolder) holder).bindView(position);
        }

        @Override
        public int getItemCount() {
            if(feedbackList != null)
                return feedbackList.size();

            return 0;
        }

        private class FeedbackViewHolder extends RecyclerView.ViewHolder {

            TextView title, userName, comment;
            ImageView userAvatar;

            public FeedbackViewHolder(View itemView) {
                super(itemView);

                title = itemView.findViewById(R.id.title);
                userName = itemView.findViewById(R.id.user);
                comment = itemView.findViewById(R.id.comment);
                userAvatar = itemView.findViewById(R.id.user_avatar);
            }

            public void bindView(int position){
                RecipeFeedbackDTO feedbackDTO = feedbackList.get(position);
                title.setText(feedbackDTO.getTitle());
                userName.setText(feedbackDTO.getUserName());
                comment.setText(feedbackDTO.getComment());

                if(feedbackDTO.getUserAvatar().contains("https")){
                    RequestOptions requestOptions = new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(25));
                    Glide.with(getContext()).load(feedbackDTO.getUserAvatar()).apply(requestOptions).into(userAvatar);
                }
            }
        }
    }

}

