package net.androidsrc.retrofit2.api;

import net.androidsrc.retrofit2.model.response.Post;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by aman.yadav on 21/01/17.
 */

public interface JsonPlaceHolderApi {
    @GET("posts")
    Observable<List<Post>> getPosts();
}
