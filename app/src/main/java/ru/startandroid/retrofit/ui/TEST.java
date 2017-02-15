package ru.startandroid.retrofit.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import ru.startandroid.retrofit.R;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TEST extends AppCompatActivity {

    TextView tv;

    RecyclerView rvTest;
    Adapter adapter;
    Observable observable;

    Button btnRx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        btnRx = (Button) findViewById(R.id.btn_rx);
//        btnRx.setOnClickListener(v -> ShowToast());

//        RxView.clicks(btnRx).subscribe(v -> {ShowToast();});

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




        observable = Observable.interval(3, TimeUnit.SECONDS)
                .map(o -> "Hey " + o).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }


    private void ShowToast(){
        Toast.makeText(this, "Button", Toast.LENGTH_SHORT).show();
    }
    ArrayList<Integer> arrayList;

    public void Change(View view) {

        observable.subscribe((Observable.OnSubscribe) o -> {
            tv.setText(o.toString() + "\n");

        });



    }


    private void someMethod(){
        adapter.notifyItemMoved(3, 0);

    }


    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

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
