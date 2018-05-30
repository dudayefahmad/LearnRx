package com.ahmaddudayef.learnrx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.MaybeObserver;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.observables.MathObservable;

public class RxMath extends AppCompatActivity {
    private static final String TAG = RxMath.class.getSimpleName();

//    @BindView(R.id.max) Button max;
//    @BindView(R.id.min) Button min;
//    @BindView(R.id.sum) Button sum;
//    @BindView(R.id.avarage) Button avarage;
//    @BindView(R.id.count) Button count;
//    Unbinder unbinder;

    Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_math);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.max, R.id.min, R.id.sum, R.id.avarage, R.id.count, R.id.reduce, R.id.custoType})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.max:
                sampleMax();
                Toast.makeText(this, "BOOOMMMMM", Toast.LENGTH_SHORT).show();
                break;
            case R.id.min:
                sampleMin();
                break;
            case R.id.sum:
                sampleSum();
                break;
            case R.id.avarage:
                sample_avarage();
                break;
            case R.id.count:
                sample_count();
                break;
            case R.id.reduce:
                sample_reduce();
                break;
            case R.id.custoType:
                sample_custom_data_types();
                break;
        }
    }

    private void sample_custom_data_types() {
        /**
         * Not only on primitive datatypes, we can also perform mathematical operators on custom datatypes too. We are going to use Java8 stream API to perform the comparator operations. So, if you are using Android Studio, make sure you enable Java8 support to use the APIs. This causes the android project to target higher API devices i.e minSdkVersion to 24.
         *
         * Let’s consider an example of finding the elderly person in a list. For this we create a datatype of Person with name and age attributes. Using the Comparator.comparing(), we can easily creates an Observable that emits the max aged person in the list.
         **/
        List<Person> person = new ArrayList<>();
        person.addAll(getAllPerson());

        Observable<Person> personObservable = Observable.from(person);

        MathObservable.from(personObservable)
                .max(Comparator.comparing(Person::getAge))
                .subscribe(new Observer<Person>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Person person) {
                        Log.d(TAG, "Person with max age: " + person.getName() + ", " + person.getAge() + " yrs");
                    }
                });
    }

    private List<Person> getAllPerson() {
        List<Person> persons = new ArrayList<>();
        Person p1 = new Person("Lucy", 24);
        persons.add(p1);

        Person p2 = new Person("John", 45);
        persons.add(p2);

        Person p3 = new Person("Obama", 51);
        persons.add(p3);

        return persons;
    }

    private void sample_reduce() {
        /**
         * Reduce applies a function on each item and emits the final result. First, it applies a function to first item, takes the result and feeds back to same function on second item. This process continuous until the last emission. Once all the items are over, it emits the final result.
         *
         * Below we have an Observable that emits numbers from 1 to 10. The reduce() operator calculates the sum of all the numbers and emits the final result.
         **/
        io.reactivex.Observable
                .range(1, 10)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer number, Integer sum) throws Exception {
                        return number + sum;
                    }
                })
                .subscribe(new MaybeObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        Log.e(TAG, "Sum of numbers from 1 - 10 is: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete");
                    }
                });
    }

    private void sample_count() {
        /**
         * Counts number of items emitted by an Observable and emits only the count value.
         *
         * Below, we have an Observable that emits both Male and Female users. We can count number of Male users using count() operator as shown.
         *
         * filter() filters the items by gender by applying user.getGender().equalsIgnoreCase(“male”) on each emitted item.
         **/
        getUserObservable()
                .filter(new Predicate<User>() {
                    @Override
                    public boolean test(User user) throws Exception {
                        return user.gender.equalsIgnoreCase("male");
                    }
                })
                .count()
                .subscribeWith(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Long count) {
                        Log.d(TAG, "Male users count: " + count);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private io.reactivex.Observable<User> getUserObservable() {
        String[] maleUsers = new String[]{"Mark", "John", "Trump", "Obama"};
        String[] femaleUsers = new String[]{"Lucy", "Scarlett", "April"};

        final List<User> users = new ArrayList<>();

        for (String name : maleUsers) {
            User user = new User();
            user.setName(name);
            user.setGender("male");

            users.add(user);
        }

        for (String name : femaleUsers) {
            User user = new User();
            user.setName(name);
            user.setGender("female");

            users.add(user);
        }

        return io.reactivex.Observable
                .create(new ObservableOnSubscribe<User>() {
                    @Override
                    public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                        for (User user : users) {
                            if (!emitter.isDisposed()) {
                                emitter.onNext(user);
                            }
                        }

                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }
                    }
                }).subscribeOn(Schedulers.io());
    }

    public class User {
        String name;
        String email;
        String gender;
        DeepAboutMap.Address address;

        // getters and setters

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public DeepAboutMap.Address getAddress() {
            return address;
        }

        public void setAddress(DeepAboutMap.Address address) {
            this.address = address;
        }
    }

    public class Address {
        String address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }


    private void sample_avarage() {
        /**
         * Calculates the average of all the items emitted by an Observable and emits only the Average value.
         *
         * The below example calculates the average value of integers using averageInteger() method. To calculate average of other datatypes, averageFloat(), averageDouble() and averageLong() are available.
         **/
        Integer[] numbers = {5, 101, 404, 22, 3, 1024, 65};

        Observable<Integer> averageInteger = Observable.from(numbers);
        MathObservable
                .averageInteger(averageInteger)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "Average: " + integer);
                    }
                });
    }

    private void sampleSum() {
        /**
         * Calculates the sum of all the items emitted by an Observable and emits only the Sum value. In the below example, sumInteger() is used to calculate the sum of Integers. Likewise, we have sumFloat(), sumDouble() and sumLong() available to calculate sum of other primitive datatypes.
         **/
        Integer[] numbers = {5, 101, 404, 22, 3, 1024, 65};

        Observable<Integer> sumInteger = Observable.from(numbers);

        MathObservable
                .sumInteger(sumInteger)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "Sum value: " + integer);
                    }
                });
    }

    private void sampleMin() {
        /**
         * Min() operator emits the minimum valued item in the Observable data set.
         **/
        Integer[] numbers = {5, 101, 404, 22, 3, 1024, 65};
        Observable<Integer> observable = Observable.from(numbers);

        MathObservable
                .min(observable)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "Min value: " + integer);
                    }
                });
    }

    private void sampleMax() {
        /**
         * Max() operator finds the maximum valued item in the Observable sequence and emits that value.
         * */
        Integer[] numbers = {5, 101, 4040, 22, 3, 1024, 65};
        Observable<Integer> observable = Observable.from(numbers);

        MathObservable
                .max(observable)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "complete emitted all data");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "Max value: " + integer);
                    }
                });
    }

    private class Person {
        String name;
        int age;

        public Person(String name, int age){
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}