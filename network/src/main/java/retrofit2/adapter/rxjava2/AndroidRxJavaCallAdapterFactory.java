package retrofit2.adapter.rxjava2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class AndroidRxJavaCallAdapterFactory extends CallAdapter.Factory {
    private RxJava2CallAdapterFactory mFactory;

    public static AndroidRxJavaCallAdapterFactory create() {
        return new AndroidRxJavaCallAdapterFactory();
    }

    private AndroidRxJavaCallAdapterFactory() {
        mFactory = RxJava2CallAdapterFactory.create();
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new AndroidRxJavaCallAdapter(mFactory.get(returnType, annotations, retrofit));
    }
}
