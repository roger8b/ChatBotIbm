package com.example.roger.chatbotibm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;

public class MainActivity extends AppCompatActivity {


    String outputText;
    String inputText;
    MessageRequest request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView conversation = (TextView) findViewById(R.id.conversation);
        final EditText userInput = (EditText) findViewById(R.id.user_input);

        final ConversationService myConversationService = new ConversationService(
                "2018-06-20",
                getString(R.string.username),
                getString(R.string.password)
        );


        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    inputText = userInput.getText().toString();
                    conversation.append(
                            Html.fromHtml("<p><b>You:</b> " + inputText + "</p>"));
                    request = new MessageRequest.Builder()
                            .inputText(inputText)
                            .build();

                    userInput.setText("");

                    myConversationService
                            .message(getString(R.string.workspace), request)
                            .enqueue(new ServiceCallback<MessageResponse>() {
                                @Override
                                public void onResponse(MessageResponse response) {

                                    outputText = response.getText().get(0);

                                    if (response.getIntents().get(0).getIntent()
                                            .endsWith("RequestQuote")) {
                                        // More code here
                                    }

                                }

                                @Override
                                public void onFailure(Exception e) {

                                }
                            });

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            conversation.append(
                                    Html.fromHtml("<p><b>Bot:</b> " +
                                            outputText + "</p>")
                            );
                        }
                    });


                }
                return false;
            }
        });

    }


}
