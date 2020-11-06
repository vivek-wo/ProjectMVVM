package retrofit2.adapter.rxjava2;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class OrderBodyObservable<T> extends Observable<T> {
    private Observable<T> mBodyObservable;

    OrderBodyObservable(Observable<T> observable) {
        mBodyObservable = observable;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        mBodyObservable.subscribe(new OrderBodyObserver<T>(observer));
    }

    private static class OrderBodyObserver<R> implements Observer<R> {
        private final Observer<? super R> observer;
        private boolean terminated;

        OrderBodyObserver(Observer<? super R> observer) {
            this.observer = observer;
        }

        @Override
        public void onSubscribe(Disposable disposable) {
            observer.onSubscribe(disposable);
        }

        @Override
        public void onNext(R r) {
            //TODO 定制返回逻辑
            observer.onNext(r);
        }

        @Override
        public void onComplete() {
            if (!terminated) {
                observer.onComplete();
            }
        }

        @Override
        public void onError(Throwable throwable) {
            if (!terminated) {
                observer.onError(throwable);
            }
        }
    }
}
