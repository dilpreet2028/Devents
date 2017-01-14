package com.dilpreet2028.devents.Interfaces;

/**
 * Created by dilpreet on 12/1/17.
 */

public interface LoginInteractor {

	public void login(String name,String email,OnLoginFinished onLoginFinished);

	public interface OnLoginFinished{
		public void onSuccess(String email);
		public void onError(String msg);
	}
}
