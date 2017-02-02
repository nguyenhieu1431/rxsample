package com.rxandroidsample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.rxandroidsample.data.model.Course;
import com.rxandroidsample.data.model.CourseItem;
import com.rxandroidsample.data.model.StubItem;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Admin on 1/18/2017.
 */

public class ServiceHelper {
    private static Retrofit.Builder retrofitBuilder;
    private static OkHttpClient.Builder httpClient;
    private static Context context;

    private ServiceHelper() {
    }

    public static void initServer() {
        initService(Api.class);
    }

    public static <S> S initService(Class<S> apiClass) {
        buildRetrofitBuilder("https://jsonplaceholder.typicode.com");
        buildClient();
        return retrofitBuilder.client(httpClient.build()).build().create(apiClass);
    }

    private static void buildRetrofitBuilder(String url) {
        if (retrofitBuilder == null) {
            retrofitBuilder = new Retrofit.Builder()
                    .baseUrl(url)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create());
        }
    }

    private static void buildClient() {
        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(initInterceptor());
        }
    }

    private static Interceptor initInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("User-Agent", "Your-App-Name")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        };
    }

    public static Observable<List<PostItem>> getPost() {
        Api api = initService(Api.class);
        return api.getPost();
    }

    public static Observable<StubItem> searchCourses(String keyWorld, int page) {
        JsonObject object = new JsonObject();
        object.addProperty("keyword", keyWorld);

        retrofitBuilder.baseUrl("http://52.43.17.189/api/");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(interceptor);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIwOSwiaXNzIjoiaHR0cHM6XC9cL3d3dy5hZG1pcmVsYWIuY29tXC9hcGlcL2xvZ2luIiwiaWF0IjoxNDg0ODEzOTM0LCJleHAiOjE0ODc0MDU5MzQsIm5iZiI6MTQ4NDgxMzkzNCwianRpIjoiZTJhZjQzMDkwY2QyYWUzYjE2NzNhMzFjOWI2MzY3ZmYifQ.Bhs2EdHxjcfzU7SxjrsB7pH8jHVV6RVQcO67m1IZFc8")
                        .header("Content-Type", "application/json")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        Api api = retrofitBuilder.client(httpClient.build()).build().create(Api.class);

        return api.getCourse(object, page);
    }


    private static SSLSocketFactory buildTrust() {
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(keyStoreType);

            keyStore.load(null, null);

            // creating a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // creating an SSLSocketFactory that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            return sslContext.getSocketFactory();

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }


        return null;
    }


    @SuppressLint("TrustAllX509TrustManager")
    private static SSLSocketFactory trustAllHosts() {
        X509TrustManager easyTrustManager = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // Oh, I am easy!
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // Oh, I am easy!
            }

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        };
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{easyTrustManager};
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
           return sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Api getApi() {
        return initService(Api.class);
    }
}
