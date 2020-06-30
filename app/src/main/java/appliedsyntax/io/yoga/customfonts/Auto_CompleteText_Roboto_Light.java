package appliedsyntax.io.yoga.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class Auto_CompleteText_Roboto_Light extends AutoCompleteTextView {
    public Auto_CompleteText_Roboto_Light(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Auto_CompleteText_Roboto_Light(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Auto_CompleteText_Roboto_Light(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
            setTypeface(tf);
        }
    }

}
