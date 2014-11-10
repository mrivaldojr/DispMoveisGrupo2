package ufba.mypersonaltrainner;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class PerfilFragment extends Fragment {


	private TextView txtViewNome;
	private ProfilePictureView userProfilePictureView;
	
	
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PerfilFragment newInstance(int sectionNumber) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PerfilFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_side_bar, container, false);
        
        //instancia o usuário logado
        ParseUser user =  ParseUser.getCurrentUser();
        
        String nome = user.getString("name");
        
        txtViewNome = (TextView) rootView.findViewById(R.id.user_name);
        txtViewNome.setText(nome);
        userProfilePictureView = (ProfilePictureView) rootView.findViewById(R.id.img_perfil);
        
        Session session = ParseFacebookUtils.getSession();
		if (session != null && session.isOpened()) {
			makeMeRequest();
		}
        
        return rootView;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((SideBarActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
    
    //////////////////////////////////////////////////////////////////////////
    private void makeMeRequest() {
		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
			new Request.GraphUserCallback() {
				@Override
				public void onCompleted(GraphUser user, Response response) {
					if (user != null) {
						// Create a JSON object to hold the profile info
						JSONObject userProfile = new JSONObject();
						try {
							// Populate the JSON object
							userProfile.put("facebookId", user.getId());
							userProfile.put("name", user.getName());
							if (user.getProperty("gender") != null) {
								userProfile.put("gender", (String) user.getProperty("gender"));
							}
							if (user.getProperty("email") != null) {
								userProfile.put("email", (String) user.getProperty("email"));
							}

							// Save the user profile info in a user property
							ParseUser currentUser = ParseUser.getCurrentUser();
							currentUser.put("profile", userProfile);
							currentUser.saveInBackground();

							// Show the user info
							updateViewsWithProfileInfo();
						} catch (JSONException e) {
							//Log.d(IntegratingFacebookTutorialApplication.TAG, "Error parsing returned user data. " + e);
						}

					} else if (response.getError() != null) {
						if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY) || 
							(response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
							//Log.d(IntegratingFacebookTutorialApplication.TAG, "The facebook session was invalidated." + response.getError());
							//onLogoutButtonClicked();
						} else {
							//Log.d(IntegratingFacebookTutorialApplication.TAG, 
								//"Some other error: " + response.getError());
						}
					}
				}
			}
		);
		request.executeAsync();
	}
    
    private void updateViewsWithProfileInfo() {
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser.has("profile")) {
			JSONObject userProfile = currentUser.getJSONObject("profile");
			try {
				String id = userProfile.getString("facebookId");
				if (userProfile.has("facebookId")) {
					userProfilePictureView.setProfileId(id);
				} else {
					// Show the default, blank user profile picture
					userProfilePictureView.setProfileId(null);
				}
				
				/*if (userProfile.has("name")) {
					userNameView.setText(userProfile.getString("name"));
				} else {
					userNameView.setText("");
				}
				
				if (userProfile.has("gender")) {
					userGenderView.setText(userProfile.getString("gender"));
				} else {
					userGenderView.setText("");
				}
				
				if (userProfile.has("email")) {
					userEmailView.setText(userProfile.getString("email"));
				} else {
					userEmailView.setText("");
				}*/
				
			} catch (JSONException e) {
				//Log.d(IntegratingFacebookTutorialApplication.TAG, "Error parsing saved user data.");
			}
		}
	}
	
}
