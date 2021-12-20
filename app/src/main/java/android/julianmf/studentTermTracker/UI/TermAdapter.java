package android.julianmf.studentTermTracker.UI;

import android.content.Context;
import android.content.Intent;
import android.julianmf.studentTermTracker.Entities.Term;
import android.julianmf.studentTermTracker.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder>{

    class TermViewHolder extends RecyclerView.ViewHolder {
        private final TextView termItemView2;
        private final TextView termItemView3;
        private final TextView termItemView4;

        private TermViewHolder(View itemView){
            super (itemView);
            termItemView2 = itemView.findViewById(R.id.termTextView2);
            termItemView3 = itemView.findViewById(R.id.termTextView3);
            termItemView4 = itemView.findViewById(R.id.termTextView4);
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Term currentTerm = mTerms.get(position);
                    Intent intent = new Intent(context, TermDetails.class);
                    intent.putExtra("termId", currentTerm.getTermId());
                    intent.putExtra("termName", currentTerm.getTermName());
                    intent.putExtra("termStartDate", currentTerm.getStartDate());
                    intent.putExtra("termEndDate", currentTerm.getEndDate());
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Term> mTerms;
    private final Context context;
    private final LayoutInflater mInflater;

    public TermAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public TermAdapter.TermViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.container_terms_list, parent, false);
        return new TermViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull TermAdapter.TermViewHolder holder, int position) {
        if(mTerms != null){
            Term current = mTerms.get(position);
            holder.termItemView2.setText(current.getTermName());
            holder.termItemView3.setText("From: " + current.getStartDate());
            holder.termItemView4.setText("To: " + current.getEndDate());
        }
        else{
            holder.termItemView2.setText("No name for this term");
            holder.termItemView3.setText("No start date for this term");
            holder.termItemView4.setText("No end date for this term");
        }
    }

    public void setTerms(List<Term> Terms){
        mTerms = Terms;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mTerms.size();
    }
}
