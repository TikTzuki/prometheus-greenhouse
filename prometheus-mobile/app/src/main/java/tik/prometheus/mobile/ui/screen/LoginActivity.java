package tik.prometheus.mobile.ui.screen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import tik.prometheus.mobile.Configs;
import tik.prometheus.mobile.R;
import tik.prometheus.mobile.ZApplication;
import tik.prometheus.mobile.models.AuthRequest;
import tik.prometheus.mobile.models.AuthResponse;


public class LoginActivity extends AppCompatActivity {
    interface LoginApi {

        @POST("/connect/login")
        Call<AuthResponse> login(@Body AuthRequest authRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Button btn = findViewById(R.id.buttonLogin);
        btn.setOnClickListener(view -> {
            String baseUrl = Configs.INSTANCE.getConfigs().getRestServiceHost() + ":" + Configs.INSTANCE.getConfigs().getRestServicePort();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            EditText editPersonName = findViewById(R.id.editTextTextPersonName);
            EditText editPassword = findViewById(R.id.editTextTextPassword);

            LoginApi loginApi = retrofit.create(LoginApi.class);
            loginApi.login(new AuthRequest(editPersonName.getText().toString(), editPassword.getText().toString()))
                    .enqueue(new Callback<AuthResponse>() {
                        @Override
                        public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                            if (response.isSuccessful()) {
                                Configs.INSTANCE.setACCESS_TOKEN(response.body().getToken());
                                startMainActivity();
                            } else {
                                showToast(response.raw().toString());
                            }

                        }

                        @Override
                        public void onFailure(Call<AuthResponse> call, Throwable t) {
                            showToast(t.toString());
                        }
                    });

        });


    }

    private void startMainActivity() {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        ZApplication zApplication = (ZApplication) getApplication();
        zApplication.initContainer();
        LoginActivity.this.startActivity(mainIntent);
    }

    protected void showToast(CharSequence value) {
        Toast.makeText(this, value, Toast.LENGTH_LONG).show();
    }
}
