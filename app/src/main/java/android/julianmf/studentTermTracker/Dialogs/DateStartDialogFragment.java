package android.julianmf.studentTermTracker.Dialogs;

import android.app.Dialog;
import android.julianmf.studentTermTracker.R;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DateStartDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.invalid_date);
        builder.setMessage(R.string.date_order);
        builder.setPositiveButton(R.string.ok, null);
        return builder.create();
    }
}
