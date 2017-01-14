package com.dilpreet2028.devents.Interfaces;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;

/**
 * Created by dilpreet on 12/1/17.
 */

public interface LoginPresenter {
	public void processGoogleSignInData(GoogleSignInResult result);
}
