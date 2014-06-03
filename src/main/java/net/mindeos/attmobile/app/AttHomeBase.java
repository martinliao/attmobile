package net.mindeos.attmobile.app;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

/**
 * Abstract class for defining all the common aspects in the
 * AttHome activities
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public abstract class AttHomeBase extends AttControlBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.red0)));
    }

}
