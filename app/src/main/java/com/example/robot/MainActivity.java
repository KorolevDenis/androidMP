package com.example.robot;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button forwardButton;
    private Button leftButton;
    private Button rightButton;
    private Button startButton;
    private Button stopButton;
    private Button rightDalnButton;
    private Button leftDalnButton;

    private boolean start = false;

    private TextView cmView;
    private TextView messageTextView;

    private APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiInterface = APIClient.getApi();

        cmView = (TextView) findViewById(R.id.text_cm_view);
        cmView.setMovementMethod(new ScrollingMovementMethod());
        messageTextView = (TextView) findViewById(R.id.message_text_view);

        rightDalnButton = (Button) findViewById(R.id.right_daln_button);
        rightDalnButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    sendAction(1, "rightDaln");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    sendAction(0, "rightDaln");
                }
                return false;
            }
        });

        leftDalnButton = (Button) findViewById(R.id.left_daln_button);
        leftDalnButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    sendAction(1, "leftDaln");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    sendAction(0, "leftDaln");
                }
                return false;
            }
        });

        leftButton = (Button) findViewById(R.id.button_left);
        leftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    sendAction(1, "left");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    sendAction(0, "left");
                }
                return false;
            }
        });

        rightButton = (Button) findViewById(R.id.button_right);
        rightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    sendAction(1, "right");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    sendAction(0, "right");
                }
                return false;
            }
        });

        forwardButton = (Button) findViewById(R.id.button_forward);
        forwardButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    sendAction(1, "forward");
                    System.out.println("forward down");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    sendAction(0, "forward");
                }
                return false;
            }
        });

        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendAction(1, "start");
                start = true;
            }
        });

        stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendAction(0, "start");
                start = false;
            }
        });

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    synchronized (this) {
                        if (start) {
                            getCm();
                        }
                        try {
                            wait(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            ;
        }).start();
    }

    private void sendAction(Integer turnOn, String action) {
        Call<StringResponse> call = APIClient.getApi().sendAction(turnOn, action);

        call.enqueue(new Callback<StringResponse>() {
            @Override
            public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                if (!response.isSuccessful()) {
                    System.out.println(response.message());
                    call.cancel();
                }

                if (action.equals("start")) {
                    if (turnOn == 1) {
                        messageTextView.setText("Robot successfully started");
                    } else {
                        messageTextView.setText("Robot successfully stopped");
                    }
                }
            }

            @Override
            public void onFailure(Call<StringResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void getCm() {
        Call<StringResponse> call = APIClient.getApi().getCm();

        call.enqueue(new Callback<StringResponse>() {
            @Override
            public void onResponse(Call<StringResponse> call, Response<StringResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String cm = (String) response.body().getMessage();
                    if (!cm.trim().equals("")) {
                        cmView.append("Расстояние до объекта: " + cm.trim() + " см\n");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), response.message(),
                            Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            }

            @Override
            public void onFailure(Call<StringResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }
}