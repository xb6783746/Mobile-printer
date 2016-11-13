package com.mobileprinter.View.Host;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.mobileprinter.Interfaces.ScreenHost;
import com.mobileprinter.Presenter.EditScreen.ImageEditorPresenterImpl;
import com.mobileprinter.R;
import com.mobileprinter.Router.EditScreen.EditScreenRouterImpl;
import com.mobileprinter.View.EditScreen.ImageEditorView;

public class MainActivity extends AppCompatActivity implements ScreenHost {



    private FrameLayout host;
    private Fragment current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        host = (FrameLayout)findViewById(R.id.host);

        start();
    }

    @Override
    public void attach(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        if(current != null){
            fragmentTransaction.remove(current);
        }

        current = fragment;

        fragmentTransaction.add(R.id.host, fragment).commit();
    }

    private void start(){
        ImageEditorView fragment = new ImageEditorView();
        EditScreenRouterImpl router = new EditScreenRouterImpl(this, this);

        ImageEditorPresenterImpl presenter = new ImageEditorPresenterImpl(fragment, this, router);
        fragment.setPresenter(presenter);

        attach(fragment);
        presenter.start();
    }
}
