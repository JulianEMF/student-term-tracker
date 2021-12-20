package android.julianmf.studentTermTracker.UI;

import android.content.Context;
import android.content.Intent;
import android.julianmf.studentTermTracker.Entities.Course;
import android.julianmf.studentTermTracker.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    class CourseViewHolder extends RecyclerView.ViewHolder{
        private final TextView courseItemView2;
        private final TextView courseItemView3;
        private final TextView courseItemView4;

        private CourseViewHolder(View itemView){
            super (itemView);
            courseItemView2 = itemView.findViewById(R.id.courseTextView2);
            courseItemView3 = itemView.findViewById(R.id.courseTextView3);
            courseItemView4 = itemView.findViewById(R.id.courseTextView4);
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Course currentCourse = mCourses.get(position);
                    Intent intent = new Intent(context, CourseDetails.class);
                    intent.putExtra("courseId", currentCourse.getCourseID());
                    intent.putExtra("title", currentCourse.getTitle());
                    intent.putExtra("courseStartDate", currentCourse.getStartDate());
                    intent.putExtra("courseEndDate", currentCourse.getEndDate());
                    intent.putExtra("status", currentCourse.getStatus());
                    intent.putExtra("instructorName", currentCourse.getInstructorName());
                    intent.putExtra("instructorPhone", currentCourse.getInstructorPhone());
                    intent.putExtra("instructorEmail", currentCourse.getInstructorEmail());
                    intent.putExtra("termID", currentCourse.getTermID());
                    intent.putExtra("notes", currentCourse.getNotes());
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Course> mCourses;
    private final Context context;
    private final LayoutInflater mInflater;

    public CourseAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public CourseAdapter.CourseViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.container_courses_list, parent, false);
        return new CourseAdapter.CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull CourseAdapter.CourseViewHolder holder, int position) {
        if(mCourses != null){
            Course current = mCourses.get(position);
            holder.courseItemView2.setText(current.getTitle());
            holder.courseItemView3.setText("From: " + current.getStartDate());
            holder.courseItemView4.setText("To: " + current.getEndDate());
        }
        else{
            holder.courseItemView2.setText("No name for this course");
            holder.courseItemView3.setText("No start date for this course");
            holder.courseItemView4.setText("No end date for this course");
        }
    }

    public void setCourses(List<Course> Courses){
        mCourses = Courses;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }
}
