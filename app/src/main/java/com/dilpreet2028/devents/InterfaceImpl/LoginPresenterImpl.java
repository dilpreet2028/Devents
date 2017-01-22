package com.dilpreet2028.devents.InterfaceImpl;

import android.net.Uri;

import com.dilpreet2028.devents.Interfaces.LoginInteractor;
import com.dilpreet2028.devents.Interfaces.LoginPresenter;
import com.dilpreet2028.devents.Interfaces.LoginView;
import com.dilpreet2028.devents.Utils.Utility;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

/**
 * Created by dilpreet on 12/1/17.
 */

public class LoginPresenterImpl implements LoginPresenter,LoginInteractor.OnLoginFinished {

	private LoginView loginView;
	private LoginInteractor loginInteractor;
	public LoginPresenterImpl(LoginView loginView) {
		this.loginView=loginView;
		loginInteractor=new LoginInteractorImpl();
	}

	@Override
	public void processGoogleSignInData(GoogleSignInResult result) {
		if (result.isSuccess()) {

			GoogleSignInAccount account=result.getSignInAccount();
			String email=account.getEmail();
			String name=account.getDisplayName();
//		Utility.logger(email);
			loginInteractor.login(name,email,this);
		}
	}

	@Override
	public void onSuccess(String email) {
		loginView.onSucess(email);
	}

	@Override
	public void onError(String msg) {
		loginView.onError(msg);
	}
}
