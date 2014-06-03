package net.mindeos.attmobile.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class AboutFragment extends Fragment implements View.OnClickListener{

    /**
     * Instantiates a new Placeholder fragment.
     */
    public AboutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        rootView.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        getActivity().finish();
    }
}