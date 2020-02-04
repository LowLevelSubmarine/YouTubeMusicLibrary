package com.lowlevelsubmarine.ytml.actions;

public class RestAction<T> {

    private final Action<T> action;

    public RestAction(Action<T> action) {
        this.action = action;
    }

    public T complete() {
        return action.run();
    }

    public void queue(CompleteHook<T> completeHook) {
        new Runner<>(this.action, completeHook);
    }

    private static class Runner<T> extends Thread {

        private final Action<T> action;
        private final CompleteHook<T> completeHook;

        public Runner(Action<T> action, CompleteHook<T> completeHook) {
            this.action = action;
            this.completeHook = completeHook;
            this.start();
        }

        @Override
        public void run() {
            this.completeHook.onComplete(action.run());
        }

    }

    public interface CompleteHook<T> {
        void onComplete(T t);
    }

}
