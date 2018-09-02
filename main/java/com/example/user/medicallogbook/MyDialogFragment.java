package com.example.user.medicallogbook;

/**
 * Created by user on 1/1/2016.
 */
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@SuppressLint("NewApi")
public class MyDialogFragment extends DialogFragment implements
        OnItemClickListener {

    ListView mylist;
    Communicator communicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_fragment, null, false);
        mylist = (ListView) view.findViewById(R.id.dialog_list);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

        if (activity instanceof Communicator) {
            communicator = (Communicator) getActivity();
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implemenet MyListFragment.communicator");
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.dialog_items,
                android.R.layout.simple_list_item_1);

        mylist.setAdapter(adapter);
        mylist.setOnItemClickListener(this);

    }

    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        dismiss();
        communicator.message(position);

    }

    public interface Communicator {
        public void message(int data);
    }

}