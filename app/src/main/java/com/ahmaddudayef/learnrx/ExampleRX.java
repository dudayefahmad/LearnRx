package com.ahmaddudayef.learnrx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ExampleRX extends AppCompatActivity implements View.OnClickListener {

    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_rx);

        btn1 = (Button) findViewById(R.id.basicObaservable);
        btn2 = (Button) findViewById(R.id.basicDisposable);
        btn3 = (Button) findViewById(R.id.basicOperator);
        btn4 = (Button) findViewById(R.id.basicMultipleObaserver);
        btn5 = (Button) findViewById(R.id.basicCusotmDataType);
        btn6 = (Button) findViewById(R.id.DeepObservalbes);
        btn7 = (Button) findViewById(R.id.DeepAboutMap);
        btn8 = (Button) findViewById(R.id.Bufffer_Debounce);
        btn9 = (Button) findViewById(R.id.concat_merge);
        btn10 = (Button) findViewById(R.id.rx_math);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn10.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.basicObaservable:
                intent = new Intent(this, BasicObservableObserver.class);
                break;
            case R.id.basicDisposable:
                intent = new Intent(this, ExampleDisposable.class);
                break;
            case R.id.basicOperator:
                intent = new Intent(this, ExampleOperator.class);
                break;
            case R.id.basicMultipleObaserver:
                intent = new Intent(this, MultipleObserver.class);
                break;
            case R.id.basicCusotmDataType:
                intent = new Intent(this, CustomDataType.class);
                break;
            case R.id.DeepObservalbes:
                intent = new Intent(this, DeepAboutObservables.class);
                break;
            case R.id.DeepAboutMap:
                intent = new Intent(this, DeepAboutMap.class);
                break;
            case R.id.Bufffer_Debounce:
                intent = new Intent(this, Buffer_Debounce.class);
                break;
            case R.id.concat_merge:
                intent = new Intent(this, Concat_Merge.class);
                break;
            case R.id.rx_math:
                intent = new Intent(this, RxMath.class);
                break;
        }
        startActivity(intent);
    }
}
