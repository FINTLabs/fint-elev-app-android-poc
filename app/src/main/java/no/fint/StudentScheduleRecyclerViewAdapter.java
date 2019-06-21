package no.fint;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentScheduleRecyclerViewAdapter extends RecyclerView.Adapter<StudentScheduleRecyclerViewAdapter.SubjectViewHolder> {
    ArrayList<Subject> subjects;
    Context context;

    StudentScheduleRecyclerViewAdapter(ArrayList<Subject> subjects, Context context) {
        this.subjects = subjects;
        this.context = context;
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView subjectHeader;
        TextView subjectTime;
        TextView subjectTeacher;
        RelativeLayout subjectBackground;

        SubjectViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.subject_card_view);
            subjectHeader = itemView.findViewById(R.id.subject_header);
            subjectTime = itemView.findViewById(R.id.subject_time_text_view);
            subjectTeacher = itemView.findViewById(R.id.subject_teacher_text_view);
            subjectBackground = itemView.findViewById(R.id.subject_schedule_background);
        }
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.student_schedule_subjects_cardview, viewGroup, false);
        SubjectViewHolder doorHolderView = new SubjectViewHolder(v);
        return doorHolderView;
    }

    @Override
    public void onBindViewHolder(@NonNull final SubjectViewHolder doorViewHolder, final int i) {
        doorViewHolder.subjectHeader.setText(subjects.get(i).getName());
        doorViewHolder.subjectTime.setText(String.format("Kl. %s-%s", subjects.get(i).getStartTime(), subjects.get(i).getEndTime()));
        doorViewHolder.subjectTeacher.setText(String.format("Lærer\n%s", subjects.get(i).getTeacherName()));
        doorViewHolder.subjectBackground.setBackgroundColor(
                (subjects.get(i).getName().equals("Norsk")) ?
                        context.getColor(R.color.colorSubjectNorwegian) :
                        (subjects.get(i).getName().equals("Matte")) ?
                                context.getColor(R.color.colorSubjectMath) :
                                context.getColor(R.color.colorSubjectEnglish)
        );
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
