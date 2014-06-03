// This file is part of AttMobile - http://att.mindeos.net/
//
// AttMobile is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// AttMobile is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with AttMobile. If not, see <http://www.gnu.org/licenses/>.

package net.mindeos.attmobile.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.mindeos.attmobile.app.R;

/**
 * Class to build custom dialogs
 * @author José Luis Antúnez <jantunez@xtec.cat>
 */
public class CustomDialogBuilder extends AlertDialog.Builder {
    /**
     * Instantiates a new CustomDialogBuilder.
     *
     * @param context the context in which the builder will work
     */
    public CustomDialogBuilder(Context context) {
        super(context);
    }

    @Override
    public AlertDialog show() {
        AlertDialog d = super.show();

        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = d.findViewById(dividerId);
        divider.setVisibility(View.GONE);

        int textViewId = d.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
        TextView tv = (TextView) d.findViewById(textViewId);
        tv.setTextColor(d.getContext().getResources().getColor(R.color.white));

        int title_templateid = d.getContext().getResources().getIdentifier("android:id/title_template", null, null);
        View title_template = (View) d.findViewById(title_templateid);
        ((ViewGroup.MarginLayoutParams)title_template.getLayoutParams()).setMargins(0,0,0,0);
        title_template.setPadding(20,0,20,0);
        title_template.setBackground(d.getContext().getResources().getDrawable(R.color.gray2));

        return d;

    }

}


