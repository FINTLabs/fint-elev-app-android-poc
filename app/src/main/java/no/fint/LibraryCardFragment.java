package no.fint;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LibraryCardFragment extends Fragment implements View.OnClickListener {
    LinearLayout linearLayoutBottom;
    private OnFragmentInteractionListener mListener;

    public LibraryCardFragment() {
    }

    public static LibraryCardFragment newInstance() {
        return new LibraryCardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library_card, container, false);
        linearLayoutBottom = view.findViewById(R.id.library_card_fragment_bottom);
        linearLayoutBottom.setOnClickListener(this);
        TextView verifiedBusCardText = view.findViewById(R.id.library_card_verified_text);
        AnimatorSet glow = (AnimatorSet) AnimatorInflater.loadAnimator(view.getContext(), R.animator.verified_animation);
        glow.setTarget(verifiedBusCardText);
        glow.start();

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == linearLayoutBottom.getId()) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
