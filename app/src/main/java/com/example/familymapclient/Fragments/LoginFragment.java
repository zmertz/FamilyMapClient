package com.example.familymapclient.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.familymapclient.MainActivity;
import com.example.familymapclient.R;
import com.example.familymapclient.Tasks.GetPersonTask;
import com.example.familymapclient.Tasks.LoginTask;
import com.example.familymapclient.Tasks.RegisterTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Request.LoginRequest;
import Request.RegisterRequest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private LoginRequest logRequest;
    private RegisterRequest regRequest;
    private Button signButton;
    private Button regButton;
    private String serverPort;
    private String serverHost;
    private EditText hostField;
    private EditText portField;
    private EditText usernameField;
    private EditText passwordField;
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText emailField;
    private RadioGroup genders;
    private RadioButton maleButton;
    private static Context context;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance(Context con) {
        LoginFragment fragment = new LoginFragment();
        context = con;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logRequest = new LoginRequest(null, null);
        regRequest = new RegisterRequest(null, null, null, null, null, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        signButton = (Button) view.findViewById(R.id.signinButton);
        regButton = (Button) view.findViewById(R.id.registerButton);
        hostField = (EditText) view.findViewById(R.id.serverHostEditText);
        portField = (EditText) view.findViewById(R.id.serverPortEditText);
        usernameField = (EditText) view.findViewById(R.id.userNameEditText);
        passwordField = (EditText) view.findViewById(R.id.passwordEditText);
        firstNameField = (EditText) view.findViewById(R.id.firstNameEditText);
        lastNameField = (EditText) view.findViewById(R.id.lastNameEditText);
        emailField = (EditText) view.findViewById(R.id.emailEditText);


        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginTask logTask = new LoginTask(LoginHandler, logRequest);

                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(logTask);
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterTask regTask = new RegisterTask(RegisterHandler, regRequest);

                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(regTask);
            }
        });

        hostField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkLoginEmpty();
                checkRegisterEmpty();
            }
        });

        portField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkLoginEmpty();
                checkRegisterEmpty();
            }
        });

        usernameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                logRequest.setUserName(charSequence.toString());
                regRequest.setUsername(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkLoginEmpty();
                checkRegisterEmpty();
            }
        });

        passwordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                logRequest.setPassword(charSequence.toString());
                regRequest.setPassword(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkLoginEmpty();
                checkRegisterEmpty();
            }
        });

        firstNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                regRequest.setFirstName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkRegisterEmpty();
            }
        });

        lastNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                regRequest.setLastName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkRegisterEmpty();
            }
        });

        emailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                regRequest.setEmail(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkRegisterEmpty();
            }
        });

        genders = (RadioGroup) view.findViewById(R.id.radioGroup);
        genders.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.femaleRadioButton) {
                    regRequest.setGender("f");
                }
                if(i == R.id.maleRadioButton) {
                    regRequest.setGender("m");
                }
            }
        });
        maleButton = (RadioButton)view.findViewById(R.id.maleRadioButton);
        maleButton.setChecked(true);

        checkRegisterEmpty();

        return view;
    }

    private void checkRegisterEmpty() {
        String s1 = hostField.getText().toString();
        String s2 = portField.getText().toString();
        String s3 = usernameField.getText().toString();
        String s4 = passwordField.getText().toString();
        String s5 = firstNameField.getText().toString();
        String s6 = lastNameField.getText().toString();
        String s7 = emailField.getText().toString();

        if (s1.equals("") || s2.equals("") || s3.equals("") || s4.equals("") || s5.equals("") || s6.equals("") || s7.equals("")) {
            regButton.setEnabled(false);
        } else {
            regButton.setEnabled(true);
        }
    }

    private void checkLoginEmpty() {
        String s1 = hostField.getText().toString();
        String s2 = portField.getText().toString();
        String s3 = usernameField.getText().toString();
        String s4 = passwordField.getText().toString();

        if (s1.equals("") || s2.equals("") || s3.equals("") || s4.equals("")) {
            signButton.setEnabled(false);
        } else {
            signButton.setEnabled(true);
        }
    }

    Handler LoginHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();
            boolean isSuccess = false;
            String authToken = null;
            String personID = null;
            isSuccess = bundle.getBoolean("rString");

            if(!isSuccess) {
                //bad toast here
                Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                authToken = bundle.getString("auth");
                personID = bundle.getString("pID");
                GetPersonTask personTask = new GetPersonTask(getDataHandler, authToken, personID);
                //GetEventTask eventTask = new GetEventTask(getDataHandler, authToken);

                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(personTask);
                //executor.submit(eventTask);
            }
        }
    };

    Handler RegisterHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();
            boolean isSuccess = false;
            String authToken = null;
            String personID = null;
            isSuccess = bundle.getBoolean("rString");

            if(!isSuccess) {
                //bad toast here
                Toast.makeText(getContext(), "Register Failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Register Successful", Toast.LENGTH_SHORT).show();

                authToken = bundle.getString("auth");
                personID = bundle.getString("pID");
                GetPersonTask personTask = new GetPersonTask(getDataHandler, authToken, personID);
                //GetEventTask eventTask = new GetEventTask(getDataHandler, authToken);

                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(personTask);
                //executor.submit(eventTask);
            }
        }
    };

    Handler getDataHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();
            String firstName = bundle.getString("firstName");
            String lastName = bundle.getString("lastName");
            Toast.makeText(getContext(), firstName +" "+ lastName, Toast.LENGTH_SHORT).show();

            MainActivity activity = (MainActivity) getActivity();
            activity.switchFragment();
        }
    };

}