/*
 *  Copyright (c) 2014, Facebook, Inc. All rights reserved.
 *
 *  You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 *  copy, modify, and distribute this software in source code or binary form for use
 *  in connection with the web services and APIs provided by Facebook.
 *
 *  As with any software that integrates with the Facebook platform, your use of
 *  this software is subject to the Facebook Developer Principles and Policies
 *  [http://developers.facebook.com/policy/]. This copyright notice shall be
 *  included in all copies or substantial portions of the software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package ufba.mypersonaltrainner.parseLogin;


import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import ufba.mypersonaltrainner.R;

public class SampleApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();

    // Required - Initialize the Parse SDK
    Parse.enableLocalDatastore(this);
    Parse.initialize(this, getString(R.string.parse_app_id),
        getString(R.string.parse_client_key));
    Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

    // Optional - If you don't want to allow Facebook login, you can
    // remove this line (and other related ParseFacebookUtils calls)
    ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));


    ParsePush.subscribeInBackground("", new SaveCallback() {
        @Override
        public void done(ParseException e) {
            if (e == null) {
                Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
            } else {
                Log.e("com.parse.push", "failed to subscribe for push", e);
            }
        }
    });
  }
}
