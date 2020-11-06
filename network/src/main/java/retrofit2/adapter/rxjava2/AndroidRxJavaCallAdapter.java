package retrofit2.adapter.rxjava2;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.CallAdapter;

class AndroidRxJavaCallAdapter<R> implements CallAdapter<R, Object> {
    private CallAdapter mAdapter;

    AndroidRxJavaCallAdapter(CallAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public Type responseType() {
        return mAdapter.responseType();
    }

    @Override
    public Object adapt(Call<R> call) {
        Object object = mAdapter.adapt(call);
        if (object instanceof BodyObservable) {
            object = new OrderBodyObservable<>((Observable<?>) object);
        }
        return object;
    }
}
