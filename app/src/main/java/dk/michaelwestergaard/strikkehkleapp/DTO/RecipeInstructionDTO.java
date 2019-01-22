package dk.michaelwestergaard.strikkehkleapp.DTO;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class RecipeInstructionDTO implements Parcelable {

    private String title;
    private List<String> instructions;

    public RecipeInstructionDTO(){}

    public RecipeInstructionDTO(String title, List<String> instructions) {
        this.title = title;
        this.instructions = instructions;
    }

    public RecipeInstructionDTO(Parcel in) {
        this.title = in.readString();
        in.readStringList(this.instructions);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        return "RecipeInstructionDTO{" +
                "title='" + title + '\'' +
                ", instructions=" + instructions +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeStringList(instructions);
    }

    public static final Parcelable.Creator<RecipeInstructionDTO> CREATOR = new Parcelable.Creator<RecipeInstructionDTO>() {

        @Override
        public RecipeInstructionDTO createFromParcel(Parcel in) {
            return new RecipeInstructionDTO(in);
        }

        @Override
        public RecipeInstructionDTO[] newArray(int size) {
            return new RecipeInstructionDTO[size];
        }
    };
}