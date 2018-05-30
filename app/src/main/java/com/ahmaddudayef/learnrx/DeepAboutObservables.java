package com.ahmaddudayef.learnrx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class DeepAboutObservables extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = DeepAboutObservables.class.getSimpleName();

    private Disposable disposable;

    Button btn1, btn2, btn3, btn4, btn5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_about_observables);

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);

        observable_observer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
                observable_observer();
                break;
            case R.id.btn2:
                single_singleObserver();
                break;
            case R.id.btn3:
                maybe_maybeObserver();
                break;
            case R.id.btn4:
                completable_Observer();
                break;
            case R.id.btn5:
                flowable_Observer();
                break;
        }
    }

    private void flowable_Observer() {
        /**
         * Simple example of Flowable just to show the syntax
         * the use of Flowable is best explained when used with BackPressure
         * Read the below link to know the best use cases to use Flowable operator
         * https://github.com/ReactiveX/RxJava/wiki/What%27s-different-in-2.0#when-to-use-flowable
         * -
         * Flowable : SingleObserver
         */
        Flowable<Integer> flowableObservable = getFlowableObservable();

        SingleObserver<Integer> observer = getFlowableObserver();

        flowableObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .reduce(0, new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer result, Integer number) throws Exception {
                        return result + number;
                    }
                })
                .subscribe(observer);
    }

    private SingleObserver<Integer> getFlowableObserver() {
        return new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
                disposable = d;
            }

            @Override
            public void onSuccess(Integer integer) {
                Log.d(TAG, "onSuccess: " + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }
        };
    }

    private Flowable<Integer> getFlowableObservable() {
        return Flowable.range(1, 100);
    }

    private void completable_Observer() {
        /**
         * Completable won't emit any item, instead it returns
         * Success or failure state
         * Consider an example of making a PUT request to server to update
         * something where you are not expecting any response but the
         * success status
         * -
         * Completable : CompletableObserver
         */

        Note note = new Note(1, "Home Work !");

        Completable completableObservable = updateNote(note);
        CompletableObserver completable_Observer = completableObserver();

        completableObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(completable_Observer);
    }

    private CompletableObserver completableObserver() {
        return new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
                disposable = d;
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: Note updated successfully!");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }
        };
    }

    /**
     * Assume this making PUT request to server to update the Note
     */
    private Completable updateNote(Note note) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                if (!emitter.isDisposed()){
                    Thread.sleep(1000);
                    emitter.onComplete();
                }
            }
        });
    }

    private void maybe_maybeObserver() {
        /**
         * Consider an example getting a note from db using ID
         * There is possibility of not finding the note by ID in the db
         * In this situation, MayBe can be used
         * -
         * Maybe : MaybeObserver
         */

        Maybe<Note> noteObservable = getNoteMaybeObservable();
        MaybeObserver<Note> noteObserver = getNoteMaybeObserver();

        noteObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(noteObserver);
    }

    private MaybeObserver<Note> getNoteMaybeObserver() {
        return new MaybeObserver<Note>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(Note note) {
                Log.d(TAG, "onSuccess: " + note.getNote());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete");
            }
        };
    }

    /**
     * Emits optional data (0 or 1 emission)
     * But for now it emits 1 Note always
     */
    private Maybe<Note> getNoteMaybeObservable() {
        return Maybe.create(new MaybeOnSubscribe<Note>() {
            @Override
            public void subscribe(MaybeEmitter<Note> emitter) throws Exception {
                Note note = new Note(1, "Call Brother !");
                if (!emitter.isDisposed()){
                    emitter.onSuccess(note);
                }
            }
        });
    }

    private void single_singleObserver() {
        /**
         * Single Observable emitting single Note
         * Single Observable is more useful in making network calls
         * where you expect a single response object to be emitted
         * -
         * Single : SingleObserver
         */
        Single<Note> noteObservable = getNoteObservable();

        SingleObserver<Note> singleObserver = getSingleObserver();

        noteObservable.observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(singleObserver);
    }

    private SingleObserver<Note> getSingleObserver() {
        return new SingleObserver<Note>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
                disposable = d;
            }

            @Override
            public void onSuccess(Note note) {
                Log.d(TAG, "onSuccess: " + note.getNote());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }
        };
    }

    private Single<Note> getNoteObservable() {
        return Single.create(new SingleOnSubscribe<Note>() {
            @Override
            public void subscribe(SingleEmitter<Note> emitter) throws Exception {
                Note note = new Note(1, "Buy milk !");
                emitter.onSuccess(note);
            }
        });
    }

    private void observable_observer() {
        /**
         * Simple Observable emitting multiple Notes
         * -
         * Observable : Observer
         */

        Observable<Note> noteObservable = getNotesObservable();

        Observer<Note> noteObserver = getNotesObserver();

        noteObservable.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeWith(noteObserver);

    }

    private Observer<Note> getNotesObserver() {
        return new Observer<Note>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
                disposable = d;
            }

            @Override
            public void onNext(Note note) {
                Log.d(TAG, "onNext: " + note.getNote());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        };
    }

    private Observable<Note> getNotesObservable() {
        final List<Note> notes = prepareNotes();

        return Observable.create(new ObservableOnSubscribe<Note>() {
            @Override
            public void subscribe(ObservableEmitter<Note> emitter) throws Exception {
                for (Note note : notes){
                    if (!emitter.isDisposed()){
                        emitter.onNext(note);
                    }
                }

                //all note are emitted
                if (!emitter.isDisposed()){
                    emitter.onComplete();
                }
            }
        });
    }

    private List<Note> prepareNotes(){
        List<Note> notes = new ArrayList<>();
        notes.add(new Note(1, "Buy tooth paste!"));
        notes.add(new Note(2, "Call brother!"));
        notes.add(new Note(3, "Watch Narcos tonight!"));
        notes.add(new Note(4, "Pay power bill!"));
        return notes;
    }

    class Note {
        int id;
        String note;

        public Note(int id, String note){
            this.id = id;
            this.note = note;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String notes) {
            this.note = note;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
