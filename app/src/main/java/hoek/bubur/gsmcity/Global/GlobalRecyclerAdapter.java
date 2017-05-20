package hoek.bubur.gsmcity.Global;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import hoek.bubur.gsmcity.Interface.OnItemClickListener;

/**
 * Created by dalbo on 5/20/2017.
 */

public abstract class GlobalRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    List<T> data;
    OnItemClickListener<T> listener;

    public GlobalRecyclerAdapter(List<T> data) {
        this.data = data;
        initListener();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onBindDataToHolder(holder, position, data.get(position));
    }

    public abstract void onBindDataToHolder(VH holder, int pos, T data);

    public GlobalRecyclerAdapter() {
        data = new ArrayList<>();
        initListener();
    }

    public void initListener() {
        listener = new OnItemClickListener<T>() {
            @Override
            public void onItemClick(View v, int pos, T data) {

            }
        };
    }


    public void remove(T d) {
        this.data.remove(d);
    }

    public void remove(int i) {
        this.data.remove(i);
    }

    public void replaceData(List<T> d) {
        this.data = d;
    }

    public void clearData() {
        this.data.clear();
    }

    public void addData(List<T> d) {
        this.data.addAll(d);
    }

    public void addData(T d) {
        this.data.add(d);
    }

    public T getData(int i) {
        return data.get(i);
    }

    public List<T> getData() {
        return data;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return listener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
