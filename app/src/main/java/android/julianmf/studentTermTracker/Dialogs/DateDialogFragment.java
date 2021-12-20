package android.julianmf.studentTermTracker.Dialogs;

import android.app.Dialog;
import android.app.FragmentManager;
import android.julianmf.studentTermTracker.R;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DateDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.input_restriction);
        builder.setMessage(R.string.date_restriction);
        builder.setPositiveButton(R.string.ok, null);
        return builder.create();
    }
}
