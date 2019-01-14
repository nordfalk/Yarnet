package dk.michaelwestergaard.strikkehkleapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;

import java.util.List;

public class CreateRecipeAdapter extends AbstractFragmentStepAdapter {

    private List<Fragment> stepFragments;

    public CreateRecipeAdapter(@NonNull FragmentManager fm, @NonNull Context context, List<Fragment> fragments) {
        super(fm, context);
        stepFragments = fragments;
    }

    @Override
    public Step createStep(int position) {


        return (Step) stepFragments.get(position);
    }

    @Override
    public int getCount() {
        return stepFragments.size();
    }
}
