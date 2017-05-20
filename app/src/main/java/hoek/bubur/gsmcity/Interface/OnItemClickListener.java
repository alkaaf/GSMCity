package hoek.bubur.gsmcity.Interface;

import android.view.View;

public interface OnItemClickListener<T> {
    void onItemClick(View v, int pos, T data);
}