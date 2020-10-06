package com.example.bobo_hello.UI.SideNavigationItems.AppInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.bobo_hello.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AppInfoFragment extends Fragment {

    private FloatingActionButton sendEmailBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_info, container, false);
    }

    private void setOnBtnClickAction() {
            sendEmailBtn.setOnClickListener(view -> {
                Intent Email = new Intent(Intent.ACTION_SEND);
                Email.setType("text/email");
                Email.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{"kc.bezshliakh@gmail.com"});
                Email.putExtra(Intent.EXTRA_SUBJECT,
                        "Add your Subject");
                startActivity(Intent.createChooser(Email, "Send Feedback:"));
            });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setOnBtnClickAction();

    }

    private void initViews(View view) {
        sendEmailBtn = view.findViewById(R.id.fab_send_email);
    }

}
