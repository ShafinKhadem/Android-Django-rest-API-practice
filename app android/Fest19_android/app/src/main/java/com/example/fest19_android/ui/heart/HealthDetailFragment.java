package com.example.fest19_android.ui.heart;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.fest19_android.R;
import com.example.fest19_android.data.HealthRepository;
import com.example.fest19_android.data.model.HealthEntry;
import com.example.fest19_android.ui.login.LoginActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;

/**
 * A fragment representing a single HealthEntry detail screen.
 * This fragment is either contained in a {@link HealthListActivity}
 * in two-pane mode (on tablets) or a {@link HealthDetailActivity}
 * on handsets.
 */
public class HealthDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    static final String ARG_ITEM_ID = "item_id";

    private String itemId;

    /**
     * The dummy content this fragment is presenting.
     */
//    private HealthEntry mItem = null;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HealthDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {   // it's a fragment, you can't initialize any graphical element here, use
                                                        // onCreateView for that. For modifying activity UI elements, use onActivityStarted.
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(ARG_ITEM_ID)) {
            itemId = arguments.getString(ARG_ITEM_ID);
            Log.d(LoginActivity.TAG, "detail in: "+itemId);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = this.getActivity();
        assert activity != null;
        CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(itemId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.health_detail, container, false);

        // Show the dummy content as text in a TextView.
//        if (mItem != null) {
//            ((TextView) rootView.findViewById(R.id.health_detail)).setText(mItem.toString());
//        }


        // Doesn't make sense to make viewmodel just for a proxy function.
        final LiveData<HealthEntry> itemById = HealthRepository.getInstance(requireActivity()).getItemById(itemId);
        assert itemById!=null;
        itemById.observe(getViewLifecycleOwner(), new Observer<HealthEntry>() {
            @Override
            public void onChanged(HealthEntry healthEntry) {
                if (itemById.getValue()==null) return;
                ((TextView) rootView.findViewById(R.id.health_detail)).setText(itemById.getValue().toString());
            }
        });

        return rootView;
    }
}
