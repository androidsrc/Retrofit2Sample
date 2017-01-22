package net.androidsrc.retrofit2.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.androidsrc.retrofit2.R;
import net.androidsrc.retrofit2.api.JsonPlaceHolderApi;
import net.androidsrc.retrofit2.networking.RetrofitProvider;
import net.androidsrc.retrofit2.ui.adapter.AllPostsAdapter;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PostsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private JsonPlaceHolderApi api;
    private AllPostsAdapter adapter;
    private ProgressDialog progress;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");

        //initialize adapter
        adapter = new AllPostsAdapter();

        //reference recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //setup layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //set adapter to recycler view
        recyclerView.setAdapter(adapter);

        //create api object from retrofit
        api = RetrofitProvider.get().create(JsonPlaceHolderApi.class);

        //get all posts
        subscription = api.getPosts().subscribeOn(Schedulers.io())       //setting up worker thread
                .observeOn(AndroidSchedulers.mainThread())               //setting up thread where result will be delivered
                .doOnSubscribe(() -> progress.show())                    //show progress bar on subscribe
                .doOnCompleted(() -> progress.dismiss())                 //dismiss progress bar on complete
                .subscribe(posts -> {                                    //result will be delivered here
                    adapter.setData(posts);                              //sending api result to recycler adapter
                }, err -> {                                              //error from api comes here
                    err.printStackTrace();                               //printing stack trace in case of err
                    progress.dismiss();                                  //hiding progressbar as onComplete is not called in case of error
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }
}
