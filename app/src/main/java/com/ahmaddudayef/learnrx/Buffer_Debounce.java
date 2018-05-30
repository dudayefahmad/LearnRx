package com.ahmaddudayef.learnrx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Buffer_Debounce extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = Buffer_Debounce.class.getSimpleName();
    Button buffer, debounce, bufferAndroid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffer__debounce);
        buffer = (Button) findViewById(R.id.buffer);
        debounce = (Button) findViewById(R.id.debounce);
        bufferAndroid = (Button) findViewById(R.id.bufferAndroid);

        buffer.setOnClickListener(this);
        debounce.setOnClickListener(this);
        bufferAndroid.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buffer:
                bufferSample();
                break;
            case R.id.debounce:
                Intent intent1 = new Intent(this, DebounceOperatorActivity.class);
                startActivity(intent1);
                break;
            case R.id.bufferAndroid:
                Intent intent = new Intent(this, BufferOperatorActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void bufferSample() {
        /**
         * Buffer gathers items emitted by an Observable into batches and emit the batch instead of emitting one item at a time.
         * Below, we have an Observable that emits integers from 1-9. When buffer(3) is used, it emits 3 integers at a time.
         * */
        Observable<Integer> integerObservable = Observable.just(1, 2, 3, 4,
                5, 6, 7, 8, 9);

        integerObservable
                .subscribeOn(Schedulers.io())
                .buffer(3)
                .subscribe(new Observer<List<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Integer> integers) {
                        Log.d(TAG, "onNext");
                        for (Integer integer : integers) {
                            Log.d(TAG, "Item: " + integer);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "All items emitted!");
                    }
                });
    }
}
