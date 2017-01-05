package ru.startandroid.retrofit.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import ru.startandroid.retrofit.R;

public class TEST extends AppCompatActivity {

    TextView tv;

    RecyclerView rvTest;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        rvTest = (RecyclerView) findViewById(R.id.rv_test);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvTest.setLayoutManager(mLayoutManager);
        adapter = new Adapter();
        rvTest.setAdapter(adapter);

        arrayList = new ArrayList();
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        arrayList.add(4);
        arrayList.add(5);

        tv = (TextView) findViewById(R.id.tv);

        for (int a : arrayList) {
            tv.append("" + a + " ");
        }

    }

    ArrayList<Integer> arrayList;

    public void Change(View view) {

        adapter.notifyItemMoved(3, 0);
      /*  tv.setText(arrayList.size() + "\n");

        int temp = arrayList.get(3);
        arrayList.remove(3);
        arrayList.add(0, temp);

        for (int a : arrayList) {
            tv.append(" " + a + " ");
        }*/


    }


    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView tvListId1;

            public ViewHolder(View v) {
                super(v);
                tvListId1 = (TextView) itemView.findViewById(R.id.tv_test);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflatedView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_test, parent, false);
            return new ViewHolder(inflatedView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tvListId1.setText(arrayList.get(position) + "");
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }
}
