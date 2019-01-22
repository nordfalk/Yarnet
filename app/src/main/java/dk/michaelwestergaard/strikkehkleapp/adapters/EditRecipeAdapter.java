package dk.michaelwestergaard.strikkehkleapp.adapters;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import java.util.List;

import dk.michaelwestergaard.strikkehkleapp.activities.EditRecipe;

public class EditRecipeAdapter extends AbstractFragmentStepAdapter {

    private List<EditRecipe.EditRecipeAdapterStepperInfo> stepFragments;

    public EditRecipeAdapter(@NonNull FragmentManager fm, @NonNull Context context, List<EditRecipe.EditRecipeAdapterStepperInfo> fragments) {
        super(fm, context);
        stepFragments = fragments;
    }

    @Override
    public Step createStep(int position) {
        return (Step) stepFragments.get(position).getFragment();
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        StepViewModel.Builder stepViewModelBuilder = new StepViewModel.Builder(context);

        stepViewModelBuilder.setTitle(stepFragments.get(position).getTitle());

        return stepViewModelBuilder.create();
    }

    @Override
    public int getCount() {
        return stepFragments.size();
    }

}
