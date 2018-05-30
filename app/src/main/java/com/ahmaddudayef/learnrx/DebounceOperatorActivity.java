package com.ahmaddudayef.learnrx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class DebounceOperatorActivity extends AppCompatActivity {
    /**
     * Debounce operators emits items only when a specified timespan is passed. This operator is very useful when the Observable is rapidly emitting items but you are only interested in receiving them in timely manner.
     *
     * Consider an example of performing an instant search. When user types the search query, the query will be sent to server and the result will be displayed. The request will be sent to server every time user types a character which makes unnecessary calls to server with incomplete search word. Instead, we can wait for a certain time period until user types proper search keyword then we can send the request to server.
     * */
    private static final String TAG = DebounceOperatorActivity.class.getSimpleName();

    private CompositeDisposable disposable = new CompositeDisposable();
    private Unbinder unbinder;

    @BindView(R.id.input_search)
    EditText inputSearch;
    @BindView(R.id.txt_search_string)
    TextView txtSearchString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debounce_operator);
        unbinder = ButterKnife.bind(this);


        /**
         * Let’s say user want to search for `RxJava`. Without debounce, there would be multiple calls to server for keywords `R`, `Rx`, `RxJ` and so on. Instead we can give user a time period say 300 milli sec to type and send the query the to server. Most probably user can type upto `RxJ` in the given time period.
         * Here, we have used debounce(300, TimeUnit.MILLISECONDS) which means the query will be emitted every 300 milli seconds
         * */
        disposable.add(
                RxTextView.textChangeEvents(inputSearch)
                        .skipInitialValue()
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(searchQuery()));

        txtSearchString.setText("Search query will be accumulated every 300 milli sec");
    }

    private DisposableObserver<TextViewTextChangeEvent> searchQuery() {
        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                Log.d(TAG, "search string: " + textViewTextChangeEvent.text().toString());

                txtSearchString.setText("Query: " + textViewTextChangeEvent.text().toString());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        disposable.clear();
    }
}
