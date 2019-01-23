package dk.michaelwestergaard.strikkehkleapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.DAO.RecipeDAO;
import dk.michaelwestergaard.strikkehkleapp.DAO.UserDAO;
import dk.michaelwestergaard.strikkehkleapp.DTO.RecipeDTO;
import dk.michaelwestergaard.strikkehkleapp.DTO.UserDTO;
import dk.michaelwestergaard.strikkehkleapp.MainSingleton;
import dk.michaelwestergaard.strikkehkleapp.R;
import dk.michaelwestergaard.strikkehkleapp.adapters.RecipeAdapter;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    ImageView avatar;
    TextView name;
    Button btn;
    ImageView backBtn;
    ImageView drawerBtn;
    private RecyclerView recyclerView;

    List<RecipeDTO> myRecipes;

    private RecipeDAO recipeDAO = new RecipeDAO();
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final Context myContext = this;

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

        btn = findViewById(R.id.editProfileBtn);
        btn.setOnClickListener(this);

        avatar = findViewById(R.id.avatar);
        name = findViewById(R.id.profileName);

        recyclerView = findViewById(R.id.recyclerViewProf);

        UserDTO user = MainSingleton.getInstance().getUser();
        name.setText(user.getFirst_name() + " " + user.getLast_name());
        if(user.getAvatar().contains("https")) {
            RequestOptions requestOptions = new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(50));
            Glide.with(Profile.this).load(user.getAvatar()).apply(requestOptions).into(avatar);
        }

        final List<RecipeDTO> recipes = new ArrayList<RecipeDTO>();

        recipeDAO.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipes.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    recipes.add(snapshot.getValue(RecipeDTO.class));
                }

                final FirebaseAuth auth = FirebaseAuth.getInstance();
                final List<UserDTO> users = new ArrayList<>();

                userDAO.getReference().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        users.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            users.add(snapshot.getValue(UserDTO.class));
                        }

                        UserDTO actualUser = new UserDTO();
                        for (UserDTO user : users) {
                            if (user.getUserID().equals(auth.getCurrentUser().getUid())) {
                                actualUser = user;
                            }
                        }

                        myRecipes = sortRecipes("my", recipes, actualUser);

                        RecipeAdapter adapter = new RecipeAdapter(myRecipes);
                        recyclerView.setAdapter(adapter);

                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(myContext, 3);
                        recyclerView.setLayoutManager(layoutManager);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private List<RecipeDTO> sortRecipes(String sortStyle, List<RecipeDTO> recipes, UserDTO user) {
        List<RecipeDTO> recipesToShow = new ArrayList<RecipeDTO>();

        switch (sortStyle) {
            case "my":
                if(user != null) {
                    for(RecipeDTO recipe : recipes){
                        if(recipe.getUserID().equals(user.getUserID()))
                            recipesToShow.add(recipe);
                    }
                } else {
                    System.out.println("Error sorting recipes: User not found!");
                }
                break;

            default:
                System.out.print("Error sorting recipes: Unknown sortStyle!");
                break;
        }
        return recipesToShow;
    }

    @Override
    public void onClick(View view) {
        if (view == btn) {
            Intent intent = new Intent(Profile.this, EditPage.class);
            startActivity(intent);
        }
    }
}
