package android.julianmf.studentTermTracker.UI;

import android.content.Context;
import android.content.Intent;
import android.julianmf.studentTermTracker.Entities.Assessment;
import android.julianmf.studentTermTracker.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {

    class AssessmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView assessmentItemView2;
        private final TextView assessmentItemView3;
        private final TextView assessmentItemView4;

        private AssessmentViewHolder(View itemView){
            super (itemView);
            assessmentItemView2 = itemView.findViewById(R.id.assessmentTextView2);
            assessmentItemView3 = itemView.findViewById(R.id.assessmentTextView3);
            assessmentItemView4 = itemView.findViewById(R.id.assessmentTextView4);
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Assessment currentAssessment = mAssessments.get(position);
                    Intent intent = new Intent(context, AssessmentDetails.class);
                    intent.putExtra("assessmentId", currentAssessment.getAssessmentID());
                    intent.putExtra("assessmentType", currentAssessment.getType());
                    intent.putExtra("assessmentTitle", currentAssessment.getTitle());
                    intent.putExtra("assessmentStartDate", currentAssessment.getStartDate());
                    intent.putExtra("assessmentEndDate", currentAssessment.getEndDate());
                    intent.putExtra("courseId", currentAssessment.getCourseID());
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Assessment> mAssessments;
    private final Context context;
    private final LayoutInflater mInflater;

    public AssessmentAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public AssessmentAdapter.AssessmentViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.container_assessments_list, parent, false);
        return new AssessmentAdapter.AssessmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull AssessmentAdapter.AssessmentViewHolder holder, int position) {
        if(mAssessments != null){
            Assessment current = mAssessments.get(position);
            holder.assessmentItemView2.setText(current.getTitle());
            holder.assessmentItemView3.setText("From: " + current.getStartDate());
            holder.assessmentItemView4.setText("To: " + current.getEndDate());
        }
        else{
            holder.assessmentItemView2.setText("No name for this term");
            holder.assessmentItemView3.setText("No start date for this term");
            holder.assessmentItemView4.setText("No end date for this term");
        }
    }

    public void setAssessments(List<Assessment> Assessments){
        mAssessments = Assessments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mAssessments.size();
    }
}
