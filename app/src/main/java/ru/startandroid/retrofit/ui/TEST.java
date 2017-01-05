package ru.startandroid.retrofit.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import ru.startandroid.retrofit.R;

public class TEST extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

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

        tv.setText(arrayList.size() + "\n");

        int temp = arrayList.get(3);
        arrayList.remove(3);
        arrayList.add(0, temp);

        for (int a : arrayList) {
            tv.append(" " + a + " ");
        }


    }
}
