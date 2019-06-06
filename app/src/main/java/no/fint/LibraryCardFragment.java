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
    LinearLayout linearLayoutTop;
    LinearLayout linearLayoutBottom;
    private OnFragmentInteractionListener mListener;

    public LibraryCardFragment() {
        // Required empty public constructor
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
        //linearLayoutTop = view.findViewById(R.id.library_card_fragment_top);
        linearLayoutBottom = view.findViewById(R.id.library_card_fragment_bottom);
        linearLayoutBottom.setOnClickListener(this);
        //linearLayoutTop.setOnClickListener(this);

        TextView verifiedBusCardText = view.findViewById(R.id.library_card_verified_text);
        AnimatorSet glow = (AnimatorSet) AnimatorInflater.loadAnimator(view.getContext(), R.animator.verified_animation);
        glow.setTarget(verifiedBusCardText);
        glow.start();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == linearLayoutTop.getId() || v.getId() == linearLayoutBottom.getId()){
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
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
}
