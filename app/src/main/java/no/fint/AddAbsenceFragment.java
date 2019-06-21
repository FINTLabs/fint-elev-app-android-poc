package no.fint;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

public class AddAbsenceFragment extends Fragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener;
    private NumberPicker absenceDayPicker, absenceHourPicker;
    private Button absenceAddButton;
    View view;

    public AddAbsenceFragment() {
    }

    public static AddAbsenceFragment newInstance() {
        return new AddAbsenceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_absence, container, false);
        absenceDayPicker = view.findViewById(R.id.number_picker_absence_days);
        absenceHourPicker = view.findViewById(R.id.number_picker_absence_hours);
        AbsenceActivity.setNumberPickerColor(absenceDayPicker, Color.WHITE);
        AbsenceActivity.setNumberPickerColor(absenceHourPicker, Color.WHITE);
        absenceDayPicker.setMaxValue(5);
        absenceDayPicker.setMinValue(0);
        absenceDayPicker.setDisplayedValues(new String[]{"0 dager ", "1 dager", "2 dager", "3 dager", "4 dager", "5 dager"});
        absenceDayPicker.setValue(1);
        absenceHourPicker.setMaxValue(5);
        absenceHourPicker.setMinValue(0);
        absenceHourPicker.setDisplayedValues(new String[]{"0 timer ", "1 time", "2 timer", "3 timer", "4 timer", "5 timer"});
        absenceHourPicker.setValue(1);
        absenceAddButton = view.findViewById(R.id.absence_add_button);
        absenceAddButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == absenceAddButton.getId()) {
            AbsenceActivity activity = (AbsenceActivity) getActivity();
            activity.updateAbsence(absenceDayPicker.getValue(), absenceHourPicker.getValue());
            activity.fab.setRotation(0);
            activity.onBackPressed();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
