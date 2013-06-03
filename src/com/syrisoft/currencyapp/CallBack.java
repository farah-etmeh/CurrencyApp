package com.syrisoft.currencyapp;

public interface CallBack<T> {

	public void onFinished(boolean succeeded, T result, String error);
	
}
